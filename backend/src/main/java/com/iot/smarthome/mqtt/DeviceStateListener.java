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
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.iot.smarthome.annotation.Listener;
import com.iot.smarthome.dto.DeviceState;
import com.iot.smarthome.service.DeviceDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.UUID;

import static com.iot.smarthome.mqtt.TopicTemplateVariableType.DEVICE_ID;

/**
 * Listens for and handles device state updates
 */
@Listener
public class DeviceStateListener implements MqttListener<Mqtt3Publish> {

    private static final Logger log = LoggerFactory.getLogger(DeviceStateListener.class);

    @Autowired
    private MqttSubscriber subscriber;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DeviceDataService deviceDataService;

    @Value("${mqtt.listeners.device-state.topic}")
    private String topic;

    private final IncomingMqttMessageConverter<DeviceState> converter = msg ->
            objectMapper.readValue(msg.getPayloadAsBytes(), DeviceState.class);

    @PostConstruct
    private void init() {
        setUpSubscription();
    }

    private void setUpSubscription() {
        subscriber.subscribe(TopicTemplateVariableType.subscriptionTopicFmt(topic), this::onReceived);
    }

    @Override
    public void onReceived(Mqtt3Publish publish) {
        // Get deviceId from topic:
        final UUID deviceUuid = UUID.fromString(publish.getTopic().getLevels().get(DEVICE_ID.getPosition(topic)));

        // Get payload:
        final DeviceState deviceState = converter.apply(publish);
        log.info("Received device state message from '{}': {}", deviceUuid, deviceState);

        // Write record to influxDB:
        deviceDataService.storeDeviceStateUpdate(deviceUuid.toString(), deviceState);
    }
}