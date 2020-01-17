package com.shaun.SerialPortClient.eventbus.subscribe;

import com.google.common.eventbus.Subscribe;
import com.shaun.SerialPortClient.eventbus.message.TcpMessage;

public interface TcpListening {
    @Subscribe
    void on(TcpMessage message) throws Exception;
}