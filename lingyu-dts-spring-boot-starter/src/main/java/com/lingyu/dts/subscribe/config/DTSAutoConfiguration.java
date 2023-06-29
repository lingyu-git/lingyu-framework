package com.lingyu.dts.subscribe.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties({MultiDTSProperties.class})
@ConditionalOnProperty(name = "lingyu.aliyun.dts.subscribe.enable", havingValue = "true")
@Import({DTSRegistrar.class})
public class DTSAutoConfiguration {
}
