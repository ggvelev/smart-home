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

import com.hivemq.client.mqtt.MqttClientState;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.iot.smarthome.config.MqttClientConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Wrapper for {@link Mqtt3AsyncClient} bean defined in {@link MqttClientConfig#mqtt3AsyncClient()}.
 * <p>
 * The actual client is retrieved via {@link #get()} method.
 * <p>
 * Upon initialization. the component attempts to connect the MQTT client to the configured broker.
 * <p>
 * Upon graceful application shutdown, a {@link PreDestroy} hook is registered that disconnects the MQTT client.
 */
@Component
public class MqttClient {

    private final Mqtt3AsyncClient client;

    public MqttClient(Mqtt3AsyncClient client) {
        this.client = client;
    }

    @PostConstruct
    private void init() {
        if (client.getState() == MqttClientState.DISCONNECTED) {
            client.connectWith().cleanSession(false).keepAlive(60).send().join();
        }
    }

    public Mqtt3AsyncClient get() {
        return client;
    }

    @PreDestroy
    private void shutdown() {
        if (client.getState() != MqttClientState.DISCONNECTED) {
            client.disconnect().join();
        }
    }
}
