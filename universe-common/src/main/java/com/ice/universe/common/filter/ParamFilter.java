package com.ice.universe.common.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ParamFilter 接口 -- 过滤器
 * @ClassName: ParamFilter
 * @DetaTime 2018-10-29 09:57
 * @author ice
 */
@WebFilter(filterName = "ParamFilter",urlPatterns = "/*")
public class ParamFilter implements Filter {


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
         * 设置编码
         */
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.setContentType("text/plain;charset=utf-8");
        response.setContentType("application/json;charset=utf-8");
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
