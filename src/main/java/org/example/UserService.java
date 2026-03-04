package org.example;

/**
 * 用户服务类 - 演示如何在业务代码中使用配置
 */
public class UserService {

    public void printUserInfo() {
        // ✅ 推荐：统一用 ConfigManager 获取配置
        ConfigManager config = ConfigManager.getInstance();
        String appName = config.getAppName();
        String version = config.getAppVersion();

        System.out.println("=== UserService 打印用户信息 ===");
        System.out.println("当前应用: " + appName);
        System.out.println("版本号: " + version);

        // 获取不存在的配置，使用默认值
        String timeout = config.get("request.timeout", "5000");
        System.out.println("请求超时时间: " + timeout + "ms");
    }
}
