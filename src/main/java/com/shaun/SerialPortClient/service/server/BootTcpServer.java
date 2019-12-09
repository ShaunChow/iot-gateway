package com.shaun.SerialPortClient.service.server;

import com.shaun.SerialPortClient.service.handler.ModbusDecoderHandler;

import org.springframework.stereotype.Component;

import io.netty.handler.timeout.IdleStateHandler;
import reactor.core.publisher.Flux;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

@Component
public class BootTcpServer {

    public DisposableServer runTcpServer() {
        return TcpServer.create().wiretap(true).handle((inbound, outbound) -> {
            inbound.receive().asByteArray().subscribe();
            return outbound.sendByteArray(Flux.just(new byte[] { (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x02, (byte) 0xC4, (byte) 0x0B })).neverComplete();
        }).doOnConnection(conn -> {
            conn.addHandler(new IdleStateHandler(5, 5, 0));
            conn.addHandler(new ModbusDecoderHandler());
        }).port(26).bindNow();
    }

}