package com.shaun.SerialPortClient.web.rest;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.shaun.SerialPortClient.entity.IotInfo;
import com.shaun.SerialPortClient.repository.IotInfoRepository;
import com.shaun.SerialPortClient.service.ChannelCacheService;
import com.shaun.SerialPortClient.web.rest.vm.IotInfoRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iot-info")
public class IotInfoController {

    @Autowired
    private ChannelCacheService channelCacheService;

    @Autowired
    private IotInfoRepository iotInfoRepository;

    @GetMapping("/connected")
    public Object getConnected() {
        return channelCacheService.getCacheKeys();
    }

    @GetMapping()
    public Object get() {
        return iotInfoRepository.findAll();
    }

    @PostMapping()
    public Object post(@RequestBody IotInfoRequest requestBody) {

        IotInfo request = new IotInfo();
        request.setProtocal(requestBody.protocal);
        request.setIp(requestBody.ip);
        request.setEchoInterval(requestBody.echoInterval);
        request.setEchoHex(requestBody.echoHex);
        request.setDataFrameType(requestBody.dataFrameType);
        request.setFrameMaxLengh(requestBody.frameMaxLengh);
        request.setContentMap(requestBody.contentMap);

        if (!StringUtils.isEmpty(request.getIp())) {
            IotInfo search = new IotInfo();
            search.setIp(request.getIp());
            IotInfo originData = iotInfoRepository.findOne(Example.of(search)).orElse(new IotInfo());
            request.setId(originData.getId());

            if (originData.getCreateAt() == null) {
                request.setCreateAt(LocalDateTime.now());
            } else {
                request.setUpdateAt(LocalDateTime.now());
            }
            request.setStatus("");
        }
        IotInfo result = iotInfoRepository.save(request);

        Arrays.stream(channelCacheService.getCacheKeys().toArray()).forEach((e -> {
            if (e.toString().indexOf(":" + request.getIp() + ":") > 0) {
                channelCacheService.getCache(e.toString()).close();
            }
        }));

        return result;
    }

}