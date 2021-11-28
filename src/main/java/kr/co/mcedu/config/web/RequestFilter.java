package kr.co.mcedu.config.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        ReadableRequestWrapper readableRequestWrapper = new ReadableRequestWrapper((HttpServletRequest) request);
        chain.doFilter(readableRequestWrapper, response);
    }
}

