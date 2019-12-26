package com.shaun.SerialPortClient.service.handler;

import java.net.InetSocketAddress;
import java.util.List;
import com.shaun.SerialPortClient.config.properties.TcpServerProperties.ContractClientsProperties;
import com.shaun.SerialPortClient.eventbus.TcpEventBus;
import com.shaun.SerialPortClient.eventbus.message.TcpMessage;
import com.shaun.SerialPortClient.util.HexStrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class TcpDecoderHandler extends MessageToMessageDecoder<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(TcpDecoderHandler.class);

    private ContractClientsProperties currentConfig;

    public TcpDecoderHandler(ContractClientsProperties contractClientsProperties) {
        this.currentConfig = contractClientsProperties;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf o, List<Object> list) {
        byte[] bytes = new byte[o.readableBytes()];
        o.duplicate().readBytes(bytes);
        InetSocketAddress x = (InetSocketAddress) ctx.channel().remoteAddress();

        log.info("recieved (" + x.getAddress().getHostAddress() + ":" + x.getPort() + ") client data : "
                + HexStrUtil.bytesToHex(bytes));

        String key = currentConfig.getProtocal() + ":" + currentConfig.getIp() + ":" + currentConfig.getPort();

        TcpEventBus.post(new TcpMessage(key, HexStrUtil.bytesToHex(bytes)));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String socketString = ctx.channel().remoteAddress().toString();

        log.info("EventTriggered at :" + socketString);

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE
                    && !StringUtils.isEmpty(currentConfig.getHeartbeat().getEchohex())) {
                ctx.writeAndFlush(
                        Unpooled.buffer().writeBytes(HexStrUtil.hexToBytes(currentConfig.getHeartbeat().getEchohex())));
            }
        }

        super.userEventTriggered(ctx, evt);
    }
}