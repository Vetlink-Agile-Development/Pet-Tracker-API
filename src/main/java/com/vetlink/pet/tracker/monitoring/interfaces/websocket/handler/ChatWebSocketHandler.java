package com.vetlink.pet.tracker.monitoring.interfaces.websocket.handler;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateActivityCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllGeoFencesByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.*;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.resource.CurrentLocationResource;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.resource.GpsResource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

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
        var apiKey = getRoomNameFromSession(session);
        var messageToString = "ALARM_ON";
        broadcastMessageToRoom(apiKey, messageToString, session);
    }


    private void broadcastMessageToRoom(String roomName, String message, WebSocketSession senderSession) {
        Set<WebSocketSession> sessions = roomSessions.get(roomName);
        if (sessions != null) {
            synchronized (sessions) {
                sessions.forEach(session -> {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (Exception e) {
                        e.printStackTrace();
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
