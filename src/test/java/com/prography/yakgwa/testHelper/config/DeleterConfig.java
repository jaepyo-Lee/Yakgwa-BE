package com.prography.yakgwa.testHelper.config;

import com.prography.yakgwa.testHelper.RepositoryDeleter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DeleterConfig {
    @Bean
    RepositoryDeleter deleter(){
        return new RepositoryDeleter();
    }
}
