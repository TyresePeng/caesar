package org.caesar.media.factory;

import lombok.extern.log4j.Log4j2;
import org.caesar.boot.start.exception.BusinessException;
import org.caesar.media.entity.PlatformUser;
import org.caesar.media.service.PlatformUserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * PlaywrightFactoryPool 用于管理多个 PlaywrightFactory 实例的对象池。
 * 支持自动初始化、获取、释放、关闭等操作，确保 Playwright 使用的高效性与线程安全。
 *
 * @author peng.guo
 */
@Component
@Log4j2
public class PlaywrightFactoryPool {

    /**
     * 平台用户服务，用于获取活跃用户及其会话信息
     */
    @Resource
    private PlatformUserService platformUserService;

    /**
     * 使用阻塞队列存储 PlaywrightFactory 实例，实现线程安全
     */
    private final BlockingQueue<PlaywrightFactory> pool = new LinkedBlockingQueue<>();


    /**
     * 在容器启动时执行初始化操作，创建多个 PlaywrightFactory 实例
     * 并初始化每个实例中所有活跃用户的会话状态
     */
    @PostConstruct
    public void initPool() {
        dependencyCheck();
        List<PlatformUser> platformUserList = platformUserService.platformActiveUserList();
        try {
            for (PlatformUser platformUser : platformUserList) {
                // 创建并初始化一个工厂实例
                PlaywrightFactory factory = createFactoryWithSessions(platformUser);
                pool.offer(factory);
            }
        } catch (Exception e) {
            log.error("初始化 PlaywrightFactory 实例失败", e);
        }

        log.info("PlaywrightFactoryPool 初始化完成，共创建 {} 个工厂实例", pool.size());
    }


    /**
     * 测试依赖
     */
    public void dependencyCheck(){
        try {
            PlaywrightFactory factory = new PlaywrightFactory();
            factory.init(true, true, "");
        } catch (Exception e) {
            log.error("尝试调用Playwright失败", e);
            throw e;
        }
    }

    /**
     * 创建 PlaywrightFactory 并为传入的用户列表初始化会话状态
     *
     * @param platformUser 活跃用户
     * @return 初始化后的 PlaywrightFactory 实例
     */
    private PlaywrightFactory createFactoryWithSessions(PlatformUser platformUser) {
        PlaywrightFactory factory = new PlaywrightFactory();
        try {
            // 使用用户 sessionStorage 初始化 Playwright 会话状态
            factory.initWithStorageStateJson(true, platformUser.getSessionStorage(), platformUser.getId());
        } catch (Exception e) {
            log.warn("初始化用户会话失败，userId={}, error={}", platformUser.getId(), e.getMessage());
        }
        return factory;
    }

    /**
     * 从池中获取一个 PlaywrightFactory 实例，阻塞等待直到可用
     *
     * @return 可用的 PlaywrightFactory 实例
     * @throws BusinessException 获取失败时抛出业务异常
     */
    public PlaywrightFactory acquire() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            log.error("获取 PlaywrightFactory 失败", e);
            throw new BusinessException("PlaywrightFactoryPool acquire error: " + e.getMessage(), e);
        }
    }

    /**
     * 将使用完毕的 PlaywrightFactory 实例归还到池中
     *
     * @param factory 待归还的实例
     */
    public void release(PlaywrightFactory factory) {
        boolean success = pool.offer(factory);
        if (!success) {
            log.warn("PlaywrightFactory 归还失败，池已满");
        }
    }

    /**
     * 使用 PlaywrightFactory 执行指定操作（带返回值），自动获取与归还资源
     *
     * @param action 操作逻辑
     * @param <T>    返回类型
     * @return 执行结果，失败返回 null
     */
    public <T> T withFactory(Function<PlaywrightFactory, T> action) {
        PlaywrightFactory factory = acquire();
        try {
            return action.apply(factory);
        } catch (Exception e) {
            log.error("PlaywrightFactory 执行失败", e);
           throw e;
        } finally {
            release(factory);
        }
    }

    /**
     * 使用 PlaywrightFactory 执行指定操作（无返回值），自动获取与归还资源
     *
     * @param action 操作逻辑
     */
    public void withFactory(Consumer<PlaywrightFactory> action) {
        PlaywrightFactory factory = acquire();
        try {
            action.accept(factory);
        } finally {
            release(factory);
        }
    }

    /**
     * 容器销毁前执行清理操作，关闭所有 PlaywrightFactory 实例
     */
    @PreDestroy
    public void shutdown() {
        log.info("正在关闭 PlaywrightFactoryPool...");
        PlaywrightFactory factory;
        while ((factory = pool.poll()) != null) {
            try {
                factory.close();
            } catch (Exception e) {
                log.warn("关闭 PlaywrightFactory 失败", e);
            }
        }
        log.info("PlaywrightFactoryPool 已关闭");
    }

}
