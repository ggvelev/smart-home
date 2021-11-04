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

import com.hivemq.client.mqtt.datatypes.MqttTopicFilter;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * Component for creating MQTT subscriptions
 */
@Component
public class MqttSubscriber {

    private static final Logger log = LoggerFactory.getLogger(MqttSubscriber.class);

    private final MqttClient mqttClient;
    private final MqttSubscriptionsCache subscriptionsCache;

    public MqttSubscriber(MqttClient mqttClient, MqttSubscriptionsCache subscriptionsCache) {
        this.mqttClient = mqttClient;
        this.subscriptionsCache = subscriptionsCache;
    }

    /**
     * Subscribes to the given topic
     *
     * @param topic     topic to subscribe to
     * @param converter converts incoming {@link Mqtt3Publish} message to {@link T}
     * @param callback  lambda consuming the converted message
     * @param <T>       target type of the payload carried in the incoming {@link Mqtt3Publish}
     */
    public <T> void subscribe(String topic, IncomingMqttMessageConverter<T> converter, Consumer<T> callback) {
        subscribe(topic, publish ->
                converter.apply(publish).ifPresentOrElse(
                        callback,
                        () -> log.warn("Result of incoming message conversion is empty")
                ));
    }

    /**
     * Subscribe to the given topic
     *
     * @param topic    MQTT topic to subscribe
     * @param callback {@link Consumer} to execute operation over the {@link Mqtt3Publish}
     */
    public void subscribe(String topic, Consumer<Mqtt3Publish> callback) {
        log.info("Subscribing to '{}'", topic);
        mqttClient.get().subscribeWith()
                .topicFilter(MqttTopicFilter.of(topic))
                .callback(callback)
                .send()
                .whenComplete((ack, ex) -> {
                    if (ex != null) {
                        log.error("Error subscribing to '{}': {}", topic, ex);
                        return;
                    }
                    subscriptionsCache.add(topic, callback);
                    log.info("Successfully subscribed to '{}'", topic);
                });
    }

    /**
     * Unsubscribes from a given topic
     *
     * @param topic the topic to unsubscribe from
     */
    public void unsubscribe(String topic) {
        log.info("Unsubscribing from '{}'", topic);
        mqttClient.get().unsubscribeWith()
                .topicFilter(MqttTopicFilter.of(topic))
                .send()
                .whenComplete((ack, ex) -> {
                    if (ex != null) {
                        log.error("Error unsubscribing from '{}': {}", topic, ex);
                        return;
                    }
                    subscriptionsCache.remove(topic);
                    log.info("Successfully unsubscribed from '{}'", topic);
                });
    }
}
