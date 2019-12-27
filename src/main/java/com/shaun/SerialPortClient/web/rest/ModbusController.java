package com.shaun.SerialPortClient.web.rest;

import java.util.Map;

import com.shaun.SerialPortClient.service.ChannelCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modbus")
public class ModbusController {

    @Autowired
    private ChannelCacheService channelCacheService;

    @GetMapping()
    public Object getCahce() {
        // channelCacheService.getCache("Modbus:10.252.31.144:26")
        // .writeAndFlush(Unpooled.buffer().writeBytes(new byte[] { (byte) 0x01, (byte)
        // 0x03, (byte) 0x00,
        // (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0xC4, (byte) 0x0B }));

        return channelCacheService.getCacheBusinessResult("Modbus:10.252.31.144:26", Map.class);
    }

}