package com.shaun.SerialPortClient.service.handler;

import java.net.InetSocketAddress;
import java.util.List;
import com.shaun.SerialPortClient.entity.IotInfo;
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

    private IotInfo currentConfig;

    public TcpDecoderHandler(IotInfo contractClientsProperties) {
        this.currentConfig = contractClientsProperties;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf o, List<Object> list) {
        byte[] bytes = new byte[o.readableBytes()];
        o.duplicate().readBytes(bytes);
        InetSocketAddress x = (InetSocketAddress) ctx.channel().remoteAddress();

        log.info("recieved (" + x.getAddress().getHostAddress() + ":" + x.getPort() + ") client data : "
                + HexStrUtil.bytesToHex(bytes));

        String key = currentConfig.getProtocal() + ":" + x.getAddress().getHostAddress() + ":" + x.getPort();

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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        log.error("tcp exception,{}", cause.getMessage());
        ctx.close();
    }

}