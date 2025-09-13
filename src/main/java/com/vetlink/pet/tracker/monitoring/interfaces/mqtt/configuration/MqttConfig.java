package com.vetlink.pet.tracker.monitoring.interfaces.mqtt.configuration;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {
    @Value("${mqtt.url}")
    private String MQTT_BROKER_URL;

    @Value("${mqtt.client}")
    private String CLIENT_ID;

    @Value("${mqtt.credentials.username}")
    private String USERNAME;

    @Value("${mqtt.credentials.password}")
    private String PASSWORD;

    private static final String[] SUBSCRIBE_TOPICS = {
            "guardian-area/health-thresholds"
    };
    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(MQTT_BROKER_URL, CLIENT_ID, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                System.out.println("Message received on topic: " + topic + ". Content: " + new String(message.getPayload()));

                // Procesamiento según el topic
                switch (topic) {
                    case "guardian-area/health-thresholds":
                        // Procesa mensajes para el topic de comandos
                        // handleCommandMessage(message);
                        break;
                    default:
                        System.out.println("Unrecognized topic: " + topic);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Esta función es opcional para suscripciones
            }
        });

        client.connect(options);
//        for (String topic : SUBSCRIBE_TOPICS) {
//            client.subscribe(topic);
//        }

        return client;
    }

}
