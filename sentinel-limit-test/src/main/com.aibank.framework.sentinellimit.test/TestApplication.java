package com.aibank.framework.sentinellimit.test;

import com.aibank.framework.sentinellimit.flow.proxy.PointcutAdvisorConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(PointcutAdvisorConfig.class)
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
