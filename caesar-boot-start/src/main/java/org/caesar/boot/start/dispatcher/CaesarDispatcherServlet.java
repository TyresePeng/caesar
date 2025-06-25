package org.caesar.boot.start.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.caesar.boot.start.event.SystemErrorEvent;
import org.caesar.boot.start.properties.SystemErrorPropeties;
import org.caesar.common.enums.LogEnums;
import org.caesar.common.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 1、日志监控，外部请求日志
 * 如果做为下游服务，开启最优，用于链路追踪，异常排查
 * 2、监听系统异常发布到监听器,发布系统异常消息
 *
 * @author Peng.GUO
 */
public class CaesarDispatcherServlet extends DispatcherServlet {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
//
//    @Resource
//    private RequestLogPropeties requestLogPropeties;

    @Resource
    private SystemErrorPropeties systemErrorPropeties;

    public static final String CAESAR_DISPATCHER_SERVLET = "CaesarDispatcherServlet";

//    private static final Logger logger = LoggerFactory.getLogger(CAESAR_DISPATCHER_SERVLET);
//
//    private static final ObjectMapper mapper = new ObjectMapper();
//
//    @Override
//    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
//        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
//        //创建一个 json 对象，用来存放 http 日志信息
//        ObjectNode rootNode = mapper.createObjectNode();
//        rootNode.put(LogEnums.URI.getCode(), requestWrapper.getRequestURI());
//        rootNode.put(LogEnums.CLIENT_IP.getCode(), requestWrapper.getRemoteAddr());
//        rootNode.put(LogEnums.CREATE_TIME.getCode(), DateUtils.nowDefaultToString());
//        rootNode.set(LogEnums.REQUEST_HEADERS.getCode(), mapper.valueToTree(getRequestHeaders(requestWrapper)));
//        String method = requestWrapper.getMethod();
//        rootNode.put(LogEnums.METHOD.getCode(), method);
//        try {
//            super.doDispatch(requestWrapper, responseWrapper);
//        } finally {
//            rootNode.set(LogEnums.PARAMS.getCode(), mapper.valueToTree(requestWrapper.getParameterMap()));
//            rootNode.set(LogEnums.REQUEST_BODY.getCode(), mapper.readTree(requestWrapper.getContentAsByteArray()));
//            rootNode.put(LogEnums.STATUS.getCode(), responseWrapper.getStatus());
////            rootNode.set(LogEnums.RESPONSE.getCode(), mapper.readTree(responseWrapper.getContentAsByteArray()));
//            responseWrapper.copyBodyToResponse();
//            rootNode.set(LogEnums.RESPONSE_HEADERS.getCode(), mapper.valueToTree(getResponsetHeaders(responseWrapper)));
//            if (requestLogPropeties.getEnable()) {
//                logger.info(rootNode.toString());
//            }
//        }
//    }

    /**
     * <p> @description 监听系统异常发布到监听器</p>
     * <p> @author GuoPeng </p>
     * <p> @param [request, response, handler, ex] </p>
     * <p> @time 2023-02-17 16:14</p>
     */
    @Override
    protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) throws Exception {
        if (systemErrorPropeties.getEnable()) {
            SystemErrorEvent event = new SystemErrorEvent(ex);
            applicationEventPublisher.publishEvent(event);
        }
        return super.processHandlerException(request, response, handler, ex);
    }

    private Map<String, Object> getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;

    }

    private Map<String, Object> getResponsetHeaders(ContentCachingResponseWrapper response) {
        Map<String, Object> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }
}