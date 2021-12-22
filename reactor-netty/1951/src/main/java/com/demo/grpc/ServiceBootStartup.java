package com.demo.grpc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.util.concurrent.ThreadFactory;

public class ServiceBootStartup {
    public static void main(String[] args) throws InterruptedException, IOException {
        ThreadFactory bossThreadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("CloudNativeGrpcBoss_%d").build();
        ThreadFactory workerThreadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("CloudNativeGrpcWorker_%d").build();
        final Class<? extends ServerChannel> channelType = NioServerSocketChannel.class;
        final EventLoopGroup boss = new NioEventLoopGroup(2,bossThreadFactory);
        final EventLoopGroup worker = new NioEventLoopGroup(2,workerThreadFactory);
        Server server = NettyServerBuilder.forPort(8081)
                .channelType(channelType)
                .bossEventLoopGroup(boss)
                .workerEventLoopGroup(worker)
                .addService(new DemoServiceImpl()).build().start();
        System.out.println("grcp server start finish");
        server.awaitTermination();
    }
}
