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

import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

// TODO - to be decided later what/how and if this will be implemented and is it really needed
@Component
public class MqttSubscriptionsCache {

    private final ConcurrentMap<String, List<Consumer<Mqtt3Publish>>> cache = new ConcurrentHashMap<>();

    /**
     * Removes cache entry that has the specified topic as a key
     *
     * @param topic topic to remove the mapping for
     */
    public void remove(String topic) {
        //        cache.computeIfPresent(topic, (theTopic, callbacks) -> callbacks.stream().filter());
    }

    public void add(String topic, Consumer<Mqtt3Publish> consumer) {
        //        cache.computeIfAbsent(topic, theTopic -> )
    }

    public Object update() {
        return null;
    }

    public Object retrieve() {
        return null;
    }

    public Object clear() {
        return null;
    }
}
