package de.dennisboldt.examples.version;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 *
 * @author Dennis Boldt
 *
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ChannelFactory factory =
            new NioServerSocketChannelFactory(
                    Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool());

        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new PipelineFactory());

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

        bootstrap.bind(new InetSocketAddress(1234));
        System.out.println("Server started");
    }
}