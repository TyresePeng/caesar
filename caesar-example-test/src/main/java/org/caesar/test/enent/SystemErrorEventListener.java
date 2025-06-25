package org.caesar.test.enent;

import org.caesar.boot.start.event.SystemErrorEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SystemErrorEventListener implements ApplicationListener<SystemErrorEvent> {

    @Override
    public void onApplicationEvent(SystemErrorEvent systemErrorEvent) {
        System.err.println("系统异常被监听到了，开始发布预警");
        System.err.println(systemErrorEvent.getSource());
        System.err.println("系统异常被监听到了，开始发布预警");
    }
}
