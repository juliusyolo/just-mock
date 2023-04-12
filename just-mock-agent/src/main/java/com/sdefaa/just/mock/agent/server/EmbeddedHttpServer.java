package com.sdefaa.just.mock.agent.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * @author Julius Wong
 * <p>
 * 内嵌Http服务
 * <p>
 * @since 1.0.0
 */
public class EmbeddedHttpServer {
    private static final Logger logger = Logger.getLogger(EmbeddedHttpServer.class.getName());
    private int port;
    private AtomicBoolean isStopped = new AtomicBoolean(false);
    private NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    public EmbeddedHttpServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        logger.info("start embedded http server");
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new EmbeddedServerInitializer());
        ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
        channelFuture.channel().closeFuture().sync();
    }

    public void stop() {
        if (isStopped.compareAndSet(false,true)){
          logger.info("stop embedded http server");
          bossGroup.shutdownGracefully();
          workerGroup.shutdownGracefully();
        }
    }

}
