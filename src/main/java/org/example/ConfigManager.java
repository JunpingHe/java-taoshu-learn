package org.example;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置管理工具类 - 单例模式
 * 整个应用统一用这个类读取配置
 */
public class ConfigManager {

    // 单例实例
    private static ConfigManager instance;
    // 缓存的配置
    private Properties props;

    // 私有构造函数，防止外部创建实例
    private ConfigManager() {
        loadConfig();
    }

    /**
     * 获取单例实例
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * 加载配置文件
     */
    private void loadConfig() {
        // 只需要加载一次，这里用哪个类的类加载器都可以
        // 因为都是同一个类加载器
        InputStream is = ConfigManager.class.getClassLoader()
                            .getResourceAsStream("config.properties");

        if (is != null) {
            props = new Properties();
            try {
                props.load(is);
            } catch (Exception e) {
                throw new RuntimeException("加载配置文件失败", e);
            }
        } else {
            throw new RuntimeException("找不到配置文件 config.properties");
        }
    }

    /**
     * 获取配置值
     */
    public String get(String key) {
        return props.getProperty(key);
    }

    /**
     * 获取配置值，带默认值
     */
    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    /**
     * 获取应用名称
     */
    public String getAppName() {
        return get("app.name");
    }

    /**
     * 获取应用版本
     */
    public String getAppVersion() {
        return get("app.version");
    }
}
