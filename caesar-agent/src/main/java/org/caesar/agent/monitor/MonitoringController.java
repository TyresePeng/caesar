package org.caesar.agent.monitor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

    // 获取系统资源使用情况：CPU、内存、线程
    @GetMapping("/api/system-metrics")
    public SystemMonitoringService.SystemMetrics getSystemMetrics() {
        return SystemMonitoringService.getSystemMetrics();
    }

    // 获取类的内存信息
    @GetMapping("/api/class-memory-info")
    public String getClassMemoryInfo(@RequestParam String className) {
        try {
            return ClassMemoryInfoService.getClassMemoryInfo(className);
        } catch (ClassNotFoundException e) {
            return "Class not found: " + className;
        }
    }

    // 获取对象内存布局
    @GetMapping("/api/object-memory-layout")
    public String getObjectMemoryLayout(@RequestParam String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object object = clazz.newInstance();
            return ClassMemoryInfoService.getObjectMemoryLayout(object);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // 获取对象的内存图
    @GetMapping("/api/object-graph-layout")
    public String getObjectGraphLayout(@RequestParam String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object object = clazz.newInstance();
            return ClassMemoryInfoService.getObjectGraphLayout(object);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
