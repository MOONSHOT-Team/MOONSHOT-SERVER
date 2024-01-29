package org.moonshot.config;

import org.moonshot.filter.MDCFilter;
import org.moonshot.filter.ServletWrappingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!local")
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ServletWrappingFilter> secondFilter() {
        FilterRegistrationBean<ServletWrappingFilter> filterRegistrationBean = new FilterRegistrationBean<>(
                new ServletWrappingFilter());
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<MDCFilter> thirdFilter() {
        FilterRegistrationBean<MDCFilter> filterRegistrationBean = new FilterRegistrationBean<>(
                new MDCFilter());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
