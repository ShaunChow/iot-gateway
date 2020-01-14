package com.shaun.SerialPortClient.service.server;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Optional;

import com.shaun.SerialPortClient.config.properties.TcpServerProperties;
import com.shaun.SerialPortClient.entity.IotInfo;
import com.shaun.SerialPortClient.model.EnumTcpFrameStrategy;
import com.shaun.SerialPortClient.repository.IotInfoRepository;
import com.shaun.SerialPortClient.service.ChannelCacheService;
import com.shaun.SerialPortClient.service.handler.TcpDecoderHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

@Component
public class BootTcpServer {

    private static final Logger log = LoggerFactory.getLogger(BootTcpServer.class);

    private TcpServerProperties config;

    private ChannelCacheService channelCacheService;

    private IotInfoRepository iotInfoRepository;

    public BootTcpServer(TcpServerProperties config, IotInfoRepository iotInfoRepository,
            ChannelCacheService channelCacheService) {
        this.config = config;
        this.iotInfoRepository = iotInfoRepository;
        this.channelCacheService = channelCacheService;
    }

    public DisposableServer runTcpServer() {

        int tcpServerPort = config.getServer().getPort();
        log.info("Netty tcp server is listening on: " + tcpServerPort);

        return TcpServer.create().wiretap(true).handle((inbound, outbound) -> {
            inbound.receive().asByteArray().subscribe();
            return outbound.sendString(Mono.just("connect sucessfully..")).neverComplete();
        }).doOnConnection(this::registeredHandlerOnConnection).doOnUnbound(this::deregistHandlerOnUnbound)
                .port(tcpServerPort).bindNow();
    }

    private void registeredHandlerOnConnection(Connection conn) {

        InetSocketAddress remoteAddress = (InetSocketAddress) conn.channel().remoteAddress();
        log.info("tcp " + remoteAddress.toString() + " is connecting...");

        String key = "Strange:" + conn.channel().remoteAddress().toString().substring(1) + ":"
                + Thread.currentThread().getName();
        IotInfo currentIotInfo = new IotInfo();
        currentIotInfo.setProtocal("Strange");

        Optional<IotInfo> channalConfig = searchIotInfoByIp(remoteAddress.getAddress().getHostAddress());

        if (channalConfig.isPresent()) {
            currentIotInfo = channalConfig.get();

            if (null == currentIotInfo.getEchoInterval()) {
                currentIotInfo.setEchoInterval(0);
            }

            conn.addHandler(new IdleStateHandler(0, currentIotInfo.getEchoInterval(), 0));

            if (null == currentIotInfo.getDataFrameType()) {
                currentIotInfo.setDataFrameType("Strange");
            }
            String frameStrategyStr = currentIotInfo.getDataFrameType();

            if (null == currentIotInfo.getFrameMaxLengh()) {
                currentIotInfo.setFrameMaxLengh(1024);
            }
            int maxFrameLengh = currentIotInfo.getFrameMaxLengh();

            try {
                if (EnumTcpFrameStrategy.FIXED_LENGTH.getValue().equals(frameStrategyStr)) {
                    conn.addHandler(new FixedLengthFrameDecoder(
                            (int) currentIotInfo.getContentMap().getOrDefault("decodeFrameLength", 8)));
                } else if (EnumTcpFrameStrategy.DELIMITER.getValue().equals(frameStrategyStr)) {
                    conn.addHandler(new DelimiterBasedFrameDecoder(maxFrameLengh, Unpooled.buffer()
                            .writeBytes(currentIotInfo.getContentMap().get("delimiter").toString().getBytes())));
                } else if (EnumTcpFrameStrategy.LINE.getValue().equals(frameStrategyStr)) {
                    conn.addHandler(new LineBasedFrameDecoder(maxFrameLengh));
                } else if (EnumTcpFrameStrategy.LENGTH_FIELD.getValue().equals(frameStrategyStr)) {
                    int lengthFieldOffset = (int) currentIotInfo.getContentMap().get("lengthFieldOffset");
                    int lengthFieldLength = (int) currentIotInfo.getContentMap().get("lengthFieldLength");
                    int lengthAdjustment = (int) currentIotInfo.getContentMap().get("lengthAdjustment");
                    int initialBytesToStrip = (int) currentIotInfo.getContentMap().get("initialBytesToStrip");
                    conn.addHandler(new LengthFieldBasedFrameDecoder(maxFrameLengh, lengthFieldOffset,
                            lengthFieldLength, lengthAdjustment, initialBytesToStrip));
                }

                key = null == currentIotInfo.getProtocal()
                        ? "Strange:" + currentIotInfo.getIp() + ":" + remoteAddress.getPort() + ":"
                                + Thread.currentThread().getName()
                        : currentIotInfo.getProtocal() + ":" + currentIotInfo.getIp() + ":" + remoteAddress.getPort()
                                + ":" + Thread.currentThread().getName();
            } catch (Exception e) {
                log.info("config wrong: " + e.getMessage());
            }
        }

        if (currentIotInfo.getCreateAt() == null) {
            currentIotInfo.setCreateAt(LocalDateTime.now());
        } else {
            currentIotInfo.setUpdateAt(LocalDateTime.now());
        }
        currentIotInfo.setIp(remoteAddress.getAddress().getHostAddress());
        currentIotInfo.setStatus(key);
        currentIotInfo = iotInfoRepository.save(currentIotInfo);

        conn.addHandler(new TcpDecoderHandler(currentIotInfo, iotInfoRepository, channelCacheService));
        channelCacheService.cache(key, conn.channel());
        log.info("tcp: " + key + " is connected.");
    }

    private void deregistHandlerOnUnbound(DisposableServer server) {
        InetSocketAddress remoteAddress = (InetSocketAddress) server.channel().remoteAddress();
        log.info("tcp " + remoteAddress.toString() + " is disconnecting...");
        Optional<IotInfo> disConnectIot = searchIotInfoByIp(remoteAddress.getAddress().getHostAddress());

        if (disConnectIot.isPresent()) {
            IotInfo updated = disConnectIot.get();
            updated.setUpdateAt(LocalDateTime.now());
            updated.setStatus("");
            iotInfoRepository.save(updated);
        }

    }

    private Optional<IotInfo> searchIotInfoByIp(String ip) {
        IotInfo search = new IotInfo();
        search.setIp(ip);
        return iotInfoRepository.findOne(Example.of(search));
    }

}