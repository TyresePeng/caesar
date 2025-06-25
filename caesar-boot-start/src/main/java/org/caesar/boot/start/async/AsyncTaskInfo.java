package org.caesar.boot.start.async;

import lombok.Data;

import java.util.List;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 异步任务信息</p>
 * <p> @createTime 2019-03-14 22:53:00</p>
 */
@Data
public class AsyncTaskInfo {
    /**
     * 任务id
     */
    private String id;
    /**
     * 执行状态 0执行中 1执行完成 2执行失败
     */
    private Integer status;
    /**
     * 执行信息
     */
    private String message;
}
