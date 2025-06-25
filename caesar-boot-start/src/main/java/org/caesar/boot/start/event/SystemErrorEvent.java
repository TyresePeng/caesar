package org.caesar.boot.start.event;

import org.springframework.context.ApplicationEvent;


/**
 * 系统异常监听器
 * @author Peng.GUO
 */
public class  SystemErrorEvent extends ApplicationEvent {

    public SystemErrorEvent(Object source) {
        super(source);
    }
}
