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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Function;

/**
 * Generic interface representing a converter from an incoming {@link Mqtt3Publish} to object of type {@link T}
 *
 * @param <T> target type for conversion from {@link Mqtt3Publish#getPayloadAsBytes()} or {@link
 *            Mqtt3Publish#getPayload()}
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface IncomingMqttMessageConverter<T> extends Function<Mqtt3Publish, Optional<T>> {

    Logger log = LoggerFactory.getLogger(IncomingMqttMessageConverter.class);

    /**
     * Converts message to {@link Optional}<{@link T}>. If conversion is unsuccessful, the exception is logged and
     * {@link Optional#empty()} is returned.
     *
     * @param publish incoming {@link Mqtt3Publish} message
     * @return the MQTT message payload converted to {@link T} and wrapped in {@link Optional}. {@link Optional#empty()}
     * if conversion failed
     */
    @Override
    default Optional<T> apply(Mqtt3Publish publish) {
        try {
            return Optional.ofNullable(convert(publish));
        } catch (Exception e) {
            log.error("MQTT incoming message conversion failed", e);
            return Optional.empty();
        }
    }

    T convert(Mqtt3Publish input) throws Exception;
}
