package org.caesar.media.client;

import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.LoadState;
import lombok.extern.log4j.Log4j2;
import okhttp3.Headers;
import org.caesar.common.util.StringUtils;
import org.caesar.media.exception.MediaException;
import org.caesar.media.factory.PlaywrightFactoryPool;
import org.caesar.media.proto.douyin.Live;
import org.caesar.media.service.DouyinLiveMessageService;
import org.caesar.media.utils.MatcherUtils;
import org.caesar.media.utils.RequestParametersUtils;
import org.caesar.media.utils.DouyinSignUtil;
import org.caesar.media.utils.OkHttpUtils;
import org.caesar.media.websocket.DouyinLiveWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 抖音请求Client
 * @Author: peng.guo
 * @Create: 2025-05-22 16:08
 * @Version 1.0
 **/
@Component
@Log4j2
public class DouyinLiveClient {

    @Resource
    private PlaywrightFactoryPool playwrightFactoryPool;

    @Resource
    private DouyinLiveMessageService douyinLiveMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final Map<String, DouyinLiveWebSocket> wsClientMap = new ConcurrentHashMap<>();


    // stealth 插件脚本路径，用于防止被反爬虫检测
    private static final String STEALTH_SCRIPT_PATH = "libs/stealth.min.js";

    // 抖音直播主页地址
    private static final String DOUYIN_URL = "https://live.douyin.com/";


    /**
     * 根据直播间 ID 查询直播间信息
     *
     * @param webCaseId 直播间页面url后面的数字
     * @return 返回直播间信息 JSON 对象，失败返回 null
     */
    @Retryable(
            value = Exception.class,
            backoff = @Backoff(delay = 2000, multiplier = 3)
    )
    public JSONObject queryRoom(String webCaseId) {
        JSONObject ret = playwrightFactoryPool.withFactory(factory -> {
            return factory.doWithContext(ctx -> {
                ctx.addInitScript(STEALTH_SCRIPT_PATH);
                Page page = ctx.newPage();
                page.navigate(DOUYIN_URL + webCaseId);
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);

                // 获取当前页面 cookie 并转换为字符串格式
                List<Cookie> cookies = ctx.cookies();
                String cookieStr = RequestParametersUtils.convertCookiesToString(cookies);

                // 构建请求参数
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put("live_id", "1");
                queryParams.put("room_id_str", webCaseId);
                queryParams.put("web_rid", webCaseId);

                // 构建请求头
                String userAgent = page.evaluate("() => navigator.userAgent").toString();
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", userAgent);
                headers.put("Cookie", cookieStr);
                headers.put("Host", "live.douyin.com");
                headers.put("Origin", DOUYIN_URL);
                headers.put("referer", DOUYIN_URL);
                headers.put("Content-Type", "application/json;charset=UTF-8");

                String uri="/webcast/room/web/enter/";
                // 从 localStorage 读取 xmst 用于生成签名参数
                Map<String, String> localStorage = (Map<String, String>) page.evaluate("() => window.localStorage");
                Map<String, String> commonParams = DouyinSignUtil.getCommonParams(queryParams, uri,localStorage.get("xmst"), userAgent);

                try {
                    String response = OkHttpUtils.getInstance().getSync(
                            DOUYIN_URL+uri,
                            JSONObject.parseObject(JSONObject.toJSONString(commonParams)),
                            headers
                    );
                    if (StringUtils.isBlank(response)){
                        log.error("请求抖音房间信息查询接口失败");
                        throw new MediaException("请求抖音房间信息查询接口失败");
                    }
                    return JSONObject.parseObject(response);
                } catch (IOException e) {
                    log.error("请求抖音房间信息查询接口失败: {}", e.getMessage(), e);
                    throw new MediaException(e.getMessage(),e);
                }
            });
        });
        return ret;
    }

    /**
     * 获取抖音直播间的 WebSocket 地址与 Cookies
     *
     * @param webCaseId 直播间页面 URL 中的数字 ID
     * @return 包含 wsUrl、房间标题、状态、cookies 和 userAgent 的 JSON 对象，失败返回 null
     */
    @Retryable(
            value = Exception.class, // 哪些异常触发重试
            backoff = @Backoff(delay = 2000, multiplier = 3)

    )
    public JSONObject queryRoomWsUrl(String webCaseId) {
        JSONObject ret = playwrightFactoryPool.withFactory(factory -> {
            log.info("开始查询抖音房间信息");
            return factory.doWithContext(ctx -> {
                ctx.addInitScript(STEALTH_SCRIPT_PATH);
                Page page = ctx.newPage();
                page.navigate(DOUYIN_URL + webCaseId);
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);

                // 获取 cookie 字符串
                List<Cookie> cookies = ctx.cookies();
                String cookieStr = RequestParametersUtils.convertCookiesToString(cookies);

                // 获取页面源码，正则提取 roomId 和 userId
                String script = page.evaluate("() => document.body.innerHTML").toString();
                String roomId = MatcherUtils.extractGroup(script, "\\\\\"roomId\\\\\":\\\\\"(\\d+)\\\\\"");
                String userId = MatcherUtils.extractGroup(script, "\\\\\"user_unique_id\\\\\":\\\\\"(\\d+)\\\\\"");

                // 提取房间状态和标题信息（数组第0为状态，第1为标题）
                String[] roomInfo = MatcherUtils.extractGroups(script, "\\\\\"roomInfo\\\\\":\\{\\\\\"room\\\\\":\\{\\\\\"id_str\\\\\":\\\\\".*?\\\\\",\\\\\"status\\\\\":(.*?),\\\\\"status_str\\\\\":\\\\\".*?\\\\\",\\\\\"title\\\\\":\\\\\"(.*?)\\\\\"");

                // 构造待签名字符串并转换为字节数组
                String words = String.format(
                        "live_id=1,aid=6383,version_code=180800,webcast_sdk_version=1.0.14-beta.0," +
                                "room_id=%s,sub_room_id=,sub_channel_id=,did_rule=3,user_unique_id=%s," +
                                "device_platform=web,device_type=,ac=,identity=audience",
                        roomId, userId);
                String wordsSin = DouyinSignUtil.wordsToBytes(words);

                String userAgent = page.evaluate("() => navigator.userAgent").toString();

                // 等待 window.byted_acrawler 对象出现
                Boolean exists = (Boolean) page.evaluate("() => !!window.byted_acrawler");

                if (!exists) {
                    log.warn("window.byted_acrawler 未出现，结束程序");
                    throw new MediaException("window.byted_acrawler 未出现");
                }

                // 调用 frontierSign 函数生成签名参数
                Map<String, String> frontierSignMap = (Map<String, String>) page.evaluate(DouyinSignUtil.FRONTIER_SIGN_STR,wordsSin);
                if (frontierSignMap == null) {
                    log.warn("frontierSign 返回为空");
                    throw new MediaException("frontierSign 返回为空");
                }
                String frontierSign = frontierSignMap.get("X-Bogus");

                // 获取 WebSocket 参数
                Map<String, String> wssParams = DouyinSignUtil.getWssParams(roomId, userId, frontierSign);
                String url = "wss://webcast100-ws-web-lf.douyin.com/webcast/im/push/v2/?" +
                        RequestParametersUtils.buildQueryString(wssParams);

                // 构造返回数据
                JSONObject retData = new JSONObject();
                retData.put("wsUrl", url);
                retData.put("roomTitle", roomInfo.length > 1 ? roomInfo[1] : "");
                retData.put("roomStatus", roomInfo.length > 0 ? roomInfo[0] : "");
                retData.put("cookies", cookieStr);
                retData.put("userAgent", userAgent);
                return retData;
            });
        });
        return ret;
    }

    /**
     * 连接直播间 WebSocket，接收并处理直播消息
     *
     * @param webCaseId 直播间页面 URL 中的数字 ID
     */
    @Retryable(
            value = Exception.class, // 哪些异常触发重试
            backoff = @Backoff(delay = 2000, multiplier = 3)
    )
    public void connectRoom(String webCaseId) {
        JSONObject roomInfo = this.queryRoomWsUrl(webCaseId);
        if (roomInfo == null) {
            log.error("无法获取直播间 WebSocket 地址，连接失败");
            throw new MediaException("无法获取直播间 WebSocket 地址，连接失败");
        }

        String wsUrl = roomInfo.getString("wsUrl");
        String roomTitle = roomInfo.getString("roomTitle");
        String roomStatus = roomInfo.getString("roomStatus");
        String userAgent = roomInfo.getString("userAgent");
        String cookie = roomInfo.getString("cookies");

        log.info("连接直播间：{}，状态：{}", roomTitle, roomStatus);
        log.info("WebSocket URL: {}", wsUrl);

        // 构造 WebSocket 请求头
        Headers headers = new Headers.Builder()
                .add("Pragma", "no-cache")
                .add("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .add("User-Agent", userAgent)
                .add("Upgrade", "websocket")
                .add("Cache-Control", "no-cache")
                .add("Connection", "Upgrade")
                .add("Cookie", cookie)
                .build();

        // 实例化 WebSocket 客户端，设置消息回调
        DouyinLiveWebSocket douyinLiveWebSocket = new DouyinLiveWebSocket(
                wsUrl,
                message -> {
                    log.info("📥 收到消息类型: {}", message.getMethod());
                    try {
                        handleMessage(message, webCaseId);
                    } catch (Exception e) {
                        log.error("处理直播消息异常: {}", e.getMessage(), e);
                    }
                }
        );
        // 连接 WebSocket
        douyinLiveWebSocket.connect(headers);
        // 保存连接到 Map 中
        wsClientMap.put(webCaseId, douyinLiveWebSocket);
    }

    /**
     * 分类型处理接收到的直播消息，并保存至数据库
     *
     * @param msg    Live.Message protobuf 消息对象
     * @param roomId 直播间 ID
     * @throws Exception 反序列化失败等异常
     */
    private void handleMessage(Live.Message msg, String roomId) throws Exception {
        String method = msg.getMethod();
        long msgId = msg.getMsgId();
        byte[] payloadBytes = msg.getPayload().toByteArray();
        String content;
        JSONObject message = new JSONObject();
        message.put("method", method);
        switch (method) {
            case "WebcastGiftMessage": {
                Live.GiftMessage gift = Live.GiftMessage.parseFrom(payloadBytes);
                content = gift.getGift().getName();
                log.info("🎁 [{}] {} 送 {} 礼物给 {}", msgId,
                        gift.getUser().getNickname(),
                        gift.getGift().getName(),
                        gift.getToUser().getNickname());
                break;
            }

            case "WebcastChatMessage": {
                Live.ChatMessage chat = Live.ChatMessage.parseFrom(payloadBytes);
                content = chat.getContent();
                log.info("💬 [{}] {}：{}", msgId, chat.getUser().getNickname(), content);
                message.put("content", content);
                message.put("nickname", chat.getUser().getNickname());
                this.sendMessage(roomId, message);
                break;
            }

            case "WebcastMemberMessage": {
                Live.MemberMessage member = Live.MemberMessage.parseFrom(payloadBytes);
                content = member.getUser().getNickname() + " 进入直播间";
                log.info("🚪 [{}] {} 进入直播间", msgId, member.getUser().getNickname());
                message.put("content", content);
                message.put("nickname", member.getUser().getNickname());
                this.sendMessage(roomId, message);
                break;
            }

            case "WebcastLikeMessage": {
                Live.LikeMessage like = Live.LikeMessage.parseFrom(payloadBytes);
                content = "点赞：" + like.getCount();
                log.info("👍 [{}] {} 点赞（次数：{}，总数：{}）", msgId,
                        like.getUser().getNickname(), like.getCount(), like.getTotal());
                break;
            }

            case "WebcastSocialMessage": {
                Live.SocialMessage social = Live.SocialMessage.parseFrom(payloadBytes);
                content = social.getUser().getNickname() + " 关注主播";
                log.info("⭐ [{}] {} 关注了主播", msgId, social.getUser().getNickname());
                break;
            }

            case "WebcastRoomStatsMessage": {
                Live.RoomStatsMessage stats = Live.RoomStatsMessage.parseFrom(payloadBytes);
                content = stats.getDisplayLong();
                log.debug("📊 房间数据更新：{}", content);
                break;
            }

            default:
                content = "(未知消息类型)";
                log.debug("📩 收到未知类型消息 [{}]: {}", msgId, method);
        }

        // 持久化消息到数据库
        douyinLiveMessageService.saveMessage(msgId, roomId, method, content, payloadBytes);
    }
    /**
     * 断开 WebSocket 连接
     *
     * @param webCaseId WebCaseId
     */
    public void disconnectRoom(String webCaseId) {
        DouyinLiveWebSocket socket = wsClientMap.remove(webCaseId);
        if (socket != null) {
            socket.disconnect();
            log.info("🔌 断开直播间 [{}] 的 WebSocket 连接", webCaseId);
        } else {
            log.warn("没有找到 WebSocket 连接用于 webCaseId={}", webCaseId);
        }
    }
    /**
     * 发送消息给前端
     * @param webCaseId 直播间id
     * @param message 信息
     */
    public void  sendMessage(String webCaseId, JSONObject message){
        messagingTemplate.convertAndSend("/topic/room/"+webCaseId, message);
    }
}
