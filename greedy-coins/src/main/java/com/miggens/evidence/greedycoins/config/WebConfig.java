package com.miggens.evidence.greedycoins.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {

        ClassLoaderTemplateResolver tr = new ClassLoaderTemplateResolver();

        tr.setPrefix("templates/");
        tr.setCacheable(false);
        tr.setSuffix(".html");
        tr.setTemplateMode("HTML");
        tr.setCharacterEncoding("utf-8");

        return tr;
    }


    @Bean
    public SpringTemplateEngine templateEngine() {

        SpringTemplateEngine ste = new SpringTemplateEngine();
        ste.setTemplateResolver(this.templateResolver());

        return ste;
    }

    @Bean
    public ViewResolver viewResolver() {

        ThymeleafViewResolver vr = new ThymeleafViewResolver();

        vr.setTemplateEngine( this.templateEngine() );
        vr.setCharacterEncoding("utf-8");

        return vr;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
    }
}
