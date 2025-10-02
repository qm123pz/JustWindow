# JustWindow - Windows 灵动岛

一个功能丰富的 Windows 灵动岛实现，灵感来源于 iPhone 的 Dynamic Island 设计。

## Dev-作者

### BiliBili - 药儿老师
### BiliBili - 药儿同学
- **( 其实是一个人嘻嘻 )**

**⭐ 如果这个项目对你有帮助，请给个Star支持一下！**


## 🌟 功能特性

### 🎨 核心特性
- **现代化UI设计** - 仿 iPhone 灵动岛的流畅动画和视觉效果 超可爱的Kaomoji
- **主题切换** - 支持深色/浅色主题，自动适配所有组件
- **智能闲置模式** - 1分钟无操作后自动进入闲置模式
- **多级菜单系统** - 直观的分层菜单结构，支持鼠标交互

### 📋 剪贴板管理
- **历史记录** - 自动保存最近10条剪贴板内容
- **持久化存储** - 数据保存至 `%USERPROFILE%\AppData\NightSky\Clipboard.json`
- **快速访问** - 点击即可重新复制历史内容
- **批量管理** - 支持单项删除和一键清空

### 🌐 实时翻译
- **智能识别** - 自动检测中文、英文和其他语言
- **双向翻译** - 中文↔英文，其他语言→中文
- **滚轮支持** - 支持滚轮查看长翻译内容
- **百度翻译API** - 集成百度翻译，提供准确的翻译结果

### ⏱️ 秒表功能
- **精确计时** - 毫秒级精度显示
- **开始/暂停** - 灵活的计时控制
- **重置功能** - 一键重置计时器
- **扩展布局** - 秒表模式下灵动岛自动扩展

### 🎵 Spotify 集成
- **实时同步** - 显示当前播放的歌曲信息
- **歌词显示** - 支持同步歌词滚动显示
- **专辑信息** - 展示歌曲名称、艺术家等详细信息
- **OAuth认证** - 安全的Spotify账户连接

### 🖼️ 截图工具
- **快捷截图** - 一键调用系统截图工具
- **便捷访问** - 集成在Quick菜单中

### 🔍 搜索功能
- **网页搜索** - 快速进行网络搜索
- **实时输入** - 流畅的搜索框体验

### ⚙️ 系统设置
- **开机启动** - 支持开机自动启动
- **主题控制** - 深色/浅色主题切换
- **透明度调节** - 自定义界面透明度
- **缩放控制** - 支持界面缩放调节

## 🚀 快速开始

### 环境要求
- **Java** 17 或更高版本
- **JavaFX** 运行时库
- **Windows** 10/11 操作系统

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/yourusername/JustWindow.git
   cd JustWindow
   ```

2. **配置API密钥**
   
   编辑 `src/main/java/sb/justwindow/ApiKey.java` 文件：
   
   ```java
   // 百度翻译API（翻译功能必需）
   public static final String BAIDU_TRANSLATE_APP_ID = "你的APP_ID";
   public static final String BAIDU_TRANSLATE_SECURITY_KEY = "你的密钥";
   
   // Spotify API（音乐功能可选）
   public static final String SPOTIFY_CLIENT_ID = "你的Client_ID";
   public static final String SPOTIFY_CLIENT_SECRET = "你的Client_Secret";
   public static final String SPOTIFY_REDIRECT_URI = "你的重定向URI";
   ```

3. **编译运行**
   ```bash
   # 使用 Maven
   mvn clean compile exec:java
   
   # 或使用 Gradle
   gradle run
   ```

### API 配置指南

- **详情请看API_SETUP.md**

###### 百度翻译API
1. 访问 [百度翻译开放平台](https://fanyi-api.baidu.com/)
2. 注册开发者账号并创建应用
3. 获取 APP_ID 和密钥
4. 每月有免费翻译额度

###### Spotify API
1. 访问 [Spotify开发者控制台](https://developer.spotify.com/dashboard)
2. 创建新应用
3. 获取 Client ID 和 Client Secret
4. 设置重定向URI：`http://127.0.0.1:8000/callback`

## 💡 使用说明

### 基本操作
- **鼠标悬停** - 底部区域悬停显示功能按钮 - 顶部区域悬停显示搜索栏
- **点击展开** - 点击不同按钮进入对应功能模式
- **鼠标离开** - 离开灵动岛区域自动收起
- **闲置触发** - 无操作1分钟后进入闲置模式

### 功能访问路径
```
灵动岛
├── Quick菜单 (鼠标悬停底部)
│   ├── 截图工具
│   ├── 剪贴板管理
│   ├── 秒表计时器
│   └── 实时翻译
├── 搜索功能 (鼠标悬停顶部)
├── 设置菜单 (点击设置按钮)
│   ├── 主题切换
│   ├── 开机启动
│   ├── 透明度调节
│   └── 其他选项
└── Spotify (音乐播放时自动显示)
```

### 快捷键说明
- **ESC** - 退出当前模式
- **滚轮** - 翻译界面滚动查看内容
- **点击** - 剪贴板历史项目重新复制

## 🛠️ 技术栈

- **核心框架**: JavaFX 17+
- **构建工具**: Maven/Gradle
- **JSON处理**: Gson
- **HTTP请求**: HttpURLConnection
- **文件存储**: JSON格式持久化
- **API集成**: 百度翻译API、Spotify Web API
- **系统交互**: Java AWT Robot

## 📁 项目结构

```
JustWindow/
├── src/main/java/sb/justwindow/
│   ├── DynamicIsland.java          # 主程序入口
│   ├── ApiKey.java                 # API密钥配置
│   ├── ConfigManager.java          # 配置管理
│   ├── SpotifyAPI.java            # Spotify集成
│   ├── SpotifyAuth.java           # Spotify认证
│   └── translate/                  # 翻译功能模块
│       ├── TransApi.java
│       ├── HttpGet.java
│       ├── MD5.java
│       └── TranslationResult.java
├── API_SETUP.md                   # API配置详细指南
└── README.md                      # 项目说明文档
```

## 🎯 功能演示

### 主界面
- 简洁的胶囊状设计，显示日期、时间和动态文本
- 支持深色/浅色主题自动切换

### 翻译功能
- 实时输入翻译，支持中英互译
- 长文本自动换行，滚轮滚动查看

### 剪贴板管理
- 历史记录自动保存，重启后依然可用
- 直观的列表界面，支持删除和清空操作

### 秒表计时
- 精确到毫秒的计时显示
- 简洁的开始/暂停/重置控制

## 🔧 配置文件

### 主配置文件
位置：`%USERPROFILE%\AppData\NightSky\config.json`
```json
{
  "autoStart": false,
  "isDarkTheme": true,
  "alpha": 0.85,
  "scale": 1.0,
  "bloomEnabled": true,
  "blurEnabled": false
}
```

### 剪贴板历史
位置：`%USERPROFILE%\AppData\NightSky\Clipboard.json`
```json
[
  "最新的剪贴板内容",
  "第二新的内容",
  "..."
]
```

## 🐛 故障排除

### 常见问题

**Q: 翻译功能显示"请配置百度翻译API"**
A: 请在 `ApiKey.java` 中配置正确的百度翻译APP_ID和密钥

**Q: Spotify功能无法使用**
A: 检查Spotify API配置和网络连接，确保重定向URI设置正确

**Q: 程序无法启动**
A: 确保安装了Java 17+和JavaFX运行时库

**Q: 界面显示异常**
A: 尝试删除配置文件重置设置，或检查系统DPI缩放设置

### 日志调试
程序运行时会在控制台输出详细的状态信息，可用于问题诊断。

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

### 开发环境搭建
1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request


## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 🙏 致谢

- 感谢 Apple 提供的 Dynamic Island 设计灵感
- 感谢百度翻译和Spotify提供的API服务
- 感谢所有贡献者和用户的支持

## 📞 联系方式

- **项目链接**: [https://github.com/yourusername/JustWindow](https://github.com/yourusername/JustWindow)
- **问题反馈**: [Issues](https://github.com/yourusername/JustWindow/issues)

---

**⭐ 如果这个项目对你有帮助，请给个Star支持一下！**