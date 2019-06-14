package com.ice.universe.common.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * CoRsFilter 接口 -- 过滤器
 * @ClassName: CoRsFilter
 * @DetaTime 2018-10-29 09:57
 * @author ice
 */
@WebFilter(filterName = "CoRsFilter",urlPatterns = "/*")
public class CoRsFilter implements Filter {


    /**
     * 配置访问 Origin
     */
    private static final String[] Origin = {
            "http://localhost:3000", "http://127.0.0.1:3000"
    };
    /**
     * 配置允许跨域访问 AllowedOrigin
     */
    private static final Set<String> AllowedOrigin = new HashSet<>(Arrays.asList(Origin));


    /**
     * 初始化过滤器
     * @param filterConfig 过滤器配置
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 在业务处理器处理请求之前被调用
     * @param servletRequest Request对象
     * @param servletResponse Response对象
     * @param chain 过滤器链对象
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        //这里手动设置ASYNC_SUPPORTED，开启servlet3.0一步任务的支持
        servletRequest.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        /**
         * 由于doFilter传递进来的默认是ServletRequest对象,而设置业务相关参数和方法是在HttpServletRequest里
         * 所以我们需要强制转换一下,转换过后我们就可以进行设置了
         */
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        /**
         * 设置跨域
         * -- Access-Control-Allow-Origin : 为允许哪些Origin发起跨域请求. 这里设置为 "*" 表示允许所有, 通常设置为所有并不安全, 最好指定一下
         * -- Access-Control-Allow-Methods : 为允许请求的方法.
         * -- Access-Control-Max-Age : 表明在多少秒内, 不需要再发送预检验请求, 可以缓存该结果
         * -- Access-Control-Allow-Headers : 表明它允许跨域请求包含的content-type头, 这里设置的 x-requested-with , 表示ajax请求
         * -- Access-Control-Expose-Headers : 该字段可选. 跨域请求时, XMLHttpRequest对象的getResponseHeader()方法只能拿到6个基本字段: Cache-Control、Content-Language、Content-Type、Expires、Last-Modified、Pragma. 如果想拿到其他字段, 就必须在Access-Control-Expose-Headers里面指定.
         * -- Access-Control-Allow-Credentials 表示是否允许发送Cookie. 默认情况下, Cookie不包括在CORS请求之中. 设为true, 即表示服务器明确许可, Cookie可以包含在请求中, 一起发给服务器
         */
        /** 设置允许跨域 **/
        String origin = request.getHeader("Origin");
        if (AllowedOrigin.contains(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, AccessToken, Code");
            response.setHeader("Access-Control-Allow-Credentials","true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
        } else {
            response.addHeader("Access-Control-Allow-Origin", (origin == null) ? "*" : origin);
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, AccessToken, Code");
            response.setHeader("Access-Control-Allow-Credentials","true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
        }

        if(request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        /**
         * 因为有可能不止这一个过滤器,所以需要将所有的过滤器执行
         * 注意:这一行代码一定要写到最后
         */
        chain.doFilter(servletRequest, servletResponse);

    }

    /**
     * 摧毁过滤器
     */
    @Override
    public void destroy() {

    }

}
