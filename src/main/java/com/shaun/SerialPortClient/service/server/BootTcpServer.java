package com.shaun.SerialPortClient.service.server;

import java.util.Optional;

import com.shaun.SerialPortClient.config.properties.TcpServerProperties;
import com.shaun.SerialPortClient.config.properties.TcpServerProperties.ContractClientsProperties;
import com.shaun.SerialPortClient.service.handler.ModbusDecoderHandler;
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

    private CacheManager cacheManager;

    public BootTcpServer(TcpServerProperties config, CacheManager cacheManager) {
        this.config = config;
        this.cacheManager = cacheManager;
    }

    public DisposableServer runTcpServer() {

        return TcpServer.create().wiretap(true).handle((inbound, outbound) -> {
            inbound.receive().asByteArray().subscribe();
            return outbound.sendString(Mono.just("connect sucessfully..")).neverComplete();
        }).doOnConnection(this::registeredHandlerOnConnection).doOnUnbound(this::deregistHandlerOnUnbound)
                .port(config.getServer().getPort()).bindNow();
    }

    public void registeredHandlerOnConnection(Connection conn) {

        Optional<ContractClientsProperties> channalConfig = config.getContractclients().stream()
                .filter(e -> ("/" + e.getIp() + ":" + e.getPort()).equals(conn.channel().remoteAddress().toString()))
                .findFirst();

        if (channalConfig.isPresent()) {

            String key = channalConfig.get().getProtocal() + ":" + channalConfig.get().getIp() + ":"
                    + channalConfig.get().getPort();

            cacheManager.getCache("tcp_connection").put(key, conn.channel());

            conn.addHandler(new IdleStateHandler(0, channalConfig.get().getHeartbeat().getInterval().intValue(), 0));

            if ("Modbus".equals(channalConfig.get().getProtocal())) {

                ModbusDecoderHandler temp = new ModbusDecoderHandler();
                conn.addHandler(temp);
            }
        }
    }

    public void deregistHandlerOnUnbound(DisposableServer server) {

    }

}