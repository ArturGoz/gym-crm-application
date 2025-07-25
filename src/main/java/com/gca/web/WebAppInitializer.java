package com.gca.web;

import com.gca.config.AppConfig;
import com.gca.config.PersistenceConfig;
import com.gca.config.WebConfig;
import com.gca.security.CachingRequestResponseFilter;
import com.gca.security.TransactionIdFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.util.EnumSet;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class, PersistenceConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected String getServletName() {
        return "mainDispatcher";
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        FilterRegistration.Dynamic cachingFilter = servletContext.addFilter(
                "cachingRequestResponseFilter", new CachingRequestResponseFilter());

        cachingFilter.setAsyncSupported(true);
        cachingFilter.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST), false, "/*"
        );

        FilterRegistration.Dynamic transactionIdFilter = servletContext.addFilter(
                "transactionIdFilter", new TransactionIdFilter());

        transactionIdFilter.setAsyncSupported(true);
        transactionIdFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
