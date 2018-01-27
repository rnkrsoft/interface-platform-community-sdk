package com.rnkrsoft.platform.spring;

import com.rnkrsoft.platform.annotation.InterfaceSender;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/7/25.
 */
public class SyncSenderClasspathScanner extends ClassPathBeanDefinitionScanner {

    SyncSenderFactoryBean syncSenderFactoryBean;

    public void setSyncSenderFactoryBean(SyncSenderFactoryBean syncSenderFactoryBean) {
        this.syncSenderFactoryBean = syncSenderFactoryBean == null ? new SyncSenderFactoryBean() : syncSenderFactoryBean;
    }

    public SyncSenderClasspathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(InterfaceSender.class) {
        });

        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className = metadataReader.getClassMetadata().getClassName();
                return className.endsWith("package-info");
            }
        });
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No Sync Sender was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("Creating SyncSenderFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }
            String syncSenderInterfaceClassName = definition.getBeanClassName();

            definition.setBeanClass(this.syncSenderFactoryBean.getClass());
            definition.getPropertyValues().add("syncSenderInterface", syncSenderInterfaceClassName);
            if (logger.isDebugEnabled()) {
                logger.debug("Enabling autowire by type for MongoMapperFactoryBean with name '" + holder.getBeanName() + "'.");
            }
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            registerBeanDefinition(holder, getRegistry());
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            logger.warn("Skipping SyncSenderFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' SyncSenderInterface Bean already defined with the same name!");
            return false;
        }
    }

}
