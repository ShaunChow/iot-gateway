package com.shaun.SerialPortClient.repository;

import com.shaun.SerialPortClient.entity.IotRecord;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IotRecordRepository extends JpaRepository<IotRecord, Long> {

}