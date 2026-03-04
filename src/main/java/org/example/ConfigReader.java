package org.example;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    public void readConfig() {
        // 用 ConfigReader 类的类加载器读取
        InputStream is = ConfigReader.class.getClassLoader()
                            .getResourceAsStream("config.properties");

        if (is != null) {
            Properties props = new Properties();
            try {
                props.load(is);
                System.out.println("=== 用 ConfigReader 读取配置 ===");
                System.out.println("应用名称: " + props.getProperty("app.name"));
                System.out.println("应用版本: " + props.getProperty("app.version"));
            } catch (Exception e) {
                System.out.println("读取失败: " + e.getMessage());
            }
        }
    }
}
