package sb.justwindow;

/**
 * API密钥配置类
 * 管理项目中使用的各种API密钥和配置信息
 * 在此处修改您的API密钥配置
 */
public class ApiKey {

    /**
     * 百度翻译API AppID
     * 获取方式：
     * 1. 访问 https://fanyi-api.baidu.com/
     * 2. 注册百度开发者账号
     * 3. 创建翻译应用获取APP_ID
     */
    public static final String BAIDU_TRANSLATE_APP_ID = "你的百度翻译API 软件ID";
    
    /**
     * 百度翻译API 密钥
     * 获取方式：
     * 1. 在百度翻译开放平台创建应用后获取
     * 2. 与APP_ID配套使用
     */
    public static final String BAIDU_TRANSLATE_SECURITY_KEY = "你的百度翻译API 密钥";
    
    /**
     * Spotify Client ID
     * 获取方式：
     * 1. 访问 https://developer.spotify.com/dashboard
     * 2. 创建Spotify应用获取Client ID
     */
    public static final String SPOTIFY_CLIENT_ID = "你的Spotify 软件ID";
    
    /**
     * Spotify Client Secret
     * 获取方式：
     * 1. 在Spotify开发者面板中创建应用后获取
     * 2. 与Client ID配套使用
     */
    public static final String SPOTIFY_CLIENT_SECRET = "你的Spotify Api 密钥";
    
    /**
     * Spotify重定向URI
     * 用于OAuth认证的回调地址
     * 需要在Spotify应用设置中配置相同的URI
     */
    public static final String SPOTIFY_REDIRECT_URI = "你的Spotify软件重定向URL";
    
    /**
     * 检查百度翻译API配置是否完整
     * @return 如果AppID和密钥都已配置则返回true
     */
    public static boolean isBaiduTranslateConfigured() {
        return !BAIDU_TRANSLATE_APP_ID.isEmpty() && !BAIDU_TRANSLATE_SECURITY_KEY.isEmpty();
    }

    /**
     * 检查Spotify API配置是否完整
     * @return 如果Client ID和Secret都已配置则返回true
     */
    public static boolean isSpotifyConfigured() {
        return !SPOTIFY_CLIENT_ID.isEmpty() && !SPOTIFY_CLIENT_SECRET.isEmpty();
    }
    
    /**
     * 百度翻译API配置信息
     * @return 包含AppID和密钥的数组，[0]为AppID，[1]为密钥
     */
    public static String[] getBaiduTranslateConfig() {
        return new String[]{BAIDU_TRANSLATE_APP_ID, BAIDU_TRANSLATE_SECURITY_KEY};
    }
    
    /**
     * SpotifyAPI配置信息
     * @return 包含Client配置的数组，[0]为Client ID，[1]为Client Secret，[2]为重定向URI
     */
    public static String[] getSpotifyConfig() {
        return new String[]{SPOTIFY_CLIENT_ID, SPOTIFY_CLIENT_SECRET, SPOTIFY_REDIRECT_URI};
    }
}
