/*
 * Copyright 2011 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package de.dennisboldt.examples.FlashSocketPolicy;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 *
 * @author Dennis Boldt
 *
 */
public class FlashSocketPolicyServer {
    public static void main(String[] args) {
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.FINE);
        Logger.getLogger("").addHandler(ch);
        Logger.getLogger("").setLevel(Level.FINE);

        // Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new FlashSocketPolicyPipelineFactory());

        // Bind and start to accept incoming connections.
        try {
        	bootstrap.bind(new InetSocketAddress(843));
        	System.out.println("Flash Socket Policy Server started on 843.");
		} catch (Exception e) {
        	bootstrap.bind(new InetSocketAddress(8080));
        	System.out.println("Flash Socket Policy Server started on 8080.");
		}
    }
}
