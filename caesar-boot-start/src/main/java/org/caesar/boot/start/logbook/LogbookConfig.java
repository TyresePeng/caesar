package org.caesar.boot.start.logbook;

import org.caesar.boot.start.transmittable.ContextThreadLocal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;
import org.zalando.logbook.json.JsonHttpLogFormatter;


/**
 * LogbookConfig 请求日志记录
 * @author peng.guo
 */
@Configuration
public class LogbookConfig {
    @Bean
    public Logbook logbook() {
        //json 输出
        HttpLogFormatter formatter = new JsonHttpLogFormatter();
        Sink sink = new DefaultSink(formatter, new DefaultHttpLogWriter());
        //请求id correlationId
        //默认输出格式
        return Logbook.builder()
                .correlationId(request -> ContextThreadLocal.getTraceId())
                .strategy(new DefaultStrategy())
                .sink(sink)
                .build();

    }
}
