package cn.cixinxc.chime.client.sender;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    // send message to server
    ctx.writeAndFlush("I'm new Client.");
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    // receive callback from server
    System.out.println("receive(" + ctx.channel().remoteAddress() + ") callback:" + msg);
  }
}