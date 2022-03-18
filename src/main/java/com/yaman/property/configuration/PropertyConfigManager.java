package com.yaman.property.configuration;

import com.yaman.property.IPropertyManager;
import com.yaman.property.impl.ConnectionPropertyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyConfigManager {
    @Bean
    public IPropertyManager getPropertyManager(){
        IPropertyManager propertyManager = new ConnectionPropertyManager();
        propertyManager.loadProperties();
        return propertyManager;
    }
}
