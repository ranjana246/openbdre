/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wipro.ats.bdre.md.api.util;

import com.wipro.ats.bdre.MDConfig;
import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

/**
 * Created by MI294210 on 08-06-2015.
 */

/**
 * This program implements an application which acts as an producer class for sending notifications of
 * TermJob and HaltJob to the messaging server.
 */
public class StatusNotification {
    private static final Logger LOGGER = Logger.getLogger(StatusNotification.class);
    private Connection connection;

    /**
     * This constructor accepts parameters and establishes connection with the messaging server and creates
     * session followed by queue and producer creation and sends the messages generated by the TermJob and HaltJob.
     *
     * @param msg       HaltJob and TermJob notification message.
     * @param queueName Name of the queue where the messages will be passed.
     * @return This method returns information regarding that parent process and all sub-process of parent process.
     */
    public StatusNotification(String msg, String queueName) {
        try {
            //populating JNDI using Hashtable
            Hashtable connectionEnv = new Hashtable();
            connectionEnv.put(Context.INITIAL_CONTEXT_FACTORY, MDConfig.getProperty("status-notification.initial-context-factory"));
            connectionEnv.put(Context.PROVIDER_URL, MDConfig.getProperty("status-notification.provider-url"));

            InitialContext jndi = new InitialContext(connectionEnv);
            ConnectionFactory conFactory = (ConnectionFactory) jndi.lookup("ConnectionFactory");
            connection = conFactory.createConnection();

            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //destination can be a queue or a topic
            Destination destination = session.createQueue(queueName);
            //Creating Producer
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage(msg);
            producer.send(message);
            LOGGER.debug("Sent message '" + message.getText() + "'");

        } catch (Exception e) {
            LOGGER.debug("error occurred in status notification", e);

        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (JMSException e) {
                LOGGER.error("Error closing connection", e);
            }
        }
    }

    @Override
    public String toString() {
        return "StatusNotification{" +
                "connection=" + connection +
                '}';
    }
}
