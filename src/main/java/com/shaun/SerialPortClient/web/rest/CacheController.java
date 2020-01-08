package com.shaun.SerialPortClient.web.rest;

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
        return channelCacheService.getCacheBusinessResult(key, Map.class);
    }

}