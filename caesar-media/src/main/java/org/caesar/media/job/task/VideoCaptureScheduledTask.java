package org.caesar.media.job.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.caesar.common.util.StringUtils;
import org.caesar.media.client.DouyinClient;
import org.caesar.media.entity.JobConfig;
import org.caesar.media.entity.JobResource;
import org.caesar.media.enums.PlatformType;
import org.caesar.media.enums.PublishTimeType;
import org.caesar.media.enums.SearchChannelType;
import org.caesar.media.enums.SearchSortType;
import org.caesar.media.job.JobTask;
import org.caesar.media.service.JobConfigService;
import org.caesar.media.service.JobResourceService;
import org.springframework.stereotype.Component;

/**
 * @author peng.guo
 */
@Log4j2
@Component("VIDEO_CAPTURE")
@RequiredArgsConstructor
public class VideoCaptureScheduledTask implements JobTask {

    private final JobConfigService jobConfigService;
    private final DouyinClient douyinClient;
    private final JobResourceService jobResourceService;


    @Override
    public void run(JobConfig jobConfig) {
        if (jobConfig.getStatus() != 1) {
            return;
        }
        log.info("开始执行任务:{}", jobConfig.getTitle());
        String params = jobConfig.getParams();
        JSONObject jsonObject = JSONObject.parseObject(params);
        String keyword = jsonObject.getString("keyword");
        if (StringUtils.isBlank(keyword)) {
            log.error("任务参数错误:{},测试没有检索关键字", jobConfig.getTitle());
        } else {
            int offset = jsonObject.getIntValue("offset");
            int count = jsonObject.getIntValue("count");
            if (count == 0) {
                count = 10;
            }
            PublishTimeType publishTimeType;
            if (StringUtils.isBlank(jsonObject.getString("publishTimeType"))) {
                publishTimeType = PublishTimeType.UNLIMITED;
            } else {
                publishTimeType = PublishTimeType.fromValue(jsonObject.getString("publishTimeType"));
            }
            SearchChannelType searchChannelType;
            if (StringUtils.isBlank(jsonObject.getString("searchChannelType"))) {
                searchChannelType = SearchChannelType.GENERAL;
            } else {
                searchChannelType = SearchChannelType.fromValue(jsonObject.getString("searchChannelType"));
            }
            SearchSortType searchSortType;
            if (StringUtils.isBlank(jsonObject.getString("searchSortType"))) {
                searchSortType = SearchSortType.GENERAL;
            } else {
                searchSortType = SearchSortType.fromValue(jsonObject.getString("searchSortType"));
            }
            try {
                JSONObject queryKeyWord = douyinClient.queryKeyWord(keyword, offset, count, publishTimeType, searchChannelType, searchSortType);
                if (queryKeyWord != null) {
                    JSONArray data = queryKeyWord.getJSONArray("data");
                    if (data.isEmpty()) {
                        log.info("没有数据");
                        jobConfigService.updateStatus(jobConfig.getId(), 3);
                        return;
                    }
                    for (Object datum : data) {
                        JSONObject meta = (JSONObject) datum;
                        JSONObject awemeInfo = meta.getJSONObject("aweme_info");
                        String awemeId = awemeInfo.getString("aweme_id");
                        JSONObject author = awemeInfo.getJSONObject("author");
                        JobResource jobResource = new JobResource();
                        jobResource.setResourceId(awemeId);
                        jobResource.setJobConfigId(jobConfig.getId());
                        jobResource.setPlatformCode(PlatformType.DOUYIN.getValue());
                        jobResource.setResourceType("video");
                        jobResource.setTitle(awemeInfo.getString("desc"));
                        jobResource.setAuthor(author.toJSONString());
                        jobResource.setMeta(meta.toJSONString());
                        jobResourceService.saveByResourceId(awemeId, jobResource);
                    }
                }
            } catch (Exception e) {
                log.error("抓取异常:{}", e.getMessage(), e);
            }
            JSONObject param = new JSONObject();
            offset=offset+10;
            param.put("offset", offset);
            param.put("count", count);
            param.put("publishTimeType", publishTimeType.getValue());
            param.put("searchChannelType", searchChannelType.getValue());
            param.put("searchSortType", searchSortType.getValue());
            param.put("keyword", keyword);
            JobConfig jobConfigUpdate = new JobConfig();
            jobConfigUpdate.setId(jobConfig.getId());
            jobConfigUpdate.setParams(param.toJSONString());
            jobConfigService.updateById(jobConfigUpdate);

        }
    }
}
