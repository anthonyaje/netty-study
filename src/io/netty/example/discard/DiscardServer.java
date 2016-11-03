package io.netty.example.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/*
 * Discard any incoming data.
 */
public final class DiscardServer {
	static final int PORT = Integer.parseInt(System.getProperty("port","8009"));
	
	public static void main(String[] args) throws Exception {
		
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>(){
				@Override
				public void initChannel(SocketChannel ch){
					ChannelPipeline p = ch.pipeline();
					p.addLast(new DiscardServerHandler());
				}
			});
			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(PORT).sync();
			f.channel().closeFuture().sync();
		} finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}	
	}
}
