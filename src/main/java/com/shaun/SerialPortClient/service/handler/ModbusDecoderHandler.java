package com.shaun.SerialPortClient.service.handler;

import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaun.SerialPortClient.util.HexStrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ModbusDecoderHandler extends MessageToMessageDecoder<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(ModbusDecoderHandler.class);

    private DecimalFormat twoPointReserve = new DecimalFormat("0.00");
    private DecimalFormat threePointReserve = new DecimalFormat("0.000");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf o, List<Object> list) {
        byte[] bytes = new byte[o.readableBytes()];
        o.duplicate().readBytes(bytes);
        InetSocketAddress x = (InetSocketAddress) ctx.channel().remoteAddress();

        log.info("recieved (" + x.getAddress().getHostAddress() + ":" + x.getPort() + ") client data : "
                + HexStrUtil.bytesToHex(bytes));

        String dataStr = HexStrUtil.bytesToHex(bytes).replace(" ", "").substring(6);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("value1", threePointReserve.format(Integer.parseInt(dataStr.substring(0, 4), 16) / (float) 1000));
        resultMap.put("value2", twoPointReserve.format(Integer.parseInt(dataStr.substring(4, 8), 16) / (float) 100));
        try {
            log.info("response (" + x.getAddress().getHostAddress() + ":" + x.getPort() + ") result >> "
                    + new ObjectMapper().writeValueAsString(resultMap));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String socketString = ctx.channel().remoteAddress().toString();

        log.info("EventTriggered at :" + socketString);

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(Unpooled.buffer().writeBytes(HexStrUtil.hexToBytes("010300000002C40B")));
            }
        }

        super.userEventTriggered(ctx, evt);
    }
}