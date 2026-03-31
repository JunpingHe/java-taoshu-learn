package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用主类
 * 这是整个应用的启动入口
 */
@SpringBootApplication
public class TaoshuApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaoshuApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  淘书学习项目启动成功！");
        System.out.println("  访问地址: http://localhost:8083");
        System.out.println("  H2 控制台: http://localhost:8083/h2-console");
        System.out.println("=================================================");
    }
}
