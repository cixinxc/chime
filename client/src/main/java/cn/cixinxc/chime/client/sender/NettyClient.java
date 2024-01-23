package cn.cixinxc.chime.client.sender;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

public class NettyClient {

  private static final EventLoopGroup EVENT_LOOP_GROUP = new NioEventLoopGroup();
  private static final Bootstrap BOOT_STRAP = new Bootstrap();

  public NettyClient() {
    BOOT_STRAP.group(EVENT_LOOP_GROUP)
            .channel(NioSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.ERROR))
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .handler(new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                pipeline.addLast(new ClientHandler());
              }
            });
    try {
      ChannelFuture channelFuture = BOOT_STRAP.connect("192.168.1.5", 8888).sync();
      channelFuture.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
