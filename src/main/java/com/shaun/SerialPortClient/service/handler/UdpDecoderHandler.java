package com.shaun.SerialPortClient.service.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class UdpDecoderHandler extends MessageToMessageDecoder<DatagramPacket> {
    private static final Logger log = LoggerFactory.getLogger(UdpDecoderHandler.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket byteBuf, List<Object> list)
            throws Exception {
        ByteBuf byteBuf1 = byteBuf.content();
        int size = byteBuf1.readableBytes();
        byte[] data = new byte[size];
        byteBuf1.readBytes(data);
        log.info(new String(data));
    }
}