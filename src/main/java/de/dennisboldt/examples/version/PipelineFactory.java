package de.dennisboldt.examples.version;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 *
 * @author Dennis Boldt
 *
 */
public class PipelineFactory implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("version", new VersionHandler());
		pipeline.addLast("handler", new ServerHandler());
		return pipeline;
	}
}
