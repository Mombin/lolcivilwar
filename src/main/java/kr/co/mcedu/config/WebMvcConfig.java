package kr.co.mcedu.config;

import kr.co.mcedu.config.web.RequestHandler;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(final InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(requestHandler());
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // url : /static/** -> classpath:/static/**
        registry.addResourceHandler("static/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * thymeleaf layout 사용 
     */
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    /**
     * thymeleaf security 사용 
     */
    @Bean
    public SpringSecurityDialect springSecurityDialect()  {
        return new SpringSecurityDialect();
    }

    @Bean
    public RequestHandler requestHandler() { return new RequestHandler(); }
}
