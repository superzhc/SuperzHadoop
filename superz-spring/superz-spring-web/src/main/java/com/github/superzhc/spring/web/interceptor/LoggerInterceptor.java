package com.github.superzhc.spring.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.http.ResultT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author superz
 * @create 2021/12/7 13:39
 */
@Component
public class LoggerInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);

    private ObjectMapper mapper;

    public LoggerInterceptor() {
        mapper = new ObjectMapper();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求参数
        String method = request.getMethod();
        log.info("请求方式：" + method);
        // 获取请求体内容
        String body = null;
        if ("POST".equals(method)) {
            try (BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    sb.append(inputStr.trim());
                }
                body = sb.toString();
            }
        }
        log.info("请求体：" + body);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(mapper.writeValueAsString(ResultT.success()));
        }

        return false;
    }
}
