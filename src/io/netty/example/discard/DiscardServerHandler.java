package io.netty.example.discard;

import sun.rmi.runtime.Log;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)
	private final static Logger LOGGER = Logger.getLogger(DiscardServerHandler.class.getName());
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { 
        // Discard the received data silently.
    	ByteBuf in = (ByteBuf) msg;

    	String rep = "";
    	
    	try{
    		while (in.isReadable()){
    			rep += (char) in.readByte();
    		}
    		System.out.print(rep);
			System.out.flush();
        } finally{
    		in.release();
    	}
        
		LOGGER.info("for loop: \n");
    	for(int i=0; i<in.capacity(); i++){
    		System.out.print((char) in.getByte(i));
    	}
    	
        ByteBuf out = ctx.alloc().buffer(rep.length()*2);
        out.writeBytes(rep.getBytes());
         
        LOGGER.info("Incoming message. ACK was send");
        ctx.write(out); 
        ctx.flush();
        
    	
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}