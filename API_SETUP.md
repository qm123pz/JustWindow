# API密钥配置指南

## 快速开始

获取源码后，请按以下步骤配置API密钥：

### 1. 编辑API密钥配置

打开文件：`src/main/java/sb/justwindow/ApiKey.java`

### 2. 配置百度翻译API

```java
// 修改这两个字段
public static final String BAIDU_TRANSLATE_APP_ID = "您的百度翻译APP_ID";
public static final String BAIDU_TRANSLATE_SECURITY_KEY = "您的百度翻译密钥";
```

**获取方式：**
1. 访问 [百度翻译开放平台](https://fanyi-api.baidu.com/)
2. 注册百度开发者账号
3. 创建翻译应用获取APP_ID和密钥

### 3. 配置Spotify API

```java
// 修改这些字段
public static final String SPOTIFY_CLIENT_ID = "您的Spotify客户端ID";
public static final String SPOTIFY_CLIENT_SECRET = "您的Spotify客户端密钥";
public static final String SPOTIFY_REDIRECT_URI = "http://localhost:8080/callback";
```

**获取方式：**
1. 访问 [Spotify开发者控制台](https://developer.spotify.com/dashboard)
2. 创建Spotify应用
3. 获取Client ID和Client Secret
4. 在应用设置中添加重定向URI：`http://localhost:8080/callback`

## 配置检查

程序启动时会自动检查API配置状态，输出类似：

```
=== API配置状态 ===
百度翻译API: ✓ 已配置
Spotify API: ✗ 未配置

Spotify API配置缺失:
请在ApiKey.java中设置SPOTIFY_CLIENT_ID和SPOTIFY_CLIENT_SECRET
获取地址: https://developer.spotify.com/dashboard
==================
```

## 功能说明

- **翻译功能**：需要百度翻译API，支持中英互译和多语言识别
- **Spotify集成**：需要Spotify API，显示当前播放歌曲和歌词

## 注意事项

1. **不要提交API密钥**：配置完成后请勿将包含真实密钥的代码提交到公共仓库
2. **API额度**：百度翻译有免费额度限制，超出后需要付费
3. **网络连接**：API功能需要稳定的网络连接

## 故障排除

- 如果翻译显示"请配置百度翻译API"，请检查ApiKey.java中的百度配置
- 如果Spotify功能无法使用，请检查Spotify API配置和网络连接
- 程序启动时会在控制台显示详细的配置状态信息
