package org.example;

/**
 * 主类 - 演示配置管理的最佳实践
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== 最佳实践：使用 ConfigManager ===");
        System.out.println();

        // 方式1：直接获取配置值
        String appName = ConfigManager.getInstance().getAppName();
        String version = ConfigManager.getInstance().getAppVersion();

        System.out.println("应用名称: " + appName);
        System.out.println("应用版本: " + version);
        System.out.println();

        // 方式2：在其他类中使用
        UserService userService = new UserService();
        userService.printUserInfo();
        System.out.println();

        // 方式3：获取单个配置项
        ConfigManager config = ConfigManager.getInstance();
        System.out.println("=== 单独获取配置项 ===");
        System.out.println("作者: " + config.get("app.author"));
        System.out.println("数据库URL: " + config.get("db.url"));
    }
}
