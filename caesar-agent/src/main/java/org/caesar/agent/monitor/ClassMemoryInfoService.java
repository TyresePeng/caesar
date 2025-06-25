package org.caesar.agent.monitor;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import java.lang.instrument.Instrumentation;

public class ClassMemoryInfoService {

    private static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    // 返回类的内存占用信息
    public static String getClassMemoryInfo(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        long objectSize = instrumentation.getObjectSize(clazz);
        return "Object size of " + className + ": " + objectSize + " bytes";
    }

    // 返回对象的内存布局图
    public static String getObjectMemoryLayout(Object object) {
        return ClassLayout.parseInstance(object).toPrintable();
    }

    // 返回所有实例的内存布局
    public static String getObjectGraphLayout(Object object) {
        return GraphLayout.parseInstance(object).toPrintable();
    }
}
