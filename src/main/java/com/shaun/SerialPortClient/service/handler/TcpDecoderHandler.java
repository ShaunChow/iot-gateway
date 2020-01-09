package com.shaun.SerialPortClient.service.handler;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.shaun.SerialPortClient.entity.IotInfo;
import com.shaun.SerialPortClient.eventbus.TcpEventBus;
import com.shaun.SerialPortClient.eventbus.message.TcpMessage;
import com.shaun.SerialPortClient.repository.IotInfoRepository;
import com.shaun.SerialPortClient.service.ChannelCacheService;
import com.shaun.SerialPortClient.util.HexStrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class TcpDecoderHandler extends MessageToMessageDecoder<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(TcpDecoderHandler.class);

    private IotInfo currentConfig;

    private IotInfoRepository iotInfoRepository;

    private ChannelCacheService channelCacheService;

    public TcpDecoderHandler(IotInfo contractClientsProperties, IotInfoRepository iotInfoRepository,
            ChannelCacheService channelCacheService) {
        this.currentConfig = contractClientsProperties;
        this.iotInfoRepository = iotInfoRepository;
        this.channelCacheService = channelCacheService;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf o, List<Object> list) {
        byte[] bytes = new byte[o.readableBytes()];
        o.duplicate().readBytes(bytes);
        InetSocketAddress x = (InetSocketAddress) ctx.channel().remoteAddress();

        log.info("recieved (" + x.getAddress().getHostAddress() + ":" + x.getPort() + ") client data : "
                + HexStrUtil.bytesToHex(bytes));

        String key = currentConfig.getStatus();

        TcpEventBus.post(new TcpMessage(key, HexStrUtil.bytesToHex(bytes)));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String socketString = ctx.channel().remoteAddress().toString();

        log.debug("EventTriggered at :" + socketString);

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE && !StringUtils.isEmpty(currentConfig.getEchoHex())) {
                ctx.writeAndFlush(Unpooled.buffer().writeBytes(HexStrUtil.hexToBytes(currentConfig.getEchoHex())));
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info("tcp " + remoteAddress.toString() + " is inactive...");
        Optional<IotInfo> disConnectIot = searchIotInfoByIp(remoteAddress.getAddress().getHostAddress());

        if (disConnectIot.isPresent()) {
            IotInfo updated = disConnectIot.get();
            updated.setUpdateAt(LocalDateTime.now());
            updated.setStatus("");
            iotInfoRepository.save(updated);
        }

        channelCacheService.evictAllCacheByIp(remoteAddress.getAddress().getHostAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("tcp exception,{}", cause);
        ctx.close();
    }

    private Optional<IotInfo> searchIotInfoByIp(String ip) {
        IotInfo search = new IotInfo();
        search.setIp(ip);
        return iotInfoRepository.findOne(Example.of(search));
    }

}