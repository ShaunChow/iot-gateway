package com.shaun.SerialPortClient.service.server;

import java.util.Optional;

import com.shaun.SerialPortClient.config.properties.TcpServerProperties;
import com.shaun.SerialPortClient.config.properties.TcpServerProperties.ContractClientsProperties;
import com.shaun.SerialPortClient.model.EnumTcpFrameStrategy;
import com.shaun.SerialPortClient.service.ChannelCacheService;
import com.shaun.SerialPortClient.service.handler.FixedLengthFrameEncoder;
import com.shaun.SerialPortClient.service.handler.TcpDecoderHandler;
import com.shaun.SerialPortClient.util.HexStrUtil;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
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

            String frameStrategyStr = channalConfig.get().getFrame();

            int maxFrameLengh = channalConfig.get().getFrameMaxLengh();

            if (EnumTcpFrameStrategy.FIXED_LENGTH.getValue().equals(frameStrategyStr)) {

                conn.addHandler(
                        new FixedLengthFrameDecoder(channalConfig.get().getFixedLength().getDecodeframelengh()));
                conn.addHandler(new TcpDecoderHandler(channalConfig.get()));

                Integer encodeLengh = channalConfig.get().getFixedLength().getEncodeframelengh();

                if (null != encodeLengh && 0 < encodeLengh) {
                    conn.addHandler(new FixedLengthFrameEncoder(encodeLengh));
                }
                return;
            }

            if (EnumTcpFrameStrategy.DELIMITER.getValue().equals(frameStrategyStr)) {
                conn.addHandler(new DelimiterBasedFrameDecoder(maxFrameLengh,
                        Unpooled.buffer().writeBytes(HexStrUtil.hexToBytes("$"))));
                conn.addHandler(new TcpDecoderHandler(channalConfig.get()));
                return;
            }

            if (EnumTcpFrameStrategy.LINE.getValue().equals(frameStrategyStr)) {
                conn.addHandler(new LineBasedFrameDecoder(maxFrameLengh));
                conn.addHandler(new TcpDecoderHandler(channalConfig.get()));
                return;
            }

            if (EnumTcpFrameStrategy.LENGTH_FIELD.getValue().equals(frameStrategyStr)) {
                conn.addHandler(new LengthFieldBasedFrameDecoder(maxFrameLengh, 0, 2, 0, 2));
                conn.addHandler(new LengthFieldPrepender(2));
                return;
            }

            return;
        }
    }

    public void deregistHandlerOnUnbound(DisposableServer server) {

    }

}