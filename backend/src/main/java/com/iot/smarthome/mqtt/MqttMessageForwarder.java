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
import org.springframework.util.Assert;

@Component
public class MqttMessageForwarder {

    private static final Logger log = LoggerFactory.getLogger(MqttMessageForwarder.class);

    private final MqttMessagePublisher publisher;
    private final MqttSubscriber subscriber;

    public MqttMessageForwarder(MqttMessagePublisher publisher, MqttSubscriber subscriber) {
        this.publisher = publisher;
        this.subscriber = subscriber;
    }

    /**
     * Forwards any incoming {@link Mqtt3Publish} message for a given source topic to a given destination topic.
     *
     * @param sourceTopic      source topic to listen for incoming messages. May contain {@link
     *                         MqttTopicFilter#SINGLE_LEVEL_WILDCARD}s.
     * @param destinationTopic destination topic where to forward the incoming messages. Must not contain wildcards.
     */
    public void forward(String sourceTopic, String destinationTopic) {
        final MqttTopicFilter srcFilter = MqttTopicFilter.of(sourceTopic);
        final MqttTopicFilter dstFilter = MqttTopicFilter.of(destinationTopic);

        Assert.isTrue(!srcFilter.containsMultiLevelWildcard(), "source should not contain multi-level wildcard");
        Assert.isTrue(!dstFilter.containsWildcards(), "destination should not contain any wildcards");
        Assert.isTrue(!sourceTopic.equals(destinationTopic), "source and destination must not be the same");

        subscriber.subscribe(sourceTopic, p -> {
            log.info("Forward MQTT message from '{}' to '{}'", sourceTopic, destinationTopic);
            publisher.publish(destinationTopic, p.getPayloadAsBytes());
        });
    }
}
