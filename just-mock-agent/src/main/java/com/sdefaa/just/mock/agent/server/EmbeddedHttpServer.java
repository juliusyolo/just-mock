package com.sdefaa.just.mock.agent.server;

import com.sdefaa.just.mock.agent.MockAgentMain;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Logger;

/**
 * @author Julius Wong
 * <p>
 * 内嵌Http服务
 * <p>
 * @since 1.0.0
 */
public class EmbeddedHttpServer{
  private static final Logger logger = Logger.getLogger(EmbeddedHttpServer.class.getName());
  NioEventLoopGroup bossGroup = new NioEventLoopGroup();
  NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    public void start() throws InterruptedException {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new EmbeddedServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
            logger.info("start embedded http server");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop(){
     bossGroup.shutdownGracefully();
     workerGroup.shutdownGracefully();
    }

}
