package com.shaun.SerialPortClient.eventbus.subscribe;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadEventListener {

    private static final Logger log = LoggerFactory.getLogger(DeadEventListener.class);

    @Subscribe
    public void handle(DeadEvent e) {
        log.info("DeadEvent source-> " + e.getSource() + "  event-> " + e.getEvent());
    }

}