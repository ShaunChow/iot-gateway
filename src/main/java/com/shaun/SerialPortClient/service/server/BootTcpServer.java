package com.shaun.SerialPortClient.service.server;

import java.util.Optional;

import com.shaun.SerialPortClient.config.properties.TcpServerProperties;
import com.shaun.SerialPortClient.config.properties.TcpServerProperties.ContractClientsProperties;
import com.shaun.SerialPortClient.service.ChannelCacheService;
import com.shaun.SerialPortClient.service.handler.TcpDecoderHandler;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import io.netty.handler.timeout.IdleStateHandler;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

@Component
public class BootTcpServer {

    private TcpServerProperties config;

    private ChannelCacheService channelCacheService;

    public BootTcpServer(TcpServerProperties config, CacheManager cacheManager,
            ChannelCacheService channelCacheService) {
        this.config = config;
        this.channelCacheService = channelCacheService;
    }

    public DisposableServer runTcpServer() {

        return TcpServer.create().wiretap(true).handle((inbound, outbound) -> {
            inbound.receive().asByteArray().subscribe();
            return outbound.sendString(Mono.just("connect sucessfully..")).neverComplete();
        }).doOnConnection(this::registeredHandlerOnConnection).doOnUnbound(this::deregistHandlerOnUnbound)
                .port(config.getServer().getPort()).bindNow();
    }

    public void registeredHandlerOnConnection(Connection conn) {

        Optional<ContractClientsProperties> channalConfig = config.getContractclients().stream().filter(e -> {
            if (("/" + e.getIp() + ":" + e.getPort()).equals(conn.channel().remoteAddress().toString()))
                return true;
            return conn.channel().remoteAddress().toString().indexOf(e.getIp()) > 0;
        }).findFirst();

        if (channalConfig.isPresent()) {

            String key = channalConfig.get().getProtocal() + ":" + channalConfig.get().getIp() + ":"
                    + channalConfig.get().getPort();

            channelCacheService.cache(key, conn.channel());

            conn.addHandler(new IdleStateHandler(0, channalConfig.get().getHeartbeat().getInterval().intValue(), 0));

            TcpDecoderHandler temp = new TcpDecoderHandler(channalConfig.get());
            conn.addHandler(temp);
        }
    }

    public void deregistHandlerOnUnbound(DisposableServer server) {

    }

}