package com.vetlink.pet.tracker.monitoring.interfaces.websocket.handler;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateActivityCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllGeoFencesByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.*;
import com.vetlink.pet.tracker.monitoring.domain.services.ActivityCommandService;
import com.vetlink.pet.tracker.monitoring.domain.services.GeoFenceQueryService;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.resource.CurrentLocationResource;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.resource.GpsResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GpsWebSocketHandler extends TextWebSocketHandler {

    private final GeoFenceQueryService geoFenceQueryService;

    private final ActivityCommandService activityCommandService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Mapa para almacenar las sesiones agrupadas por sala
    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    public GpsWebSocketHandler(GeoFenceQueryService geoFenceQueryService, ActivityCommandService activityCommandService) {
        this.geoFenceQueryService = geoFenceQueryService;
        this.activityCommandService = activityCommandService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomName = getRoomNameFromSession(session);
        System.out.println(roomName);
        roomSessions.putIfAbsent(roomName, Collections.synchronizedSet(new HashSet<>()));
        roomSessions.get(roomName).add(session);

        System.out.println("Nueva conexión en sala: " + roomName + ", sesión: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        GpsResource gpsResource;
        var currentLocationResource = objectMapper.readValue(message.getPayload(), CurrentLocationResource.class);
        var apiKey = getRoomNameFromSession(session);
        var currentLocationCoordinate = new Coordinate(currentLocationResource.latitude(), currentLocationResource.longitude());

        //Check if any current location is in any geofence from the device
        var petTrackerId = new PetTrackerDeviceRecordId(currentLocationResource.petTrackerDeviceRecordId());
        var query = new GetAllGeoFencesByPetTrackerDeviceRecordIdQuery(petTrackerId);
        var geoFences = geoFenceQueryService.handle(query);
        //TODO: Validar caso donde todas las geocercas esten inactivas

        boolean isInsideFromAnyGeoFence = false;
        for (var geofence : geoFences ) {
            if (geofence.getGeoFenceStatus() != GeoFenceStatuses.ACTIVE) continue;
            var isInsideFromThisGeoFence = isPointInPolygon(currentLocationCoordinate, geofence.getCoordinates());
            isInsideFromAnyGeoFence = isInsideFromAnyGeoFence || isInsideFromThisGeoFence;
        }
        if (!isInsideFromAnyGeoFence) {
            // Saving activity from geofence
            var command = new CreateActivityCommand(petTrackerId, ActivityEventName.GEOFENCE_EXIT, ActivityType.GPS);
            activityCommandService.handle(command);

            gpsResource = new GpsResource(
                    currentLocationCoordinate.latitude(),
                    currentLocationCoordinate.longitude(),
                    RiskLevel.DANGER.toString()
            );
        }
        else {
            gpsResource = new GpsResource(
                    currentLocationCoordinate.latitude(),
                    currentLocationCoordinate.longitude(),
                    RiskLevel.SAFE.toString()
            );
        }

        var gpsResourceString = objectMapper.writeValueAsString(gpsResource);
        broadcastMessageToRoom(apiKey, gpsResourceString, session);
    }


    private void broadcastMessageToRoom(String roomName, String message, WebSocketSession senderSession) {
        Set<WebSocketSession> sessions = roomSessions.get(roomName);
        if (sessions != null) {
            synchronized (sessions) {
                sessions.forEach(session -> {
                    if (!session.equals(senderSession)) { // Excluir al remitente
                        try {
                            session.sendMessage(new TextMessage(message));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String roomName = getRoomNameFromSession(session);
        Set<WebSocketSession> sessions = roomSessions.get(roomName);
        if (sessions != null) {
            sessions.remove(session);
            System.out.println("Conexión cerrada en sala: " + roomName + ", sesión: " + session.getId());

            // Si la sala queda vacía, la eliminamos
            if (sessions.isEmpty()) {
                roomSessions.remove(roomName);
            }
        }
    }

    private String getRoomNameFromSession(WebSocketSession session) {
        // Aquí obtén el nombre de la sala del cliente, por ejemplo desde query params
        // Ejemplo: http://localhost:8080/chat?room=room1
        String query = session.getUri().getQuery();
        return query != null && query.startsWith("room=") ? query.substring(5) : "default";
    }

    private static boolean isPointInPolygon(Coordinate point, List<Coordinate> coordinates) {
        int n = coordinates.size();
        if (n < 3) return false; // Debe haber al menos 3 coordenadas

        boolean isInside = false;
        float x = point.longitude();
        float y = point.latitude();

        for (int i = 0, j = n - 1; i < n; j = i++) {
            float xi = coordinates.get(i).longitude();
            float yi = coordinates.get(i).latitude();
            float xj = coordinates.get(j).longitude();
            float yj = coordinates.get(j).latitude();

            // Verificar si el punto está dentro del polígono usando el algoritmo de Ray-Casting
            boolean intersect = ((yi > y) != (yj > y)) &&
                    (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
            if (intersect) {
                isInside = !isInside;
            }
        }

        return isInside;
    }

}
