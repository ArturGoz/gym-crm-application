package com.gca.storage;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class StorageInitializerPostProcessor implements BeanPostProcessor {
    private StorageInitializer storageInitializer;

    @Autowired
    public void setStorageInitializer(StorageInitializer storageInitializer) {
        this.storageInitializer = storageInitializer;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof StorageRegistry registry) {
            InitializedData data = storageInitializer.initializeData();
            registry.initStorages(data);
        }

        return bean;
    }
}
