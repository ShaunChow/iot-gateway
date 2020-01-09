package com.shaun.SerialPortClient.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "iot_info")
public class IotInfo {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String protocal;

    public String getProtocal() {
        return this.protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    private String ip;

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private Integer echo_interval;

    public Integer getEchoInterval() {
        return this.echo_interval;
    }

    public void setEchoInterval(Integer echo_interval) {
        this.echo_interval = echo_interval;
    }

    private String echo_hex;

    public String getEchoHex() {
        return this.echo_hex;
    }

    public void setEchoHex(String echo_hex) {
        this.echo_hex = echo_hex;
    }

    private String data_frame_type;

    public String getDataFrameType() {
        return this.data_frame_type;
    }

    public void setDataFrameType(String data_frame_type) {
        this.data_frame_type = data_frame_type;
    }

    private Integer frame_max_lengh;

    public Integer getFrameMaxLengh() {
        return this.frame_max_lengh;
    }

    public void setFrameMaxLengh(Integer frame_max_lengh) {
        this.frame_max_lengh = frame_max_lengh;
    }

    @JsonIgnoreProperties
    @Column(columnDefinition = "varchar(5000)")
    private String content;

    public String getContent() {
        if (null == contentMap)
            return null;
        try {
            this.content = new ObjectMapper().writeValueAsString(contentMap);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private LocalDateTime create_at;

    public LocalDateTime getCreateAt() {
        return this.create_at;
    }

    public void setCreateAt(LocalDateTime create_at) {
        this.create_at = create_at;
    }

    private LocalDateTime update_at;

    public LocalDateTime getUpdateAt() {
        return this.update_at;
    }

    public void setUpdateAt(LocalDateTime update_at) {
        this.update_at = update_at;
    }

    // ignore column by jpa

    @Transient
    private Map<String, Object> contentMap;

    public Map<String, Object> getContentMap() {

        if (null == content) {
            return null;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = new ObjectMapper().readValue(content, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public void setContentMap(Map<String, Object> contentMap) {
        this.contentMap = contentMap;

        if (null == contentMap) {
            this.content = null;
        }
        try {
            this.content = new ObjectMapper().writeValueAsString(contentMap);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}