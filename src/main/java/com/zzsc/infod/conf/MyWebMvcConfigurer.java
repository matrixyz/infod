package com.zzsc.infod.conf;

import com.zzsc.infod.interceptors.RightsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RightsInterceptor()).addPathPatterns("/**").
                excludePathPatterns("/PubService/*").
                excludePathPatterns("/index.html");

        super.addInterceptors(registry);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置server虚拟路径，handler为jsp中访问的目录，locations为files相对应的本地路径
       /* registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:d:/test/datangaishen/upload/","file:d:/test/0360360/upload/","file:d:/test/i9360/upload/");
        super.addResourceHandlers(registry);*/
    }
   /* @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home_sdjflsjdf").setViewName("home");
        registry.addViewController("/sdklfjl_fskdfj").setViewName("home");
        registry.addViewController("/hello_sflkdjf").setViewName("hello");
        registry.addViewController("/login_slkfjdl").setViewName("login");

    }*/
}