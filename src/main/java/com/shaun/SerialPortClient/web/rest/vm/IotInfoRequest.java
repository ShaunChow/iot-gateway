package com.shaun.SerialPortClient.web.rest.vm;

import java.util.Map;

public class IotInfoRequest {

    public String protocal;

    public String ip;

    public Integer echoInterval;

    public String echoHex;

    public String dataFrameType;

    public Integer frameMaxLengh;

    public Map<String, Object> contentMap;

}