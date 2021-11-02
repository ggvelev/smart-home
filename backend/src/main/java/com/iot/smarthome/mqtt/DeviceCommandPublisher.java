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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.smarthome.annotation.Publisher;
import com.iot.smarthome.dto.DeviceCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

import static com.iot.smarthome.mqtt.TopicTemplateVariableType.DEVICE_ID;

@Publisher
public class DeviceCommandPublisher {

    private static final Logger log = LoggerFactory.getLogger(DeviceCommandPublisher.class);

    @Autowired
    private MqttMessagePublisher publisher;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${mqtt.publishers.device-command.topic}")
    private String topic;

    private final OutgoingMqttMessageConverter<DeviceCommand> converter = cmd -> objectMapper.writeValueAsBytes(cmd);

    public void publish(String deviceId, DeviceCommand command) {
        final String topicFmt = TopicTemplateVariableType.publishTopicFmt(topic, Map.of(DEVICE_ID, deviceId));

        log.info("Sending command to '{}': {}", topicFmt, command);
        publisher.publish(topicFmt, converter.apply(command));
    }

}
