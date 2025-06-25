package org.caesar.media.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 菜单
 * @Author: peng.guo
 * @Create: 2025-05-30 18:27
 * @Version 1.0
 **/
public class RouteVo {

    public static List<MenuVo> getMenuList() {
        List<MenuVo> list = new ArrayList<>();

        // 一级菜单：平台管理
        MenuVo system = new MenuVo();
        system.setPath("/platform");
        system.setComponent("Layout");
        system.setRedirect("/platform/index");
        system.setName("platform-manager");

        MetaVo systemMeta = new MetaVo();
        systemMeta.setTitle("平台管理");
        systemMeta.setIcon("system");
        systemMeta.setHidden(false);
        systemMeta.setAlwaysShow(false);
        systemMeta.setParams(null);
        system.setMeta(systemMeta);

        MenuVo index = new MenuVo();
        index.setPath("index");
        index.setComponent("platform/index");
        index.setName("platform-index");

        MetaVo indexMeta = new MetaVo();
        indexMeta.setTitle("用户池");
        indexMeta.setIcon("user_manage");
        indexMeta.setHidden(false);
        indexMeta.setAlwaysShow(false);
        indexMeta.setKeepAlive(true);
        index.setMeta(indexMeta);

        system.setChildren(Collections.singletonList(index));
        list.add(system);

        // 一级菜单：短视频工作台
        MenuVo shortVideo = new MenuVo();
        shortVideo.setPath("/job");
        shortVideo.setComponent("Layout");
        shortVideo.setRedirect("/job/index");
        shortVideo.setName("job");

        MetaVo shortVideoMeta = new MetaVo();
        shortVideoMeta.setTitle("任务工作台");
        shortVideoMeta.setIcon("job");
        shortVideoMeta.setHidden(false);
        shortVideoMeta.setAlwaysShow(false);
        shortVideoMeta.setParams(null);
        shortVideo.setMeta(shortVideoMeta);

        MenuVo shortVideoHome = new MenuVo();
        shortVideoHome.setPath("index");
        shortVideoHome.setComponent("job/index");
        shortVideoHome.setName("job");

        MetaVo shortVideoHomeMeta = new MetaVo();
        shortVideoHomeMeta.setTitle("任务工作台");
        shortVideoHomeMeta.setIcon("job");
        shortVideoHomeMeta.setHidden(false);
        shortVideoHomeMeta.setAlwaysShow(false);
        shortVideoHomeMeta.setKeepAlive(true);
        shortVideoHome.setMeta(shortVideoHomeMeta);

        shortVideo.setChildren(Collections.singletonList(shortVideoHome));
        list.add(shortVideo);

        // 一级菜单：直播间控制台
        MenuVo liveRoom = new MenuVo();
        liveRoom.setPath("/live-room");
        liveRoom.setComponent("Layout");
        liveRoom.setRedirect("/live-room/index");
        liveRoom.setName("live-room");

        MetaVo liveRoomMeta = new MetaVo();
        liveRoomMeta.setTitle("直播间控制台");
        liveRoomMeta.setIcon("system");
        liveRoomMeta.setHidden(false);
        liveRoomMeta.setAlwaysShow(false);
        liveRoomMeta.setParams(null);
        liveRoom.setMeta(liveRoomMeta);

        MenuVo liveRoomDash = new MenuVo();
        liveRoomDash.setPath("index");
        liveRoomDash.setComponent("live-room/index");
        liveRoomDash.setName("live-room");

        MetaVo liveRoomDashMeta = new MetaVo();
        liveRoomDashMeta.setTitle("直播间控制台");
        liveRoomDashMeta.setIcon("live_room");
        liveRoomDashMeta.setHidden(false);
        liveRoomDashMeta.setAlwaysShow(false);
        liveRoomDashMeta.setKeepAlive(true);
        liveRoomDash.setMeta(liveRoomDashMeta);

        liveRoom.setChildren(Collections.singletonList(liveRoomDash));
        list.add(liveRoom);

        return list;
    }


}
