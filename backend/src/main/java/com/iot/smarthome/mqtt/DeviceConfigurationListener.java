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
import com.iot.smarthome.annotation.Listener;
import com.iot.smarthome.dto.DeviceDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@Listener
public class DeviceConfigurationListener implements MqttListener<DeviceDetails> {

    private static final Logger log = LoggerFactory.getLogger(DeviceConfigurationListener.class);

    @Autowired
    private MqttSubscriber subscriber;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${mqtt.listeners.device-configuration.topic}")
    private String topic;

    @PostConstruct
    private void init() {
        setUpSubscription();
    }

    private void setUpSubscription() {
        subscriber.subscribe(
                TopicTemplateVariableType.formatTopic(topic),
                msg -> objectMapper.readValue(msg.getPayloadAsBytes(), DeviceDetails.class),
                this::onReceived
        );
    }

    @Override
    public void onReceived(DeviceDetails deviceDetails) {
        // TODO
        //  log details
        //  store in DB
        //  update DB
    }
}
