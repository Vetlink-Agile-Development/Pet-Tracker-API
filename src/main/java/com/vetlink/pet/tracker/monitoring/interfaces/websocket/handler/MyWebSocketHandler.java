package com.vetlink.pet.tracker.monitoring.interfaces.websocket.handler;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateActivityCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetDeviceByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ActivityEventName;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ActivityType;
import com.vetlink.pet.tracker.monitoring.domain.services.ActivityCommandService;
import com.vetlink.pet.tracker.monitoring.domain.services.DeviceQueryService;
import com.vetlink.pet.tracker.monitoring.domain.services.HealthMeasureCommandService;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.resource.CreateHealthMeasureResource;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.transform.CreateHealthMeasureCommandFromResourceAssembler;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.transform.HealthMeasureResourceFromEntityAssembler;
import com.vetlink.pet.tracker.shared.domain.exceptions.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final HealthMeasureCommandService healthMeasureCommandService;
    private final DeviceQueryService deviceQueryService;
    private final ActivityCommandService activityCommandService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Mapa para almacenar las sesiones agrupadas por sala
    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    public MyWebSocketHandler(HealthMeasureCommandService healthMeasureCommandService, DeviceQueryService deviceQueryService, ActivityCommandService activityCommandService) {
        this.healthMeasureCommandService = healthMeasureCommandService;
        this.deviceQueryService = deviceQueryService;
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

        var createHealthMeasureResource = objectMapper.readValue(message.getPayload(), CreateHealthMeasureResource.class);
        var apiKey = getRoomNameFromSession(session);

        // Creating healthMeasure
        var createHealthMeasureCommand = CreateHealthMeasureCommandFromResourceAssembler.toCommandFromResource(apiKey, createHealthMeasureResource);
        var healthMeasure = healthMeasureCommandService.handle(createHealthMeasureCommand);

        //Getting device
        var queryDevice = new GetDeviceByPetTrackerDeviceRecordIdQuery(healthMeasure.get().getPetTrackerDeviceRecordId());
        var device = deviceQueryService.handle(queryDevice);

        //Check if healthMeasure was created successfully
        if (healthMeasure.isEmpty()) {
            broadcastMessageToRoom(apiKey, "Failed to create", session);
            throw new ValidationException("");
        }

        // check for any threshold surpass bpm
        var bpm = healthMeasure.get().getBpm().bpm();
        if (device.get().getHealthThresholds().maxBpm() < bpm) {
            var createActivityCommand = new CreateActivityCommand(device.get().getPetTrackerDeviceRecordId(), ActivityEventName.HIGH_HEART_RATE, ActivityType.BPM);
            activityCommandService.handle(createActivityCommand);
        } else if (device.get().getHealthThresholds().minBpm() > bpm) {
            var createActivityCommand = new CreateActivityCommand(device.get().getPetTrackerDeviceRecordId(), ActivityEventName.LOW_HEART_RATE, ActivityType.BPM);
            activityCommandService.handle(createActivityCommand);
        }

        // check for any threshold surpass spo2
        var spo2 = healthMeasure.get().getSpo2().spo2();
        if (device.get().getHealthThresholds().maxSpO2() < spo2) {
            var createActivityCommand = new CreateActivityCommand(device.get().getPetTrackerDeviceRecordId(), ActivityEventName.HIGH_SPO2, ActivityType.SPO2);
            activityCommandService.handle(createActivityCommand);
        } else if (device.get().getHealthThresholds().minSpO2() > spo2) {
            var createActivityCommand = new CreateActivityCommand(device.get().getPetTrackerDeviceRecordId(), ActivityEventName.LOW_SPO2, ActivityType.SPO2);
            activityCommandService.handle(createActivityCommand);
        }

        var healthMeasureResource = HealthMeasureResourceFromEntityAssembler.toResourceFromEntity(healthMeasure.get());
        var healthMeasureResourceString = objectMapper.writeValueAsString(healthMeasureResource);
        broadcastMessageToRoom(apiKey, healthMeasureResourceString, session);
    }

    // Método para enviar un mensaje a todas las sesiones
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
}