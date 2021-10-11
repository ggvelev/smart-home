/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2021 Georgi Velev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 ******************************************************************************/

package com.iot.smarthome.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A publisher for MQTT messages
 */
@Component
public class GenericMqttMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(GenericMqttMessagePublisher.class);

    private final MqttClient mqttClient;

    private final ObjectMapper objectMapper;

    public GenericMqttMessagePublisher(MqttClient mqttClient, ObjectMapper objectMapper) {
        this.mqttClient = mqttClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Publishes the given payload to the topic specified by first deserializing it to byte array
     *
     * @param topic   where to publish the payload
     * @param payload the message to publish
     */
    public void publish(String topic, Object payload) {
        try {
            publish(topic, objectMapper.writeValueAsBytes(payload));
        } catch (JsonProcessingException e) {
            log.error("Error on deserialization of MQTT publish payload");
        }
    }

    /**
     * Publishes the given payload to the topic with QoS 2 {@link MqttQos#EXACTLY_ONCE}
     *
     * @param topic   where to publish the payload
     * @param payload the message to publish (as byte array)
     */
    public void publish(String topic, byte[] payload) {
        mqttClient.get().publishWith()
                .topic(MqttTopic.of(topic))
                .qos(MqttQos.EXACTLY_ONCE)
                .payload(payload)
                .send()
                .whenComplete((mqttPub, ex) -> {
                    if (ex != null) {
                        log.error("Error when publishing to '{}' - {}", topic, ex);
                    }
                    else {
                        log.info("Successfully published to '{}'", topic);
                    }
                });
    }

}
