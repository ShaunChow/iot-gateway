package com.shaun.SerialPortClient.web.rest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.shaun.SerialPortClient.service.ChannelCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business-result")
public class CacheController {

    @Autowired
    private ChannelCacheService channelCacheService;

    @GetMapping()
    public Object getCahce(@RequestParam String key) {

        List<Object> result = new LinkedList<>();

        Arrays.stream(channelCacheService.getCacheBusinessResultKeys().toArray()).forEach((e -> {
            if (e.toString().indexOf(":" + key + ":") > 0) {
                result.add(channelCacheService.getCacheBusinessResult(e.toString(), Map.class));
            }
        }));

        return result;
    }

}