package com;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//事务开启也很容易， 在App.java中添加@EnableTransactionManagement注解
//@MapperScan("cn.milo.dao")
//@EnableTransactionManagement


@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ServletComponentScan
public class Application {

    public static void main(String[] args) {

          SpringApplication.run(Application.class, args);
    }

    @Autowired
    private Environment env;



    //显示声明CommonsMultipartResolver为mutipartResolver,为了上传
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setMaxInMemorySize(40960);
        resolver.setMaxUploadSize(200* 1024*1024);//上传文件大小 50M 50*1024*1024
        return resolver;
    }
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error-404.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error-404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-500.html");

            container.addErrorPages(error401Page, error404Page, error500Page);
        });
    }

    @Bean
    public org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory servletContainer() {
        org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory tomcat = new org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }
    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
//Connector监听的http的端口号
        connector.setPort(80);
        connector.setSecure(false);
//监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(443);
        return connector;
    }

}