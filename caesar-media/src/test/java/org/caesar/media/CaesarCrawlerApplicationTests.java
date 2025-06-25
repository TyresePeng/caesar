package org.caesar.media;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.caesar.common.util.StringUtils;
import org.caesar.media.client.DouyinClient;
import org.caesar.media.entity.JobConfig;
import org.caesar.media.entity.JobResource;
import org.caesar.media.entity.PlatformUser;
import org.caesar.media.enums.PublishTimeType;
import org.caesar.media.enums.SearchChannelType;
import org.caesar.media.enums.SearchSortType;
import org.caesar.media.service.JobConfigService;
import org.caesar.media.service.JobResourceService;
import org.caesar.media.service.PlatformUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@SpringBootTest
@Log4j2
class CaesarCrawlerApplicationTests {
    @Resource
    private  JobConfigService jobConfigService;
    @Resource
    private  DouyinClient douyinClient;
    @Resource
    private  JobResourceService jobResourceService;

    @Resource
    private PlatformUserService platformUserService;
//
//    @Test
//    public void  queryRoom(){
////        JSONObject queryRoom = douyinClient.queryRoom("38337191756");
//        System.err.println();
//    }
//
//    @Test
//    public void  VideoCaptureScheduledTaskTest() {
//        List<JobConfig> list = jobConfigService.list();
//        JobConfig jobConfig = list.stream().findAny().orElse(null);
//        String params = jobConfig.getParams();
//        List<PlatformUser> platformUserList = platformUserService.platformActiveUserList();
//        if (params != null && !CollectionUtils.isEmpty(platformUserList)) {
//            JSONObject jsonObject = JSONObject.parseObject(params);
//            String keyword = jsonObject.getString("keyword");
//            if (StringUtils.isBlank(keyword)) {
//                log.error("任务参数错误:{},测试没有检索关键字", jobConfig.getTitle());
//            } else {
//                Integer offset = jsonObject.getInteger("offset");
//                if (offset == null || offset == 0) {
//                    offset = 1;
//                } else {
//                    offset = offset + 1;
//                }
//                Integer count = jsonObject.getInteger("count");
//                if (count == null) {
//                    count = 10;
//                }
//                PublishTimeType publishTimeType;
//                if (StringUtils.isBlank(jsonObject.getString("publishTimeType"))){
//                    publishTimeType=PublishTimeType.UNLIMITED;
//                }else {
//                    publishTimeType = PublishTimeType.fromValue(jsonObject.getString("publishTimeType"));
//                }
//                SearchChannelType searchChannelType;
//                if (StringUtils.isBlank(jsonObject.getString("searchChannelType"))){
//                    searchChannelType=SearchChannelType.GENERAL;
//                }else {
//                    searchChannelType = SearchChannelType.fromValue(jsonObject.getString("searchChannelType"));
//                }
//                SearchSortType searchSortType;
//                if (StringUtils.isBlank(jsonObject.getString("searchSortType"))){
//                    searchSortType=SearchSortType.GENERAL;
//                }else {
//                    searchSortType = SearchSortType.fromValue(jsonObject.getString("searchSortType"));
//                }
//                //platformUserList
//                for (PlatformUser platformUser : platformUserList) {
//                    log.info("开始抓取任务:{},执行账户:{}", jobConfig.getTitle(), platformUser.getName());
//                    try {
//                        douyinClient.checkSession();
//                        JSONObject queryKeyWord = douyinClient.queryKeyWord("跳舞", 0, 10, PublishTimeType.UNLIMITED, SearchChannelType.GENERAL, SearchSortType.GENERAL);
//                        if (queryKeyWord != null){
//                            JobResource jobResource = new JobResource();
//                            jobResource.setJobConfigId(jobConfig.getId());
//                            jobResource.setPlatformCode(platformUser.getCode());
//                            jobResource.setResourceType("video");
//                            jobResource.setTitle(queryKeyWord.getString("title"));
//                            jobResource.setUrl(queryKeyWord.getString("url"));
//                            jobResource.setCoverUrl(queryKeyWord.getString("coverUrl"));
//                            jobResource.setAuthor(queryKeyWord.getString("author"));
//                            jobResource.setTags(queryKeyWord.getString("tags"));
//                            jobResource.setMeta(queryKeyWord.getString("meta"));
//                            jobResourceService.save(jobResource);
//                        }
//                    } catch (Exception e) {
//                        log.error("抓取异常:{}",e.getMessage(),e);
//                    }finally {
//                        offset = offset + 100;
//                    }
//                    JSONObject param = new JSONObject();
//                    param.put("offset", offset);
//                    param.put("count", count);
//                    param.put("publishTimeType", publishTimeType.getValue());
//                    param.put("searchChannelType", searchChannelType.getValue());
//                    param.put("searchSortType", searchSortType.getValue());
//                    param.put("keyword", keyword);
//                    jobConfig.setParams(param.toJSONString());
//                    jobConfigService.updateById(jobConfig);
//                }
//            }
//        }
//    }
//
//

}
