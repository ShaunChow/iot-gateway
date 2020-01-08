package com.shaun.SerialPortClient.repository;

import com.shaun.SerialPortClient.entity.IotInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotInfoRepository extends JpaRepository<IotInfo, Long> {

}