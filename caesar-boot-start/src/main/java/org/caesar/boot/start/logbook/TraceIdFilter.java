package org.caesar.boot.start.logbook;

import lombok.extern.log4j.Log4j2;
import org.caesar.boot.start.transmittable.ContextThreadLocal;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
@Log4j2
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.put("traceId", ContextThreadLocal.getTraceId());
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
         log.error("TraceIdFilter error traceId:{}",ContextThreadLocal.getTraceId(),e);
        }finally {
            ContextThreadLocal.clear();
        }
    }
}
