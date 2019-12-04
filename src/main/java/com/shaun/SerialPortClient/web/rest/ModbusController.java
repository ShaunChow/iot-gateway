package com.shaun.SerialPortClient.web.rest;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.shaun.SerialPortClient.service.J2modService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse/modbus")
public class ModbusController {

    @Autowired

    private J2modService j2modService;

    @GetMapping()
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

}