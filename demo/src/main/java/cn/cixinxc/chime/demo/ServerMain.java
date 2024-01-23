package cn.cixinxc.chime.demo;

import cn.cixinxc.chime.server.NettyServer;

public class ServerMain {
  public static void main(String[] args) {
    NettyServer server = new NettyServer();
    server.start();
  }
}