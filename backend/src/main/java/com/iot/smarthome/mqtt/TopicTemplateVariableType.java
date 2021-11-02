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

import java.util.Map;

/**
 * Utility enum for parsing and formatting MQTT topic names with placeholder variables like <b>{deviceId}</b>
 */
public enum TopicTemplateVariableType {

    DEVICE_ID("{deviceId}", String.valueOf(MqttTopicFilter.SINGLE_LEVEL_WILDCARD)),
    COMMAND_ID("{commandId}", String.valueOf(MqttTopicFilter.SINGLE_LEVEL_WILDCARD)),
    USER_ID("{userId}", String.valueOf(MqttTopicFilter.SINGLE_LEVEL_WILDCARD));

    private final String placeholder;
    private final String wildcardReplacement;

    TopicTemplateVariableType(String placeholder, String wildcardReplacement) {
        this.placeholder = placeholder;
        this.wildcardReplacement = wildcardReplacement;
    }

    /**
     * Formats a given MQTT topic with template variables defined and supported by this enumeration, to MQTT topic with
     * single-level wildcards ('+').
     *
     * @param mqttTopic topic with template variables (e.g. "{deviceId}, "{userId} etc.)
     * @return formatted MQTT topic string with template variables replaced with "+"
     * @see com.hivemq.client.mqtt.datatypes.MqttTopicFilter#SINGLE_LEVEL_WILDCARD
     */
    public static String subscriptionTopicFmt(String mqttTopic) {
        for (TopicTemplateVariableType t : values()) {
            mqttTopic = mqttTopic.replace(t.placeholder, t.wildcardReplacement);
        }
        return mqttTopic;
    }

    /**
     * Formats a MQTT topic with template variables by replacing each one with respective value.
     *
     * @param mqttTopic         topic with template variables
     * @param placeholderValues template variable to value mappings
     * @return formatted MQTT topic
     */
    public static String publishTopicFmt(String mqttTopic, Map<TopicTemplateVariableType, String> placeholderValues) {
        for (Map.Entry<TopicTemplateVariableType, String> entry : placeholderValues.entrySet()) {
            TopicTemplateVariableType template = entry.getKey();
            String value = entry.getValue();
            mqttTopic = mqttTopic.replace(template.placeholder, value);
        }
        return mqttTopic;
    }

    /**
     * Returns the position for this template variable in the topic's levels.
     * <p>
     * Example:
     * <p>
     * {@code TopicTemplateVariableType.DEVICE_ID.getPosition("device/{deviceId}/state");} // returns 1
     * <p>
     * {@code TopicTemplateVariableType.COMMAND_ID.getPosition("device/{deviceId}/commands/{commandId}");} // returns 3
     *
     * @param mqttTopic topic name with template variables
     * @return index of this template variable in the topic's levels or -1 if not found
     */
    public int getPosition(String mqttTopic) {
        return MqttTopicFilter.of(mqttTopic).getLevels().indexOf(placeholder);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getWildcardReplacement() {
        return wildcardReplacement;
    }
}
