package de.dennisboldt.examples.version;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 *
 * @author Dennis Boldt
 *
 */
public class VersionHandler extends SimpleChannelHandler {

	private String version = "v123.45a\n";

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		ChannelBuffer buffer = ChannelBuffers.buffer(version.length());
		buffer.writeBytes(this.version.getBytes());
		ctx.getChannel().write(buffer);
	    ctx.getPipeline().remove(VersionHandler.class);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();

		Channel ch = e.getChannel();
		ch.close();
	}
}