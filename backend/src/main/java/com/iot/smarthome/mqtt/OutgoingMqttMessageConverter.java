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
 * @see Function
 */
@FunctionalInterface
public interface OutgoingMqttMessageConverter<T> extends Function<T, byte[]> {

    /**
     * Converts generic object from type {@link T} to byte[] to send over MQTT, by rethrowing any checked exceptions
     * during conversion and wrapping them in a {@link RuntimeException}
     *
     * @param input generic {@link T} object (POJO)
     * @return byte array representing the MQTT message payload
     */
    @Override
    default byte[] apply(T input) {
        try {
            return convert(input);
        } catch (Exception e) {
            throw new MqttMessageConversionException("MQTT outgoing message conversion failed", e);
        }
    }

    byte[] convert(T input) throws Exception;
}
