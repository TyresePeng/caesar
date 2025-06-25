package org.caesar.media.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本正则匹配工具类
 * 提供提取单个或多个正则分组结果的方法
 *
 * 用于从字符串中快速提取所需内容，如 ID、Token 等
 *
 * @author peng
 * @since 2025-06-20
 */
public class MatcherUtils {

    /**
     * 提取正则表达式的第一个分组（group(1)）内容
     *
     * @param text  目标文本
     * @param regex 正则表达式，需包含至少一个分组
     * @return 匹配到的第一个分组内容；未匹配到则返回 null
     */
    public static String extractGroup(String text, String regex) {
        if (text == null || regex == null) {
            return null;
        }

        Matcher matcher = Pattern.compile(regex).matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * 提取正则表达式中所有分组的匹配内容（group(1)...group(n)）
     *
     * @param text  目标文本
     * @param regex 正则表达式，需包含多个分组
     * @return 字符串数组，每个元素对应一个分组；未匹配到则返回 null
     */
    public static String[] extractGroups(String text, String regex) {
        if (text == null || regex == null) {
            return null;
        }

        Matcher matcher = Pattern.compile(regex).matcher(text);
        if (matcher.find()) {
            int groupCount = matcher.groupCount();
            String[] groups = new String[groupCount];
            for (int i = 0; i < groupCount; i++) {
                groups[i] = matcher.group(i + 1);
            }
            return groups;
        }
        return null;
    }
}
