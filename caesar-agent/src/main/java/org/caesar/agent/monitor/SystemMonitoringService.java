package org.caesar.agent.monitor;

import java.lang.management.*;
import com.sun.management.OperatingSystemMXBean;
import java.util.List;
import java.util.ArrayList;

public class SystemMonitoringService {

    // 监控数据模型
    public static class SystemMetrics {
        private double cpuUsage;             // CPU 使用率
        private long totalHeapMemory;        // 堆内存总大小
        private long usedHeapMemory;         // 已用堆内存
        private long totalNonHeapMemory;     // 堆外内存总大小
        private long usedNonHeapMemory;      // 已用堆外内存
        private List<ThreadInfo> threadInfoList; // 线程信息

        // Getters and setters
        public double getCpuUsage() { return cpuUsage; }
        public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }

        public long getTotalHeapMemory() { return totalHeapMemory; }
        public void setTotalHeapMemory(long totalHeapMemory) { this.totalHeapMemory = totalHeapMemory; }

        public long getUsedHeapMemory() { return usedHeapMemory; }
        public void setUsedHeapMemory(long usedHeapMemory) { this.usedHeapMemory = usedHeapMemory; }

        public long getTotalNonHeapMemory() { return totalNonHeapMemory; }
        public void setTotalNonHeapMemory(long totalNonHeapMemory) { this.totalNonHeapMemory = totalNonHeapMemory; }

        public long getUsedNonHeapMemory() { return usedNonHeapMemory; }
        public void setUsedNonHeapMemory(long usedNonHeapMemory) { this.usedNonHeapMemory = usedNonHeapMemory; }

        public List<ThreadInfo> getThreadInfoList() { return threadInfoList; }
        public void setThreadInfoList(List<ThreadInfo> threadInfoList) { this.threadInfoList = threadInfoList; }
    }

    // 线程信息模型
    public static class ThreadInfo {
        private String threadName;
        private String threadState;
        private long cpuTime;

        // Getters and setters
        public String getThreadName() { return threadName; }
        public void setThreadName(String threadName) { this.threadName = threadName; }

        public String getThreadState() { return threadState; }
        public void setThreadState(String threadState) { this.threadState = threadState; }

        public long getCpuTime() { return cpuTime; }
        public void setCpuTime(long cpuTime) { this.cpuTime = cpuTime; }
    }

    // 获取系统监控数据
    public static SystemMetrics getSystemMetrics() {
        SystemMetrics metrics = new SystemMetrics();
        
        // 获取 CPU 使用情况
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        metrics.setCpuUsage(osBean.getSystemCpuLoad() * 100);

        // 获取内存使用情况（堆内存）
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        metrics.setTotalHeapMemory(memoryMXBean.getHeapMemoryUsage().getMax());
        metrics.setUsedHeapMemory(memoryMXBean.getHeapMemoryUsage().getUsed());

        // 获取堆外内存
        metrics.setTotalNonHeapMemory(memoryMXBean.getNonHeapMemoryUsage().getMax());
        metrics.setUsedNonHeapMemory(memoryMXBean.getNonHeapMemoryUsage().getUsed());

        // 获取线程信息
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        List<ThreadInfo> threadInfos = new ArrayList<>();
        long[] threadIds = threadMXBean.getAllThreadIds();
        for (long threadId : threadIds) {
            ThreadInfo threadInfo = new ThreadInfo();
            java.lang.management.ThreadInfo info = threadMXBean.getThreadInfo(threadId);
            if (info != null) {
                threadInfo.setThreadName(info.getThreadName());
                threadInfo.setThreadState(info.getThreadState().toString());
                threadInfo.setCpuTime(threadMXBean.getThreadCpuTime(threadId));
            }
            threadInfos.add(threadInfo);
        }
        metrics.setThreadInfoList(threadInfos);

        return metrics;
    }
}
