package com.demo.iot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AppConfig {
    // Cấu hình này sẽ bật tính năng xử lý bất đồng bộ trong Spring.
}
