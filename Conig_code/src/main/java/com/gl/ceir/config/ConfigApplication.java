package com.gl.ceir.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.gl.ceir.config.model.ImeiMsisdnIdentity;
import com.gl.ceir.config.model.constants.ImeiStatus;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
@EnableFeignClients
@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class) 
public class ConfigApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ConfigApplication.class, args);
		// DeviceSnapShot deviceSnapShot =
		// context.getBean(DeviceSnapShotServiceImpl.class).get(13329001992380L);
		// logger.info("FIRST: " + deviceSnapShot.toString());
		//
		// deviceSnapShot =
		// context.getBean(DeviceSnapShotServiceImpl.class).get(13329001992380L);
		// logger.info("SECOND: "+deviceSnapShot.toString());
		// context.getBean(DeviceSnapShotServiceImpl.class).save(convertRequestToDeviceSnapShot());
	}
}
