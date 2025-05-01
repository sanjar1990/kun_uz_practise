//package com.example.config;
//
//import com.fasterxml.jackson.core.filter.TokenFilter;
//import jakarta.servlet.Filter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SecurityFilterConfig {
//    @Autowired
//    private JwtFilter tokenFilter;
//
//
//    @Bean
//    public FilterRegistrationBean<Filter> getFilterRegistrationBean() {
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(tokenFilter);
//        filterRegistrationBean.addUrlPatterns("/api/v1/profile/admin/*");
//        filterRegistrationBean.addUrlPatterns("/api/v1/region/admin/*");
//        filterRegistrationBean.addUrlPatterns("/api/v1/articleType/admin/*");
//        filterRegistrationBean.addUrlPatterns("/api/v1/category/admin/*");
//        filterRegistrationBean.addUrlPatterns("/api/v1/article/admin/*");
//        filterRegistrationBean.addUrlPatterns("/api/v1/tag/admin/*");
//        filterRegistrationBean.addUrlPatterns("/api/v1/emailHistory/admin/*");
//        filterRegistrationBean.addUrlPatterns("/api/v1/smsHistory/admin/*");
//        return filterRegistrationBean;
//    }
//}
