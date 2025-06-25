
package org.caesar.boot.start.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.caesar.boot.start.VersionStart;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;


/**
 * @author Peng.GUO
 */
@Slf4j
@RequiredArgsConstructor
public class CaesarRunner implements ApplicationRunner {
    private final ConfigurableApplicationContext context;
    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {
            VersionStart.version.printBanner(environment);
        }
    }
}
