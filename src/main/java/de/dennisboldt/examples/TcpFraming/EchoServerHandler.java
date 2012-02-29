package de.dennisboldt.examples.TcpFraming;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class EchoServerHandler extends SimpleChannelHandler {

	protected Channel outboundChannel = null;

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {

		// Connect to the logging server
		InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), 9090);

		ClientSocketChannelFactory clientFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		ClientBootstrap outboundClientBootstrap = new ClientBootstrap(clientFactory);
		outboundClientBootstrap.setOption("connectTimeoutMillis", "30000");
		outboundClientBootstrap.getPipeline().addLast("handler", new DiscardServerHandler());
		ChannelFuture outboundClientFuture = outboundClientBootstrap.connect(address);

		outboundClientFuture.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture outboundClientFuture)
					throws Exception {
				if (outboundClientFuture.isSuccess()) {
					outboundChannel = outboundClientFuture.getChannel();
				}
			}
	      });
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

		// Echo the message
		Channel ch = e.getChannel();
		ch.write(e.getMessage());

		// Forward the message to the logging
		outboundChannel.write(e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();

		Channel ch = e.getChannel();
		ch.close();
	}
}