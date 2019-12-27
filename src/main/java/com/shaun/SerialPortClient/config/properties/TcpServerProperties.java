package com.shaun.SerialPortClient.config.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "tcp")
public class TcpServerProperties implements Serializable {

    private static final long serialVersionUID = -1186314202499099206L;

    private ServerProperties server;

    public ServerProperties getServer() {
        return server;
    }

    public void setServer(ServerProperties server) {
        this.server = server;
    }

    private List<ContractClientsProperties> contractclients = new ArrayList<>();

    public List<ContractClientsProperties> getContractclients() {
        return contractclients;
    }

    public void setContractclients(List<ContractClientsProperties> contractclients) {
        this.contractclients = contractclients;
    }

    public static class ServerProperties {

        private Integer port;

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }

    public static class ContractClientsProperties {

        private String ip;

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getIp() {
            return ip;
        }

        private Integer port;

        public void setPort(Integer port) {
            this.port = port;
        }

        public Integer getPort() {
            return port;
        }

        private String protocal;

        public void setProtocal(String protocal) {
            this.protocal = protocal;
        }

        public String getProtocal() {
            return protocal;
        }

        private HeartbeatProperties heartbeat = new HeartbeatProperties();

        public void setHeartbeat(HeartbeatProperties heartbeat) {
            this.heartbeat = heartbeat;
        }

        public HeartbeatProperties getHeartbeat() {
            return heartbeat;
        }

        private String frame;

        public void setFrame(String frame) {
            this.frame = frame;
        }

        public String getFrame() {
            return frame;
        }

        private Integer frameMaxLengh = 1024;

        public void setFrameMaxLengh(Integer frameMaxLengh) {
            this.frameMaxLengh = frameMaxLengh;
        }

        public Integer getFrameMaxLengh() {
            return frameMaxLengh;
        }

        private FixedLength fixedLength;

        public void setFixedLength(FixedLength fixedlength) {
            this.fixedLength = fixedlength;
        }

        public FixedLength getFixedLength() {
            return fixedLength;
        }

    }

    public static class HeartbeatProperties {

        private Long interval = 0L;

        public void setInterval(Long interval) {
            this.interval = interval;
        }

        public Long getInterval() {
            return interval;
        }

        private String echohex;

        public void setEchohex(String echohex) {
            this.echohex = echohex;
        }

        public String getEchohex() {
            return echohex;
        }

    }

    public static class FixedLength {

        private Integer decodelengh;

        public void setDecodeframelengh(Integer decode) {
            this.decodelengh = decode;
        }

        public Integer getDecodeframelengh() {
            return decodelengh;
        }

        private Integer encodelengh;

        public void setEncodeframelengh(Integer encode) {
            this.encodelengh = encode;
        }

        public Integer getEncodeframelengh() {
            return encodelengh;
        }

    }
}