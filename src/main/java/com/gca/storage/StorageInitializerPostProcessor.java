package com.gca.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class StorageInitializerPostProcessor implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(StorageInitializerPostProcessor.class);

    private StorageInitializer storageInitializer;

    @Autowired
    public void setStorageInitializer(StorageInitializer storageInitializer) {
        this.storageInitializer = storageInitializer;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof StorageRegistry registry) {
            logger.debug("Storage data initialization");
            InitializedData data = storageInitializer.initializeData();
            registry.initStorages(data);
            logger.debug("Storage data initialization completed");
        }

        return bean;
    }
}
