package com.shaun.SerialPortClient.eventbus;

import com.google.common.eventbus.EventBus;

public class TcpEventBus {

    public final static EventBus SINGLETON = new EventBus();

    public static void post(Object event) {
        SINGLETON.post(event);
    }

    public static void register(Object handler) {
        SINGLETON.register(handler);
    }

    public static void unregister(Object handler) {
        SINGLETON.unregister(handler);
    }
}
