package com.shaun.SerialPortClient.service.handler;

import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private DecimalFormat twoPointReserve = new DecimalFormat("0.00");
    private DecimalFormat threePointReserve = new DecimalFormat("0.000");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf o, List<Object> list) {
        byte[] bytes = new byte[o.readableBytes()];
        o.duplicate().readBytes(bytes);
        InetSocketAddress x = (InetSocketAddress) ctx.channel().remoteAddress();

        log.info("recieved (" + x.getAddress().getHostAddress() + ":" + x.getPort() + ") client data : "
                + bytesToHex(bytes));

        String dataStr = bytesToHex(bytes).replace(" ", "").substring(6);
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
                ByteBuf buf = Unpooled.buffer(16);
                ctx.writeAndFlush(buf.writeBytes(new byte[] { (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x02, (byte) 0xC4, (byte) 0x0B }));
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}