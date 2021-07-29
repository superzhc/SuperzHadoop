package com.github.superzhc.xxljob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

/**
 * @author superz
 * @create 2021/7/28 9:34
 */
@Configuration
public class ThirdBeanConfig {

    @Bean
    public SystemInfo systemInfo(){
        return new SystemInfo();
    }

    /*@Bean
    public HardwareAbstractionLayer hardware(){
        return systemInfo().getHardware();
    }

    @Bean
    public OperatingSystem operatingSystem(){
        return systemInfo().getOperatingSystem();
    }*/
}
