package com.iot.smarthome.config;

import com.hivemq.client.mqtt.datatypes.MqttClientIdentifier;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopic;
import com.hivemq.client.mqtt.lifecycle.MqttClientAutoReconnect;
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedContext;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedContext;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.message.auth.Mqtt3SimpleAuth;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * MQTT client configuration and setup
 */
@Configuration
public class MqttClientConfig {

    private static final Logger log = LoggerFactory.getLogger(MqttClientConfig.class);

    @Value("${mqtt.server.host}")
    private InetAddress serverHost;

    @Value("${mqtt.server.port}")
    private Integer serverPort;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.client.username}")
    private String username;

    @Value("${mqtt.client.password}")
    private byte[] password;

    @Value("${mqtt.client.lwt.topic}")
    private String lastWillTopic;

    @Value("${mqtt.client.lwt.message}")
    private String lastWillMessage;

    @Bean
    public Mqtt3AsyncClient mqtt3AsyncClient() {
        final Mqtt3SimpleAuth authentication = Mqtt3SimpleAuth.builder()
                .username(username)
                .password(password)
                .build();

        final MqttClientAutoReconnect autoReconnect = MqttClientAutoReconnect.builder()
                .initialDelay(5L, TimeUnit.SECONDS)
                .maxDelay(30L, TimeUnit.SECONDS)
                .build();

        final Mqtt3Publish lwt = Mqtt3Publish.builder()
                .topic(MqttTopic.of(lastWillTopic))
                .payload(lastWillMessage.getBytes(StandardCharsets.UTF_8))
                .qos(MqttQos.EXACTLY_ONCE)
                .retain(true)
                .build();

        return Mqtt3Client.builder()
                .identifier(MqttClientIdentifier.of(clientId))
                .simpleAuth(authentication)
                .serverHost(serverHost)
                .serverPort(serverPort)
                .addConnectedListener(MqttClientConfig::onConnectedLoggingListener)
                .addDisconnectedListener(MqttClientConfig::onDisconnectedLoggingListener)
                .automaticReconnect(autoReconnect)
                .willPublish(lwt)
                // .sslWithDefaultConfig()
                .buildAsync();
    }

    private static void onConnectedLoggingListener(MqttClientConnectedContext ctx) {
        log.info(
                "MQTT client connected: version=[{}], server=[{}:{}], state=[{}], clientId=[{}]",
                ctx.getClientConfig().getMqttVersion(),
                ctx.getClientConfig().getServerHost(),
                ctx.getClientConfig().getServerPort(),
                ctx.getClientConfig().getState(),
                ctx.getClientConfig().getClientIdentifier().orElse(null)
        );
    }

    private static void onDisconnectedLoggingListener(MqttClientDisconnectedContext ctx) {
        log.info(
                "MQTT client disconnected: source=[{}], version=[{}], server=[{}:{}], state=[{}], clientId=[{}], cause=[{}]",
                ctx.getSource(),
                ctx.getClientConfig().getMqttVersion(),
                ctx.getClientConfig().getServerHost(),
                ctx.getClientConfig().getServerPort(),
                ctx.getClientConfig().getState(),
                ctx.getClientConfig().getClientIdentifier().orElse(null),
                ctx.getCause()
        );
    }

}
