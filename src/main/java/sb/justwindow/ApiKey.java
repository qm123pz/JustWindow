package sb.justwindow;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * API密钥配置类
 * 从YAML配置文件中加载API密钥和配置信息
 */
public class ApiKey {
    private static Map<String, Object> config;

    static {
        loadConfig();
    }

    /**
     * 加载YAML配置文件
     */
    private static void loadConfig() {
        try (InputStream input = ApiKey.class.getClassLoader()
                .getResourceAsStream("api-config.yml")) {
            if (input == null) {
                System.err.println("警告: 找不到配置文件 api-config.yml");
                return;
            }
            Yaml yaml = new Yaml();
            config = yaml.load(input);
        } catch (Exception e) {
            System.err.println("加载配置文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取配置值（支持嵌套路径，如 "baidu.translate.app-id"）
     */
    @SuppressWarnings("unchecked")
    private static String getProperty(String path) {
        if (config == null) {
            return "";
        }

        // 首先检查环境变量（优先级最高）
        String envKey = path.toUpperCase().replace(".", "_").replace("-", "_");
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        // 然后从配置文件读取
        String[] keys = path.split("\\.");
        Object value = config;

        for (String key : keys) {
            if (value instanceof Map) {
                value = ((Map<String, Object>) value).get(key);
                if (value == null) {
                    return "";
                }
            } else {
                return "";
            }
        }

        return value != null ? value.toString() : "";
    }

    // 百度翻译API配置
    public static String getBaiduTranslateAppId() {
        return getProperty("baidu.translate.app-id");
    }

    public static String getBaiduTranslateSecurityKey() {
        return getProperty("baidu.translate.security-key");
    }

    // Spotify API配置
    public static String getSpotifyClientId() {
        return getProperty("spotify.client-id");
    }

    public static String getSpotifyClientSecret() {
        return getProperty("spotify.client-secret");
    }

    public static String getSpotifyRedirectUri() {
        return getProperty("spotify.redirect-uri");
    }

    /**
     * 检查百度翻译API配置是否完整
     */
    public static boolean isBaiduTranslateConfigured() {
        String appId = getBaiduTranslateAppId();
        String securityKey = getBaiduTranslateSecurityKey();
        return appId != null && !appId.isEmpty()
                && securityKey != null && !securityKey.isEmpty();
    }

    /**
     * 检查Spotify API配置是否完整
     */
    public static boolean isSpotifyConfigured() {
        String clientId = getSpotifyClientId();
        String clientSecret = getSpotifyClientSecret();
        return clientId != null && !clientId.isEmpty()
                && clientSecret != null && !clientSecret.isEmpty();
    }

    /**
     * 百度翻译API配置信息
     */
    public static String[] getBaiduTranslateConfig() {
        return new String[]{
                getBaiduTranslateAppId(),
                getBaiduTranslateSecurityKey()
        };
    }

    /**
     * Spotify API配置信息
     */
    public static String[] getSpotifyConfig() {
        return new String[]{
                getSpotifyClientId(),
                getSpotifyClientSecret(),
                getSpotifyRedirectUri()
        };
    }
}