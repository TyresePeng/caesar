package org.caesar.media.factory;

import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.*;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.caesar.media.function.ContextPageConsumer;
import org.caesar.media.function.ContextPageFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Description: Playwright 工厂
 * @Author: peng.guo
 * @Create: 2025-05-21 11:38
 * @Version 1.3
 **/
@Log4j2
public class PlaywrightFactory {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private boolean tracingStarted = false;
    private String tracingSavePath = "trace.zip";
    private String storageStatePath;

    /**
     * -- GETTER --
     *  获取 Playwright 实例id
     * @return Playwright 实例id
     */
    @Getter
    private Long playwrightId=0L;

    @Getter
    private String storageStateJson;

    /**
     * 带 StorageState 的初始化
     * @param headless           是否无头模式
     * @param storageStateJson   StorageState JSON 字符串
     */
    public void initWithStorageStateJson(boolean headless,  String storageStateJson,Long playwrightId) {
        try {
            this.playwrightId = playwrightId;
            this.storageStateJson = storageStateJson;
            // 将 JSON 写入临时文件
            Path tempStorageFile = Files.createTempFile("playwright-storage", ".json");
            Files.write(tempStorageFile, storageStateJson.getBytes(), StandardOpenOption.WRITE);
            log.info("使用 JSON 字符串初始化 StorageState，写入临时文件：{}", tempStorageFile);
            // 使用临时文件路径初始化
            this.init(headless, false, null, true, tempStorageFile.toString());
            // 启动后可删除临时文件（可选）
            tempStorageFile.toFile().deleteOnExit();

        } catch (IOException e) {
            log.error("通过 JSON 初始化 StorageState 失败", e);
        }
    }


    /**
     * 带 StorageState 的初始化
     * @param headless           是否无头模式
     * @param enableTracing       是否启用 Tracing
     * @param tracingPath        Tracing 保存路径（可为 null 使用默认值 trace.zip）
     * @param storageStateJson   StorageState JSON
     */
    public void initWithStorageStateJson(boolean headless, boolean enableTracing, String tracingPath, String storageStateJson) {
        try {
            // 将 JSON 写入临时文件
            Path tempStorageFile = Files.createTempFile("playwright-storage", ".json");
            Files.write(tempStorageFile, storageStateJson.getBytes(), StandardOpenOption.WRITE);
            log.info("使用 JSON 字符串初始化 StorageState，写入临时文件：{}", tempStorageFile);
            // 使用临时文件路径初始化
            this.init(headless, enableTracing, tracingPath, true, tempStorageFile.toString());
            // 启动后可删除临时文件（可选）
            tempStorageFile.toFile().deleteOnExit();

        } catch (IOException e) {
            log.error("通过 JSON 初始化 StorageState 失败", e);
        }
    }

    /**
     * 初始化 Playwright，支持 Tracing 和 StorageState 的加载与保存
     * @param tracingPath        Tracing 保存路径（可为 null 使用默认值 trace.zip）
     * @param headless           是否无头模式
     * @param enableTracing      是否启用 Tracing
     */
    public void init(boolean headless, boolean enableTracing,String tracingPath) {
      this.init(headless, enableTracing, tracingPath, false, null);
    }

    /**
     * 初始化 Playwright，支持 Tracing 和 StorageState 的加载与保存
     *
     * @param headless           是否无头模式
     * @param enableTracing      是否启用 Tracing
     * @param tracingPath        Tracing 保存路径（可为 null 使用默认值 trace.zip）
     * @param enableStorageState 是否启用 StorageState 存储与加载
     * @param storagePath        StorageState 文件路径（启用时必须指定）
     */
    public void init(boolean headless, boolean enableTracing, String tracingPath,
                     boolean enableStorageState, String storagePath) {

        try {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));

            Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();

            if (enableStorageState && storagePath != null && !storagePath.trim().isEmpty()) {
                storageStatePath = storagePath;
                Path path = Paths.get(storageStatePath);
                if (java.nio.file.Files.exists(path)) {
                    contextOptions.setStorageStatePath(path);
                    log.info("加载 StorageState：{}", storagePath);
                }
            }

            context = browser.newContext(contextOptions);

            if (enableTracing) {
                tracingStarted = true;
                if (tracingPath != null && !tracingPath.trim().isEmpty()) {
                    tracingSavePath = tracingPath;
                }
                context.tracing().start(new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
                );
                log.info("Tracing 已启动，保存路径为：{}", tracingSavePath);
            }

            log.info("Playwright 初始化成功");
        } catch (Exception e) {
            log.error("Playwright 初始化失败", e);
        }
    }

    /**
     * 保存当前上下文的 StorageState
     */
    public void saveStorageState() {
        if (context == null || storageStatePath == null) {
            log.warn("无法保存 StorageState：Context 或路径未配置");
            return;
        }
        context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get(storageStatePath)));
        log.info("StorageState 已保存至：{}", storageStatePath);
    }

    /**
     * 停止 Tracing 并保存文件
     *
     * @param path 保存路径（可为 null 使用默认 tracingSavePath）
     */
    public void stopTracing(String path) {
        if (!tracingStarted || context == null) {
            log.warn("Tracing 未启动或 Context 不存在");
            return;
        }
        String finalPath = (path != null && !path.trim().isEmpty()) ? path : tracingSavePath;
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(finalPath)));
        tracingStarted = false;
        log.info("Tracing 已保存至：{}", finalPath);
    }

    /**
     * 页面操作封装
     *
     * @param pageConsumer 页面逻辑
     */
    public void doWithPage(Consumer<Page> pageConsumer) {
        Page page = null;
        try {
            page = context.newPage();
            pageConsumer.accept(page);
        } catch (Exception e) {
            log.error("页面操作异常", e);
        } finally {
            if (page != null) {
                page.close();
            }
        }
    }

    /**
     * Context 操作封装（访问 Cookies、Tracing、Storage 等）
     *
     * @param contextConsumer 操作函数
     */
    public void doWithContext(Consumer<BrowserContext> contextConsumer) {
        try {
            contextConsumer.accept(context);
        } catch (Exception e) {
            log.error("Context 操作异常", e);
            throw e;
        }
    }

    /**
     * 支持返回值的 Context 操作封装
     *
     * @param contextFunction 有返回值的操作函数
     * @param <T> 返回类型
     * @return 操作结果
     */
    public <T> T doWithContext(Function<BrowserContext, T> contextFunction) {
        try {
            return contextFunction.apply(context);
        } catch (Exception e) {
            log.error("Context 操作异常", e);
            throw e;
        }
    }

    public void doWithContextAndPage(ContextPageConsumer consumer) throws Exception {
        Page page = null;
        try {
            page = context.newPage();
            consumer.accept(context, page);
        } catch (Exception e) {
            log.error("Context + Page 操作异常", e);
            throw e;
        } finally {
            if (page != null) {
                page.close();
            }
        }
    }
    public <T> T doWithContextAndPage(ContextPageFunction<T> function) throws Exception {
        Page page = null;
        try {
            page = context.newPage();
            return function.apply(context, page);
        } catch (Exception e) {
            log.error("Context + Page 操作异常", e);
            throw e;
        } finally {
            if (page != null) {
                page.close();
            }
        }
    }

    /**
     * 关闭所有资源，并根据配置保存 StorageState 与 Tracing
     */
    public void close() {
        if (context != null && storageStatePath != null) {
            saveStorageState();
        }
        if (tracingStarted) {
            stopTracing(null);
        }
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        log.info("Playwright 资源已关闭");
    }


}
