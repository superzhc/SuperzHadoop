package com.github.superzhc.geo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 命令行启动脚本：nohup java -jar superz-geo-web-0.2.0.jar > ./logs/geomesa_web/superz_geo_web.nohup.log 2>&1 &
 * @author superz
 * @create 2021/8/9 17:57
 */
@SpringBootApplication
public class GeoWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeoWebApplication.class, args);
    }
}
