package com.shaun.SerialPortClient.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name = "iot_record")
public class IotRecord {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String category;

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String source;

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(length = 5000)
    private String body;

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private LocalDateTime create_at = LocalDateTime.now();

    public LocalDateTime getCreateAt() {
        return this.create_at;
    }

    public void setCreateAt(LocalDateTime create_at) {
        this.create_at = create_at;
    }

}