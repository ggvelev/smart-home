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
import com.iot.smarthome.exception.MqttMessageConversionException;

import java.util.function.Function;

/**
 * Generic interface representing a converter from an incoming {@link Mqtt3Publish} to object of type {@link T}
 *
 * @param <T> target type for conversion from {@link Mqtt3Publish#getPayloadAsBytes()} or {@link
 *            Mqtt3Publish#getPayload()}
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface IncomingMqttMessageConverter<T> extends Function<Mqtt3Publish, T> {

    /**
     * Converts message to {@link T} by rethrowing any checked exceptions during conversion and wrapping them in a
     * {@link RuntimeException}
     *
     * @param publish incoming {@link Mqtt3Publish} message
     * @return the MQTT message payload converted to {@link T}
     */
    @Override
    default T apply(Mqtt3Publish publish) {
        try {
            return convert(publish);
        } catch (Exception e) {
            throw new MqttMessageConversionException("MQTT incoming message conversion failed", e);
        }
    }

    T convert(Mqtt3Publish input) throws Exception;
}
