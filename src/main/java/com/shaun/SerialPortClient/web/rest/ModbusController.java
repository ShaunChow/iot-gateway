package com.shaun.SerialPortClient.web.rest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.shaun.SerialPortClient.service.ChannelCacheService;
import com.shaun.SerialPortClient.service.J2modService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/modbus")
public class ModbusController {

    @Autowired
    private J2modService j2modService;

    @Autowired
    private ChannelCacheService channelCacheService;

    @GetMapping("/sse")
    public ResponseBodyEmitter get() {

        final SseEmitter emitter = new SseEmitter();
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(() -> {

            for (int i = 0; i < 500; i++) {

                try {
                    emitter.send(j2modService.result, MediaType.APPLICATION_JSON_UTF8);

                    Thread.sleep(3000);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    emitter.completeWithError(ex);
                    return;
                }
            }

            emitter.complete();
        });

        return emitter;
    }

    @GetMapping()
    public Object getCahce() {
        // channelCacheService.getCache("Modbus:10.252.31.144:26")
        // .writeAndFlush(Unpooled.buffer().writeBytes(new byte[] { (byte) 0x01, (byte)
        // 0x03, (byte) 0x00,
        // (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0xC4, (byte) 0x0B }));

        return channelCacheService.getCacheBusinessResult("Modbus:10.252.31.144:26", Map.class);
    }

}