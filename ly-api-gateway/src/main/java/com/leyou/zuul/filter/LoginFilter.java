package com.leyou.zuul.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.zuul.config.FilterProperties;
import com.leyou.zuul.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Srd
 * @date 2020/8/26  15:43
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * to classify a filter by type. Standard types in Zuul are "pre" for pre-routing filtering,
     * "route" for routing to an origin, "post" for post-routing filters, "error" for error handling.
     * We also support a "static" type for static responses see  StaticResponseFilter.
     * Any filterType made be created or added and run by calling FilterProcessor.runFilters(type)
     *
     * @return A String representing that type
     */
    @Override
    public String filterType() {
//        过滤类型：前置过滤
        return "pre";
    }

    /**
     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
//        过滤器执行顺序
        return 2;
    }

    /**
     * a "true" return from this method means that the run() method should be invoked
     *
     * @return true if the run() method should be invoked. false will not invoke the run() method
     */
    @Override
    public boolean shouldFilter() {
//        过滤器是否生效
//        获取context
        RequestContext context = RequestContext.getCurrentContext();
//        获取request
        HttpServletRequest request = context.getRequest();
//        获取请求路径uri
        String uri = request.getRequestURI();
//        判断白名单
        return !isAllowPath(uri);
    }

    /**
     * 判断白名单
     * @param uri
     * @return
     */
    private boolean isAllowPath(String uri) {
//        定义标记
        boolean flag = false;
//        获取忽略过滤集合
        List<String> allowPaths = this.filterProperties.getAllowPaths();
        for (String allowPath : allowPaths) {
//            判断
            if (uri.startsWith(allowPath)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * if shouldFilter() is true, this method will be invoked. this method is the core method of a ZuulFilter
     *
     * @return Some arbitrary artifact may be returned. Current implementation ignores it.
     * @throws ZuulException if an error occurs during execution.
     */
    @Override
    public Object run() throws ZuulException {
//        获取context
        RequestContext context = RequestContext.getCurrentContext();
//        获取request
        HttpServletRequest request = context.getRequest();
//        获取token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
//        校验
        try {
//            token不为空，执行校验
            if (StringUtils.isNotBlank(token)){
                JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            }else {
//                token为空，执行拦截
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(401);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
