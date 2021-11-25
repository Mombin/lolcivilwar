package kr.co.mcedu.config.web;

import kr.co.mcedu.utils.SessionUtils;
import kr.co.mcedu.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

@Slf4j
public class RequestHandler implements WebRequestInterceptor {

    @Override
    public void preHandle(final WebRequest request) throws Exception {
        String servletPath = ((DispatcherServletWebRequest) request).getRequest().getServletPath();
        if (StringUtils.isEmpty(servletPath) || servletPath.startsWith("/static") || servletPath.startsWith(
                "/error") || servletPath.startsWith("/swagger")) {
            return;
        }

        log.info("url: {}, id: {}, ip: {}", servletPath, SessionUtils.getId(), SessionUtils.getIp());
    }

    @Override public void postHandle(final WebRequest request, final ModelMap model) throws Exception {

    }

    @Override public void afterCompletion(final WebRequest request, final Exception ex) throws Exception {

    }
}
