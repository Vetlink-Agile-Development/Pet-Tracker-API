package com.vetlink.pet.tracker.monitoring.interfaces.websocket;

import com.vetlink.pet.tracker.monitoring.interfaces.websocket.handler.ChatWebSocketHandler;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.handler.GpsWebSocketHandler;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.handler.MyWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MyWebSocketHandler webSocketHandler;
    private final GpsWebSocketHandler gpsWebSocketHandler;
    private final ChatWebSocketHandler chatWebSocketHandler;

    public WebSocketConfig(MyWebSocketHandler webSocketHandler, GpsWebSocketHandler gpsWebSocketHandler, ChatWebSocketHandler chatWebSocketHandler) {
        this.webSocketHandler = webSocketHandler;
        this.gpsWebSocketHandler = gpsWebSocketHandler;
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/health-measures-stream").setAllowedOrigins("*");
        registry.addHandler(gpsWebSocketHandler, "/location-stream").setAllowedOrigins("*");
        registry.addHandler(chatWebSocketHandler, "/chat-stream").setAllowedOrigins("*");
    }
}