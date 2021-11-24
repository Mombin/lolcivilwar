package kr.co.mcedu.config;

import kr.co.mcedu.config.web.RequestHandler;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @Override
    protected void addInterceptors(final InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(requestHandler());
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // url : /static/** -> classpath:/static/**
        registry.addResourceHandler("static/**")
                .addResourceLocations("classpath:/static/");

        if ("local".equals(profile)) {
            registry.addResourceHandler("/swagger-ui/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                    .resourceChain(false);
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        if ("local".equals(profile)) {
            registry.addViewController("/swagger-ui/")
                    .setViewName("redirect:/swagger-ui/index.html");
            registry.addViewController("/swagger-ui")
                    .setViewName("redirect:/swagger-ui/index.html");
        }
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
