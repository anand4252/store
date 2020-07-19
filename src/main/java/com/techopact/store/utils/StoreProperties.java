package com.techopact.store.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "store")
public class StoreProperties {
//    private Integer viewLimit;
    private double percentageIncrease;
}
