package cn.cixinxc.chime.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class NettyServer {

  public static final int PORT = 8888;

  public void start() {
    String host;

    try {
      host = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      return;
    }

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(8);
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childOption(ChannelOption.TCP_NODELAY, true)
              .childOption(ChannelOption.SO_KEEPALIVE, true)
              .option(ChannelOption.SO_BACKLOG, 1024)
              .handler(new LoggingHandler(LogLevel.ERROR))
              .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                  ChannelPipeline pipeline = ch.pipeline();
                  pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                  pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                  pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                  pipeline.addLast(new ServerHandler());
                }
              });

      ChannelFuture channelFuture = bootstrap.bind(host, PORT).sync();
      channelFuture.channel().closeFuture().sync();
    } catch (InterruptedException ignored) {
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
      serviceHandlerGroup.shutdownGracefully();
    }
  }

}
