package com.shaun.SerialPortClient.service.server;

import com.shaun.SerialPortClient.config.properties.TcpServerProperties;
import com.shaun.SerialPortClient.service.handler.ModbusDecoderHandler;

import org.springframework.stereotype.Component;
import io.netty.handler.timeout.IdleStateHandler;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

@Component
public class BootTcpServer {

    public BootTcpServer(TcpServerProperties config) {
        config.getClass();
    }

    public DisposableServer runTcpServer() {

        return TcpServer.create().wiretap(true).handle((inbound, outbound) -> {
            inbound.receive().asByteArray().subscribe();
            return outbound.sendString(Mono.just("connect sucessfully..")).neverComplete();
        }).doOnConnection(conn -> {
            conn.addHandler(new IdleStateHandler(5, 5, 0));
            conn.addHandler(new ModbusDecoderHandler());
        }).doOnUnbound(conn -> {
        }).port(26).bindNow();
    }

}