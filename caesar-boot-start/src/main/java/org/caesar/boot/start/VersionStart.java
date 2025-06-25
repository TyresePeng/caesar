package org.caesar.boot.start;

import lombok.Builder;
import lombok.Getter;
import org.springframework.core.env.Environment;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Peng.GUO
 */
@Getter
@Builder
public final class VersionStart {

    static String  BANNER = "\n" +
            "   ___    _    ___  ___    _    ___ \n" +
            "  / __|  /_\\  | __|/ __|  /_\\  | _ \\\n" +
            " | (__  / _ \\ | _| \\__ \\ / _ \\ |   /\n" +
            "  \\___|/_/ \\_\\|___||___//_/ \\_\\|_|_\\\n" +
            "                                    \n";

    public static Version version = new Version();

    @Getter
    public static class Version {


        public void printBanner(Environment environment) {
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            List<String> banner = Arrays.asList(new String[]{BANNER,
                    String.format("local time：%tF %<tr", LocalDateTime.now()),
                    String.format("application name：%s",
                            environment.getProperty("spring.application.name")),
                    String.format("port number：%s", Optional.ofNullable(environment.getProperty("server.port")).orElse("8080")),
                    "init heap: " + getNetFileSizeDescription(heapMemoryUsage.getInit()),
                    "max heap: " + getNetFileSizeDescription(heapMemoryUsage.getMax()),
                    "use heap: " + getNetFileSizeDescription(heapMemoryUsage.getUsed()),
                    "init system: " + getCurrentSystem(),
                    "......................................."});
            banner.forEach(System.out::println);
        }


        /**
         * 获取当前系统名称
         *
         * @return {@link String}
         */
        public static String getCurrentSystem() {
            return System.getProperties().getProperty("os.name").toLowerCase();
        }

        /**
         * 字节数B转化为KB、MB、GB
         *
         * @param size 大小
         * @return 大小
         */
        public static String getNetFileSizeDescription(long size) {
            StringBuilder bytes = new StringBuilder();
            DecimalFormat format = new DecimalFormat("###.0");
            if (size >= 1024 * 1024 * 1024) {
                double i = (size / (1024.0 * 1024.0 * 1024.0));
                bytes.append(format.format(i)).append("GB");
            } else if (size >= 1024 * 1024) {
                double i = (size / (1024.0 * 1024.0));
                bytes.append(format.format(i)).append("MB");
            } else if (size >= 1024) {
                double i = (size / (1024.0));
                bytes.append(format.format(i)).append("KB");
            } else {
                if (size <= 0) {
                    bytes.append("0B");
                } else {
                    bytes.append((int) size).append("B");
                }
            }
            return bytes.toString();
        }
    }
}