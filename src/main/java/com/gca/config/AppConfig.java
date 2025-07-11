package com.gca.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ComponentScan(basePackages = {
        "com.gca.dao",
        "com.gca.service",
        "com.gca.facade",
        "com.gca.mapper",
        "com.gca.security"
})
@Import(BCryptPasswordEncoder.class)
@EnableAspectJAutoProxy
public class AppConfig {
}
