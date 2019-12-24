package com.shaun.SerialPortClient.eventbus;

import com.google.common.eventbus.EventBus;

public class TcpEventBus {

    private final static EventBus tiemEventBus = new EventBus();

    public static void post(Object event) {
        tiemEventBus.post(event);
    }

    public static void register(Object handler) {
        tiemEventBus.register(handler);
    }

    public static void unregister(Object handler) {
        tiemEventBus.unregister(handler);
    }
}
