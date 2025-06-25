package org.caesar.media.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.PlaywrightException;

/**
 * @author peng.guo
 */
public class PlaywrightUtils {


    /**
     * 等待指定 Locator 消失（默认等待元素从 DOM 中移除）
     *
     * @param locator 要等待的元素
     * @param timeoutMillis 最长等待时间（毫秒）
     * @param waitState 消失状态：DETACHED（从 DOM 中移除）或 HIDDEN（不可见）
     * @return 是否成功等待到元素消失
     */
    public static boolean waitForElementToDisappear(Locator locator, int timeoutMillis, WaitForSelectorState waitState) {
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(waitState)
                    .setTimeout(timeoutMillis));
            return true;
        } catch (PlaywrightException e) {
            return false;
        }
    }

    /**
     * 简化重载：默认等待元素从 DOM 中移除（detached），超时 30 秒
     */
    public static boolean waitForElementToDisappear(Locator locator) {
        return waitForElementToDisappear(locator, 30_000, WaitForSelectorState.DETACHED);
    }
}
