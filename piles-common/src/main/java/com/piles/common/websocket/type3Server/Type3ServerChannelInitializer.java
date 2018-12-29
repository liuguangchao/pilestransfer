package com.piles.common.websocket.type3Server;

import com.piles.common.websocket.decoder.StartAndLenthFieldFrameDecoder;
import com.piles.common.websocket.decoder.Type3StartAndLenthFieldFrameDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("type3ServerChannelInitializer")
public class Type3ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    Type3BaseChannelHandler type3BaseChannelHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//		pipeline.addLast("decoder", new ObjectDecoder(1024*1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
//		pipeline.addLast("encoder", new ObjectEncoder());
        //根据报文解决粘包和半包问题  1位首字码  一位命令码 2位流水号 2位长度
//		pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 4, 2, -4, 0));
//		pipeline.addLast("encoder", new ObjectEncoder());
//		pipeline.addLast("decoder", new ObjectDecoder(1024*1024, ClassResolvers.cacheDisabled(null)));
//		pipeline.addLast("encoder", new ObjectEncoder());

        pipeline.addLast("frameDecoder", new Type3StartAndLenthFieldFrameDecoder(0xAA, 0xF5));
        pipeline.addLast("decoder", new ByteArrayDecoder());
        pipeline.addLast("encoder", new ByteArrayEncoder());
        pipeline.addLast(new ReadTimeoutHandler(60 * 5));

        pipeline.addLast("handler", type3BaseChannelHandler);
    }

}
