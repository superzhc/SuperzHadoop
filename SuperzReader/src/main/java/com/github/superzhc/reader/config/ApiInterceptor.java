package com.github.superzhc.reader.config;

import com.alibaba.fastjson.JSON;
import com.github.superzhc.reader.common.HttpServletRequestEnhance;
import com.github.superzhc.reader.common.ResultT;
import com.github.superzhc.reader.entity.ApiConfig;
import com.github.superzhc.reader.entity.DatasourceConfig;
import com.github.superzhc.reader.executor.Executor;
import com.github.superzhc.reader.service.ApiConfigService;
import com.github.superzhc.reader.service.DatasourceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;

/**
 * API 拦截器
 *
 * @author superz
 * @create 2021/8/18 16:25
 */
@Component
@Slf4j
public class ApiInterceptor implements HandlerInterceptor {

    @Autowired
    private ApiConfigService apiConfigService;
    @Autowired
    private DatasourceConfigService datasourceConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletRequestEnhance request2=new HttpServletRequestEnhance(request);
        /* 通用响应内容 */
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // 跨域设置
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Headers", "Authorization");//这里很重要，要不然js header不能跨域携带  Authorization属性
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");

        // 对于数据服务发布的 API 接口，只支持 GET 方式，如果非 GET 方式，直接放行
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            response.sendError(404, "数据服务的请求不支持[" + request.getMethod() + "]方式");
            return false;
        }

        String servletPath = request.getServletPath();
        servletPath = servletPath.substring("/api/".length());

        ApiConfig apiConfig = apiConfigService.getByPath(servletPath);
        if (null == apiConfig) {
            response.sendError(404, "数据服务的请求[/api/" + servletPath + "]尚未注册");
            return false;
        }

        DatasourceConfig datasourceConfig = datasourceConfigService.getById(apiConfig.getId());
        Class clazz = Class.forName(datasourceConfig.getType());
        Constructor<Executor> c = clazz.getConstructor(String.class, String.class);
        Executor executor = c.newInstance(datasourceConfig.getConfig(), apiConfig.getConfig());
        ResultT t = executor.execute(request2.getParams());

        try (PrintWriter out = response.getWriter()) {
            out.append(JSON.toJSONString(t));
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
