package com.demo.gateway;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledGlobalFilter;
import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Gateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(Gateway.class);

    public static void main(String[] args) {
        ArrayList<String> tmp= new ArrayList<>(Arrays.asList(args));
        tmp.add("--server.shutdown=graceful");
        String[] strArray = tmp.toArray(new String[0]);
        SpringApplication.run(Gateway.class, strArray);
    }




    private EventLoopGroup serverEventLoopGroup;

    private EventLoopGroup clientEventLoopGroup;

    private BlockingQueue<Runnable> clientQueue;

    private BlockingQueue<Runnable> serverQueue;

    @Bean("eventLoopInit")
    public Object eventLoopInit(NettyTcpConfig nettyTcpConfig) {
        serverQueue = new LinkedBlockingQueue<Runnable>(nettyTcpConfig.getServerQueueCount());
        ExecutorService serverExecutor = new ThreadPoolExecutor(
                nettyTcpConfig.getServerThreadCount(),
                nettyTcpConfig.getServerThreadCount(),
                1000 * 60,
                TimeUnit.MILLISECONDS,
                serverQueue,
                new ThreadFactoryImpl("keeper-server-"));
        if (LoopResources.hasNativeSupport()) {
            serverEventLoopGroup = new EpollEventLoopGroup(Math.max(Runtime.getRuntime().availableProcessors(), nettyTcpConfig.getServerThreadCount()), serverExecutor);
        } else {
            serverEventLoopGroup = new NioEventLoopGroup(Math.max(Runtime.getRuntime().availableProcessors(), nettyTcpConfig.getServerThreadCount()), serverExecutor);
        }

        clientQueue = new LinkedBlockingQueue<Runnable>(nettyTcpConfig.getClientQueueCount());
        ExecutorService clientExecutor = new ThreadPoolExecutor(//
                Math.max(Runtime.getRuntime().availableProcessors(), nettyTcpConfig.getClientThreadCount()),
                Math.max(Runtime.getRuntime().availableProcessors(), nettyTcpConfig.getClientThreadCount()),
                1000 * 60,
                TimeUnit.MILLISECONDS,
                clientQueue,
                new ThreadFactoryImpl("keeper-client-"));
        if (LoopResources.hasNativeSupport()) {
            clientEventLoopGroup = new EpollEventLoopGroup(Math.max(Runtime.getRuntime().availableProcessors(), nettyTcpConfig.getClientThreadCount()), clientExecutor);
        } else {
            clientEventLoopGroup = new NioEventLoopGroup(Math.max(Runtime.getRuntime().availableProcessors(), nettyTcpConfig.getClientThreadCount()), clientExecutor);
        }
        return null;
    }

    @Bean
    @DependsOn(value = {"eventLoopInit"})
    public Object waterMark() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl(
                "waterMark-"));
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.debug(String.format("{clientQueueSize:%s,ServerQueueSize:%s}", clientQueue.size(), serverQueue.size()));
                } catch (Throwable e) {
                    LOGGER.error("printWaterMark error.", e);
                }
            }
        }, 10, 2, TimeUnit.SECONDS);
        return null;
    }

    @Bean
    @DependsOn(value = {"eventLoopInit"})
    public HttpClientCustomizer httpClientCustomizer() {
        return httpClient -> httpClient.runOn(LoopResources.colocate(clientEventLoopGroup)).protocol(HttpProtocol.H2C).noSSL();
    }

    @Bean
    @DependsOn(value = {"eventLoopInit"})
    public NettyServerCustomizer nettyServerCustomizer() {
        return httpServer -> httpServer.runOn(LoopResources.colocate(serverEventLoopGroup));
    }

    @Bean
    @ConditionalOnEnabledGlobalFilter
    public NettyRoutingFilter nettyRoutingFilter(HttpClient httpClient,
                                                 ObjectProvider<List<HttpHeadersFilter>> headersFilters, HttpClientProperties properties) {
        return new NettyRoutingFilter(httpClient, headersFilters, properties);
    }
}
