package org.caesar.media.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author job类型
 */
@Getter
@AllArgsConstructor
public enum JobType {

    /**
     * 平台code
     */
    VIDEO_CAPTURE("VIDEO_CAPTURE","视频抓取"),
    VIDEO_PUBLISH("VIDEO_PUBLISH","视频发布");
    private final String value;
    private final String msg;

    public static JobType getJobType(String jobType) {
        for (JobType jobTypeEnum : JobType.values()) {
            if (jobTypeEnum.value.equals(jobType)) {
                return jobTypeEnum;
            }
        }
        return null;
    }
}
