package de.dennisboldt.examples.FlashSocketPolicy;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 *
 * @author Dennis Boldt
 * @see http://www.lightsphere.com/dev/articles/flash_socket_policy.html
 *
 */
public class FlashSocketPolicyHandler extends SimpleChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		if(e.getMessage() instanceof ChannelBuffer) {
			/*
			 * Note:
			 * Flash requires all data from sockets to be ended with a null character
			 * 0x00 or \u0000
			 */

			// The request format
			String policyFileRequest = "<policy-file-request/>";
			ChannelBuffer requestBuffer = new DynamicChannelBuffer(23);
			requestBuffer.writeBytes(policyFileRequest.getBytes());
			requestBuffer.writeByte(0x00);

			// The response format
			StringBuffer policyFileResponse = new StringBuffer();
			policyFileResponse.append("<cross-domain-policy>");
			policyFileResponse.append("<allow-access-from domain=\"*\" to-ports=\"*\" />");
			policyFileResponse.append("</cross-domain-policy>");

			ChannelBuffer cb = (ChannelBuffer) e.getMessage();
			if(requestBuffer.equals(cb)) {
				System.out.println("Response: " + policyFileResponse.toString());
				ChannelBuffer responseBuffer = new DynamicChannelBuffer(policyFileResponse.capacity() + 1);
				responseBuffer.writeBytes(policyFileResponse.toString().getBytes());
				responseBuffer.writeByte(0x00);
				Channels.write(ctx, Channels.future(ctx.getChannel()), responseBuffer);
			}
		} else {
			super.messageReceived(ctx, e);
		}
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		super.writeRequested(ctx, e);
	}
}
