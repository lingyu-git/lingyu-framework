package com.lingyu.dts.subscribe.config;

import cn.hutool.core.collection.CollUtil;
import com.lingyu.common.util.BinderUtil;
import com.lingyu.dts.subscribe.LingYuDTSConsumer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

public class DTSRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取配置
        MultiDTSProperties multiDTSProperties = BinderUtil.getBindResult(environment, "lingyu.aliyun.dts.subscribe", MultiDTSProperties.class);

        List<MultiDTSProperties.DTSProperty> properties = multiDTSProperties.getList();
        if (CollUtil.isNotEmpty(properties)) {
            for (MultiDTSProperties.DTSProperty property : properties) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(LingYuDTSConsumer.class);
                builder.addConstructorArgValue(property);
                registry.registerBeanDefinition(property.getName(), builder.getBeanDefinition());
            }
        }
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
