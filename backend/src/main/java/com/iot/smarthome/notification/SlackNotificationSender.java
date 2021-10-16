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

package com.iot.smarthome.notification;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Component for sending Slack notification messages
 */
@Slf4j
@Component
public class SlackNotificationSender {

    /**
     * Slack API token
     */
    @Value("${slack.token}")
    private String token;

    /**
     * Default channel where to send messages
     */
    @Value("${slack.channel}")
    private String channel;

    private final Slack slack = Slack.getInstance();

    /**
     * Sends a message to the default channel specified by {@link #channel}
     *
     * @param message message to send
     */
    public void send(String message) {
        send(channel, message);
    }

    /**
     * Sends a message to the given channel
     *
     * @param channel where to send the message
     * @param message the message to send
     */
    public void send(String channel, String message) {
        sendMsg(channel, message);
    }

    private void sendMsg(String channel, String message) {
        try {
            slack.methods(token).chatPostMessage(msg -> msg.channel(channel).text(message));
        } catch (IOException e) {
            log.error("Could not send Slack message request", e);
        } catch (SlackApiException e) {
            log.error("Slack API server error on processing message request", e);
        }
    }

}
