package com.shaun.SerialPortClient.service.server;

import java.time.Duration;

import com.shaun.SerialPortClient.service.handler.UdpDecoderHandler;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.netty.Connection;
import reactor.netty.udp.UdpServer;

@Component
public class BootUdpServer {

    public Connection runUdpServer() {
        return UdpServer.create().handle((in, out) -> {
            in.receive().asByteArray().subscribe();
            return Flux.never();
        }).port(8888).doOnBound(conn -> conn.addHandlerLast("decoder", new UdpDecoderHandler()))
                .bindNow(Duration.ofSeconds(30));

    }

}