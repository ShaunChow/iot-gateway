package com.shaun.SerialPortClient.service.handler;

import com.shaun.SerialPortClient.util.HexStrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class FixedLengthFrameEncoder extends MessageToByteEncoder<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(FixedLengthFrameEncoder.class);

    private int length;

    public FixedLengthFrameEncoder(int length) {
        this.length = length;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf o, ByteBuf out) throws Exception {

        byte[] bytes = new byte[o.readableBytes()];
        o.duplicate().readBytes(bytes);

        if (bytes.length > length) {
            log.error("tcp exception,{}", "message length is too large, it's limited " + length);
            throw new UnsupportedOperationException("message length is too large, it's limited " + length);
        }

        if (bytes.length < length) {
            bytes = addSpace(bytes);
        }
        ctx.writeAndFlush(Unpooled.wrappedBuffer(bytes));
    }

    private byte[] addSpace(byte[] msg) {
        StringBuilder builder = new StringBuilder(HexStrUtil.bytesToHex(msg));
        for (int i = 0; i < length - msg.length; i++) {
            builder.append(" ");
        }
        return HexStrUtil.hexToBytes(builder.toString());
    }
}