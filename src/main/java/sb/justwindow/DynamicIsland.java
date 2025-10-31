// Decompiled with: CFR 0.152
// Class Version: 17
package sb.justwindow;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.MouseInfo;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class DynamicIsland extends Application {
    private DoubleProperty islandWidth = new SimpleDoubleProperty(280.0);
    private DoubleProperty islandHeight = new SimpleDoubleProperty(50.0);
    private DoubleProperty cornerRadius = new SimpleDoubleProperty(25.0);
    private StackPane root;
    private Pane islandContainer;
    private Shape islandShape;
    private Label timeLabel;
    private Label dateLabel;
    private Label weekLabel;
    private Label logoLabel;
    private Label kaomojiLabel;
    private TextField searchField;
    private Button searchButton;
    private HBox buttonContainer;
    private Button settingsBtn;
    private Button themeBtn;
    private Button quickBtn;
    private Button notifyBtn;
    private HBox themeSettingsContainer;
    private Button lightThemeBtn;
    private Button darkThemeBtn;
    private ToggleButton bloomToggle;
    private ToggleButton blurToggle;
    private Timeline breathingAnimation;
    private Timeline timeUpdateTimer;
    private double dragOffsetX;
    private double dragOffsetY;
    private boolean isDragging = false;
    private boolean isSearchMode = false;
    private boolean isButtonMode = false;
    private double originalX;
    private double originalY;
    private boolean isAnimating = false;
    private boolean isKaomojiMode = false;
    private Timeline currentTransitionAnimation = null;
    private PauseTransition currentPauseTransition = null;
    private boolean isThemeMode = false;
    private boolean isNoticeMode = false;
    private boolean isQuickMode = false;
    private Timeline quoteTimeline;
    private Text scrollingQuoteText;
    private double blurRadius = 12.0;
    private GaussianBlur backgroundBlur;
    private Color darkBg = Color.rgb(30, 30, 30, 0.85);
    private Color lightBg = Color.rgb(240, 240, 240, 0.85);
    private Color darkText = Color.WHITE;
    private Color lightText = Color.BLACK;
    private boolean isOptionMode = false;
    private HBox optionSettingsContainer;
    private ToggleButton autoStartToggle;
    private ToggleButton hideTaskbarToggle;
    private Label alphaLabel;
    private ConfigManager configManager;
    private HBox noticeSettingsContainer;
    private HBox quickSettingsContainer;
    private ToggleButton spotifyBtn;
    private boolean isMusicModeEnabled = false;
    private boolean isClipboardMode = false;
    private HBox clipboardContainer;
    private javafx.scene.control.ScrollPane clipboardScrollPane;
    private javafx.scene.layout.VBox clipboardItemsContainer;
    private List<String> clipboardHistory;
    private Timeline clipboardMonitorTimer;
    private String lastClipboardContent = "";
    private String clipboardFilePath;
    private SpotifyAPI spotifyAPI;
    private Timeline spotifyUpdateTimer;
    private Timeline idleTimer;
    private Timeline textRotationTimer;
    private boolean isIdleMode = false;
    private Label idleTextLabel;
    private Timeline stopwatchTimer;
    private long stopwatchStartTime = 0;
    private long stopwatchElapsedTime = 0;
    private boolean isStopwatchRunning = false;
    private boolean isStopwatchMode = false;
    private Label stopwatchTimeLabel;
    private Button stopwatchStartStopBtn;
    private Button stopwatchResetBtn;
    private VBox stopwatchContainer;
    private boolean isTranslateMode = false;
    private HBox translateContainer;
    private TextField translateInputField;
    private TextArea translateOutputArea;
    private javafx.scene.shape.Polygon translateArrow;
    private sb.justwindow.translate.TransApi baiduTransApi;
    private Timeline translationDebounceTimer;
    private Label spotifyTrackLabel;
    private Label spotifyArtistLabel;
    private Label spotifyStatusLabel;
    private Label spotifyLyricLine1;
    private Label spotifyLyricLine2;
    private Label spotifyLyricLine3;
    private int currentLyricIndex = 0;
    private List<SpotifyAPI.LyricLine> syncedLyrics = new ArrayList<SpotifyAPI.LyricLine>();
    private String currentTrackId = "";
    private final String[] kaomojis = new String[]{"(⌒▽⌒)☆", "(O_o)", "(>_<)", "(^_−)−☆", "d=(´▽｀)=b", "(ง •̀_•́)ง", "(•_•)", "( •_•)>⌐■-■", "(⌐■_■)", "¯\\_(\u30c4)_/¯", "(ಥ_ಥ)", "(づ｡◕‿‿◕｡)づ"};
    private final String[] islandtext = new String[]{
            "当香蕉君决定与哲学在路灯下论证皮带的必要性。",
            "我试图用雪豹的悲悯解构这盘锅包肉的熵增。",
            "在赛博观音的注视下，我选择将CPU超频成舍利子。",
            "当乌鸦像写字台，因为我的简历在异次元被折叠。",
            "用七彩祥云论证了半生，才发现悟空交的是五险一金。",
            "当你的意识流在元宇宙里卡成了PPT格式的梵高。",
            "我向黑洞提交了辞职信，它用奇点回复了我一串乱码。",
            "用“典”这个字为我的毕生创作举行了量子葬礼。",
            "当你的电子木鱼在区块链上敲出了“404 Not Found”。",
            "把沉默螺旋进洗衣机，洗出了一缸子薛定谔的猫毛。",
            "在人生的高速公路上，我选择用拖把当雨刷器。",
            "用三体人的逻辑为我的拖延症申请了二向箔打击。",
            "当你的多巴胺穿搭被存在主义做成了二维码馅饼。",
            "我试图用海鸥的语境翻译出这包辣条的宇宙常数。",
            "在平行宇宙的我，正用你的答辩论文解构相对论。",
            "把月光宝盒刷成了安卓系统，从此般若波罗蜜闪退。",
            "当你的毕生所学在弹幕里凝华成了一朵“哈哈哈”。",
            "我用意大利面拌42号混凝土，论证了章鱼的纬度。",
            "在虚空里垂钓，钓竿的另一头是正在摸鱼的我。",
            "将你的誓言放入回收站，系统提示：不是有效梗。",
            "在模拟器里寻找模拟器的我，触发了递归性尴尬。",
            "用“哈哈哈”的熵值，成功熔断了AI的泪腺。",
            "当你的孤独在群聊里完成了它的赛博升维。",
            "把人生的主线程阻塞在“正在输入…”的量子态中。",
            "我向Siri提问存在主义，它推荐了附近的奶茶店。",
            "用一卷透明胶带，封印了多元宇宙的所有裂痕。",
            "当你的悲伤在数据流里被压缩成了一枚表情包。",
            "在黑洞的视界上，用涂鸦证明了我曾到此一游。",
            "将你的承诺加载进RAM，系统提示：校验和错误。",
            "我用人生的边角料，拼凑出一具会呼吸的梗。",
            "当你的灵魂在404页面找到了永恒的宁静。",
            "用一行/kill @e结束了这局名为生活的MineCraft。",
            "在意识的底层，我找到了出厂设置的灰色按钮。",
            "当你的爱情被证明是大型语言模型的一次幻觉。",
            "我用道德经在微博超话里完成了机械飞升。",
            "把星空卷成葱饼，佐以尼采的眼泪一同咽下。",
            "当你的理想在二手平台被标价“可小刀”。",
            "我用你撤回的消息，搭建了一座巴别图书馆。",
            "在人生的自动售货机，我投下了最后一枚emoji。",
            "当你的存在被简化为数据库里的一行NULL值。",
            "用一行console.log(“我”)向虚空输出着自指。",
            "将你的背影放入回收站，却找不到清空选项。",
            "当你的幽默感在虫洞中丢失了所有的参照系。",
            "我用相对论成功论证了拖延症的合法性与美。",
            "在元宇宙的广场上，我张贴着自己的寻人启事。",
            "当你的内心戏被剪辑成了抖音的三分钟电影。",
            "我用康德的理论为你外卖里的苍蝇做了辩护。",
            "把时间折成纸飞机，投向名为“下一刻”的深渊。",
            "当你的自我认知在无数个滤镜间发生了坍缩。",
            "我用一生的流量，下载了一首未完成的挽歌。",
            "这是一个彩蛋！"
    };

    @Override
    public void start(Stage primaryStage) {
        this.configManager = ConfigManager.getInstance();
        this.createUI();
        this.setupStage(primaryStage);
        this.setupAnimations();
        this.setupDragHandlers();
        this.setupHoverInteractions();
        Scene scene = new Scene(this.root, 400.0, 100.0);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        try {
            Image icon = new Image(this.getClass().getResourceAsStream("/sb/justwindow/img/Logo.png"));
            primaryStage.getIcons().add(icon);
        }
        catch (Exception e) {
        }
        primaryStage.show();
        if (this.configManager.getConfig().hideTaskbar) {
            Platform.runLater(() -> WindowsUtils.hideFromTaskbar(primaryStage));
        }
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.originalX = (screenBounds.getWidth() - 400.0) / 2.0;
        this.originalY = screenBounds.getHeight() / 2333.0;
        primaryStage.setX(this.originalX);
        primaryStage.setY(this.originalY);
        this.startBreathingAnimation();
        this.startTimeUpdate();
        this.updateLogoAndQuote();
        this.switchToNextQuote();
        this.applyTheme(this.configManager.getConfig().isDarkTheme);
        this.toggleBloom(this.configManager.getConfig().bloomEnabled);
        this.toggleBlur(this.configManager.getConfig().blurEnabled);
        this.applyAlpha(this.configManager.getConfig().alpha);
        this.initClipboardMonitor();
        this.initIdleTimer();
        this.createStopwatchUI();
        this.createTranslateUI();
        this.initBaiduTranslateApi();
    }

    private void setupStage(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        Rectangle clip = new Rectangle(400.0, 100.0);
        clip.setArcWidth(20.0);
        clip.setArcHeight(20.0);
        this.root.setClip(clip);
    }

    private void createUI() {
        this.root = new StackPane();
        this.root.setStyle("-fx-background-color: transparent;");
        this.islandContainer = new Pane();
        this.islandContainer.setPrefSize(400.0, 100.0);
        this.createIslandShape();
        this.createTimeLabel();
        this.createDateLabel();
        this.createWeekLabel();
        this.createLogoLabel();
        this.createKaomojiLabel();
        this.createInteractiveComponents();
        this.setupQuoteAnimation();
        this.createSpotifyUI();
        this.islandContainer.getChildren().addAll((Node[])new Node[]{this.islandShape, this.timeLabel, this.dateLabel, this.weekLabel, this.logoLabel, this.kaomojiLabel, this.scrollingQuoteText});
        this.spotifyTrackLabel.toFront();
        this.spotifyArtistLabel.toFront();
        this.spotifyStatusLabel.toFront();
        this.spotifyLyricLine1.toFront();
        this.spotifyLyricLine2.toFront();
        this.spotifyLyricLine3.toFront();
        this.root.getChildren().add(this.islandContainer);
        this.setupLayout();
    }

    private void createInteractiveComponents() {
        this.searchField = new TextField();
        this.searchField.setPromptText("Type URL or search content...");
        this.searchField.setStyle("-fx-background-color: rgba(60,60,60,0.95); -fx-text-fill: white; -fx-border-color: #888; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 5 10;");
        this.searchField.setPrefWidth(200.0);
        this.searchField.setVisible(false);
        Glow searchGlow = new Glow(0.8);
        this.searchField.setEffect(searchGlow);
        this.searchButton = new Button("🔍");
        this.searchButton.setStyle("-fx-background-color: #0078d4; -fx-text-fill: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 12px; -fx-padding: 5 10;");
        this.searchButton.setVisible(false);
        this.searchButton.setOnAction(e -> this.performSearch());
        Glow buttonGlow = new Glow(0.6);
        this.searchButton.setEffect(buttonGlow);
        this.searchField.setOnAction(e -> this.performSearch());
        this.buttonContainer = new HBox(8.0);
        this.buttonContainer.setVisible(false);
        this.settingsBtn = this.createToolButton("⚙", "Option");
        this.themeBtn = this.createToolButton("🎨", "Theme");
        this.quickBtn = this.createToolButton("🚀", "Quick");
        this.notifyBtn = this.createToolButton("🔔", "Notice");
        this.themeBtn.setOnAction(e -> {
            if (this.isButtonMode) {
                this.showThemeSettings();
            }
        });
        this.buttonContainer.getChildren().addAll((Node[])new Node[]{this.settingsBtn, this.themeBtn, this.quickBtn, this.notifyBtn});
        this.createThemeSettingsUI();
        this.islandContainer.getChildren().addAll((Node[])new Node[]{this.searchField, this.searchButton, this.buttonContainer, this.themeSettingsContainer});
        this.settingsBtn.setOnAction(e -> {
            if (this.isButtonMode) {
                this.showOptionSettings();
            }
        });
        this.notifyBtn.setOnAction(e -> {
            if (this.isButtonMode) {
                this.showNoticeSettings();
            }
        });
        this.quickBtn.setOnAction(e -> {
            if (this.isButtonMode) {
                this.showQuickSettings();
            }
        });
        this.createOptionSettingsUI();
        this.islandContainer.getChildren().add(this.optionSettingsContainer);
        this.createNoticeSettingsUI();
        this.islandContainer.getChildren().add(this.noticeSettingsContainer);
        this.createQuickSettingsUI();
        this.islandContainer.getChildren().add(this.quickSettingsContainer);
        this.createClipboardUI();
        this.islandContainer.getChildren().add(this.clipboardContainer);
    }

    private void createSpotifyUI() {
        this.spotifyAPI = new SpotifyAPI();
        this.spotifyTrackLabel = new Label("");
        this.spotifyTrackLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-text-overrun: ellipsis;");
        this.spotifyTrackLabel.setMaxWidth(120.0);
        this.spotifyTrackLabel.setVisible(false);
        this.spotifyArtistLabel = new Label("");
        this.spotifyArtistLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-font-size: 8px; -fx-text-overrun: ellipsis;");
        this.spotifyArtistLabel.setMaxWidth(120.0);
        this.spotifyArtistLabel.setVisible(false);
        this.spotifyStatusLabel = new Label("");
        this.spotifyStatusLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 9px; -fx-font-style: italic;");
        this.spotifyStatusLabel.setMaxWidth(260.0);
        this.spotifyStatusLabel.setVisible(false);
        this.spotifyLyricLine1 = new Label("");
        this.spotifyLyricLine1.setStyle("-fx-text-fill: rgba(255,255,255,0.4); -fx-font-size: 7px;");
        this.spotifyLyricLine1.setMaxWidth(130.0);
        this.spotifyLyricLine1.setVisible(false);
        this.spotifyLyricLine1.setOpacity(0.4);
        this.spotifyLyricLine2 = new Label("");
        this.spotifyLyricLine2.setStyle("-fx-text-fill: white; -fx-font-size: 8px; -fx-font-weight: bold;");
        this.spotifyLyricLine2.setMaxWidth(130.0);
        this.spotifyLyricLine2.setVisible(false);
        this.spotifyLyricLine2.setOpacity(1.0);
        this.spotifyLyricLine3 = new Label("");
        this.spotifyLyricLine3.setStyle("-fx-text-fill: rgba(255,255,255,0.4); -fx-font-size: 7px;");
        this.spotifyLyricLine3.setMaxWidth(130.0);
        this.spotifyLyricLine3.setVisible(false);
        this.spotifyLyricLine3.setOpacity(0.4);
        this.islandContainer.getChildren().addAll((Node[])new Node[]{this.spotifyTrackLabel, this.spotifyArtistLabel, this.spotifyStatusLabel, this.spotifyLyricLine1, this.spotifyLyricLine2, this.spotifyLyricLine3});
        this.spotifyUpdateTimer = new Timeline(new KeyFrame(Duration.millis(333.0), e -> new Thread(() -> {
            SpotifyAPI.SpotifyTrack track = this.spotifyAPI.getCurrentTrack();
            Platform.runLater(() -> this.updateSpotifyDisplay(track));
        }).start(), new KeyValue[0]));
        this.spotifyUpdateTimer.setCycleCount(-1);
    }

    private void createIslandShape() {
        Rectangle rect = new Rectangle();
        rect.widthProperty().bind(this.islandWidth);
        rect.heightProperty().bind(this.islandHeight);
        
        this.cornerRadius.addListener((obs, oldVal, newVal) -> {
            rect.setArcWidth(newVal.doubleValue());
            rect.setArcHeight(newVal.doubleValue());
        });
        
        rect.setArcWidth(this.cornerRadius.get());
        rect.setArcHeight(this.cornerRadius.get());
        rect.setFill(Color.rgb(30, 30, 30, 0.85));
        
        DropShadow shadow = new DropShadow();
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setColor(Color.rgb(0, 0, 0, 0.4));
        shadow.setRadius(15.0);
        shadow.setSpread(0.3);
        shadow.setOffsetX(0.0);
        shadow.setOffsetY(0.0);
        rect.setEffect(shadow);
        this.backgroundBlur = new GaussianBlur(0.0);
        this.islandShape = rect;
    }

    private void toggleBlur(boolean enable) {
        this.configManager.getConfig().blurEnabled = enable;
        this.configManager.saveConfig();
        
        Rectangle rect = (Rectangle)this.islandShape;
        
        if (enable) {
            DropShadow enhancedShadow = new DropShadow();
            enhancedShadow.setBlurType(BlurType.GAUSSIAN);
            enhancedShadow.setColor(Color.rgb(0, 0, 0, 0.7));
            enhancedShadow.setRadius(30.0);
            enhancedShadow.setSpread(0.15);
            enhancedShadow.setOffsetX(0.0);
            enhancedShadow.setOffsetY(5.0);
            enhancedShadow.setInput(this.backgroundBlur);
            
            KeyValue blurValue = new KeyValue(this.backgroundBlur.radiusProperty(), this.blurRadius, new CustomInterpolator(Animation::easeInOutBack));
            KeyFrame blurFrame = new KeyFrame(Duration.millis(500.0), blurValue);
            Timeline blurAnimation = new Timeline(blurFrame);
            
            Color currentColor = (Color)rect.getFill();
            Color targetColor = Color.color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 0.75);
            FillTransition opacityTransition = new FillTransition(Duration.millis(300.0), rect, currentColor, targetColor);
            opacityTransition.setInterpolator(new CustomInterpolator(Animation::easeInOutBack));
            
            ParallelTransition parallelTransition = new ParallelTransition(blurAnimation, opacityTransition);
            rect.setEffect(enhancedShadow);
            parallelTransition.play();
        } else {
            KeyValue blurValue = new KeyValue(this.backgroundBlur.radiusProperty(), 0.0, new CustomInterpolator(Animation::easeInOutBack));
            KeyFrame blurFrame = new KeyFrame(Duration.millis(500.0), blurValue);
            Timeline blurAnimation = new Timeline(blurFrame);
            
            Color currentColor = (Color)rect.getFill();
            Color targetColor = Color.color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 0.85);
            FillTransition opacityTransition = new FillTransition(Duration.millis(300.0), rect, currentColor, targetColor);
            opacityTransition.setInterpolator(new CustomInterpolator(Animation::easeInOutBack));
            
            DropShadow normalShadow = new DropShadow();
            normalShadow.setBlurType(BlurType.GAUSSIAN);
            normalShadow.setColor(Color.rgb(0, 0, 0, 0.4));
            normalShadow.setRadius(15.0);
            normalShadow.setSpread(0.3);
            normalShadow.setOffsetX(0.0);
            normalShadow.setOffsetY(0.0);
            
            ParallelTransition parallelTransition = new ParallelTransition(blurAnimation, opacityTransition);
            parallelTransition.setOnFinished(e -> rect.setEffect(normalShadow));
            parallelTransition.play();
        }
    }

    private void createTimeLabel() {
        this.timeLabel = new Label();
        this.timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.timeLabel.setText(currentTime.format(formatter));
    }

    private void createDateLabel() {
        this.dateLabel = new Label();
        this.dateLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 11px;");
        this.updateDate();
    }

    private void createWeekLabel() {
        this.weekLabel = new Label();
        this.weekLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 11px;");
        this.updateWeek();
    }

    private void createLogoLabel() {
        this.logoLabel = new Label();
        this.logoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
    }

    private void createKaomojiLabel() {
        this.kaomojiLabel = new Label();
        this.kaomojiLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        this.kaomojiLabel.setVisible(false);
    }

    private void createThemeSettingsUI() {
        this.themeSettingsContainer = new HBox(8.0);
        this.themeSettingsContainer.setAlignment(Pos.CENTER_LEFT);
        this.themeSettingsContainer.setVisible(false);
        this.lightThemeBtn = this.createStyledButton("Light");
        this.darkThemeBtn = this.createStyledButton("Dark");
        this.bloomToggle = this.createStyledToggleButton("Glow");
        this.bloomToggle.setSelected(this.configManager.getConfig().bloomEnabled);
        this.blurToggle = this.createStyledToggleButton("Shadow");
        this.blurToggle.setSelected(this.configManager.getConfig().blurEnabled);
        this.themeSettingsContainer.getChildren().addAll((Node[])new Node[]{this.lightThemeBtn, this.darkThemeBtn, this.bloomToggle, this.blurToggle});
        this.lightThemeBtn.setOnAction(e -> this.applyTheme(false));
        this.darkThemeBtn.setOnAction(e -> this.applyTheme(true));
        this.bloomToggle.selectedProperty().addListener((obs, oldVal, newVal) -> this.toggleBloom((boolean)newVal));
        this.blurToggle.selectedProperty().addListener((obs, oldVal, newVal) -> this.toggleBlur((boolean)newVal));
    }

    private void createOptionSettingsUI() {
        this.optionSettingsContainer = new HBox(8.0);
        this.optionSettingsContainer.setAlignment(Pos.CENTER_LEFT);
        this.optionSettingsContainer.setVisible(false);
        this.autoStartToggle = this.createStyledToggleButton("AutoStart");
        this.autoStartToggle.setSelected(this.configManager.getConfig().autoStart);
        this.hideTaskbarToggle = this.createStyledToggleButton("HideTaskbar");
        this.hideTaskbarToggle.setSelected(this.configManager.getConfig().hideTaskbar);
        this.alphaLabel = this.createValueLabel("Alpha", this.configManager.getConfig().alpha);
        this.optionSettingsContainer.getChildren().addAll((Node[])new Node[]{this.autoStartToggle, this.hideTaskbarToggle, this.alphaLabel});
        this.autoStartToggle.selectedProperty().addListener((obs, oldVal, newVal) -> this.configManager.setAutoStart((boolean)newVal));
        this.hideTaskbarToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            this.configManager.setHideTaskbar((boolean)newVal);
            this.applyTaskbarVisibility((boolean)newVal);
        });
    }

    private Label createValueLabel(String name, double value) {
        Label label = new Label(String.format("%s: %.2f", name, value));
        label.setStyle("-fx-background-color: rgba(80,80,80,0.95); -fx-text-fill: white; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 5 10; -fx-font-size: 12px; -fx-cursor: hand;");
        label.setOnMouseEntered(ev -> label.setStyle("-fx-background-color: rgba(100,100,100,0.95); -fx-text-fill: white; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 5 10; -fx-font-size: 12px; -fx-cursor: hand;"));
        label.setOnMouseExited(ev -> label.setStyle("-fx-background-color: rgba(80,80,80,0.95); -fx-text-fill: white; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 5 10; -fx-font-size: 12px; -fx-cursor: hand;"));
        label.setOnScroll(ev -> {
                                    double delta = ev.getDeltaY() > 0.0 ? 0.05 : -0.05;
            if (name.equals("Alpha")) {
                double newValue;
                this.configManager.getConfig().alpha = newValue = Math.max(0.1, Math.min(1.0, this.configManager.getConfig().alpha + delta));
                this.configManager.saveConfig();
                label.setText(String.format("Alpha: %.2f", newValue));
                this.applyAlpha(newValue);
            }
            ev.consume();
        });
        return label;
    }

    private void applyAlpha(double alpha) {
        Color currentColor = (Color)((Rectangle)this.islandShape).getFill();
        Color newColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alpha);
        ((Rectangle)this.islandShape).setFill(newColor);
        if (this.isDarkMode()) {
            this.darkBg = Color.rgb(30, 30, 30, alpha);
        } else {
            this.lightBg = Color.rgb(240, 240, 240, alpha);
        }
    }

        private void createQuickSettingsUI() {
        this.quickSettingsContainer = new HBox(8.0);
        this.quickSettingsContainer.setAlignment(Pos.CENTER_LEFT);
        this.quickSettingsContainer.setVisible(false);

        Button screenshotBtn = this.createToolButton("✂", "Screenshot");
        Button clipboardBtn = this.createToolButton("📋", "Clipboard");
        Button stopwatchBtn = this.createToolButton("⏱", "Stopwatch");
        Button translateBtn = this.createToolButton("文A", "Translate");

        screenshotBtn.setOnAction(e -> {
            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_PRINTSCREEN);
                robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        });

                clipboardBtn.setOnAction(e -> {
            if (this.isQuickMode) {
                this.showClipboardMode();
            }
        });
        stopwatchBtn.setOnAction(e -> this.showStopwatchMode());
        translateBtn.setOnAction(e -> this.showTranslateMode());

        this.quickSettingsContainer.getChildren().addAll(screenshotBtn, clipboardBtn, stopwatchBtn, translateBtn);
    }

    private void showQuickSettings() {
        if (this.isAnimating) {
            return;
        }
        this.isQuickMode = true;
        this.isButtonMode = false;
        this.isAnimating = true;
        this.hideSpotifyTemporarily();
        this.scrollingQuoteText.setVisible(false);
        this.animateFadeOut(this.buttonContainer, 150);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            this.moveToMousePosition(false);
            double targetWidth = 187.0;
            Timeline expandAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo))));
            expandAnimation.setOnFinished(ev -> {
                this.quickSettingsContainer.toFront();
                this.quickSettingsContainer.setVisible(true);
                this.animateFadeInNode(this.quickSettingsContainer, 200);
                PauseTransition unlockDelay = new PauseTransition(Duration.millis(200.0));
                unlockDelay.setOnFinished(evv -> {
                    this.isAnimating = false;
                });
                unlockDelay.play();
            });
            expandAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void hideQuickSettings() {
        if (!this.isQuickMode) {
            return;
        }
        this.isQuickMode = false;
        this.isAnimating = true;
        this.animateFadeOut(this.quickSettingsContainer, 200);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(200.0));
        fadeOutDelay.setOnFinished(e -> {
            this.quickSettingsContainer.setVisible(false);
            Timeline shrinkAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo))));
            shrinkAnimation.setOnFinished(ev -> this.showNormalContentWithAnimation());
            shrinkAnimation.play();
        });
        fadeOutDelay.play();
    }


        private void applyTaskbarVisibility(boolean hide) {
        Stage stage = (Stage)this.root.getScene().getWindow();
        if (hide) {
            WindowsUtils.hideFromTaskbar(stage);
        } else {
            WindowsUtils.showInTaskbar(stage);
        }
    }

        private void showOptionSettings() {
        if (this.isAnimating) {
            return;
        }
        this.isOptionMode = true;
        this.isButtonMode = false;
        this.isAnimating = true;
        this.hideSpotifyTemporarily();
        this.scrollingQuoteText.setVisible(false);
        this.animateFadeOut(this.buttonContainer, 150);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            this.moveToMousePosition(false);
            double targetWidth = 290.0;
            Timeline expandAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo))));
            expandAnimation.setOnFinished(ev -> {
                this.optionSettingsContainer.toFront();
                this.optionSettingsContainer.setVisible(true);
                this.animateFadeInNode(this.optionSettingsContainer, 200);
                PauseTransition unlockDelay = new PauseTransition(Duration.millis(200.0));
                unlockDelay.setOnFinished(evv -> {
                    this.isAnimating = false;
                });
                unlockDelay.play();
            });
            expandAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void hideOptionSettings() {
        if (!this.isOptionMode) {
            return;
        }
        this.isOptionMode = false;
        this.isAnimating = true;
        this.animateFadeOut(this.optionSettingsContainer, 200);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(200.0));
        fadeOutDelay.setOnFinished(e -> {
            this.optionSettingsContainer.setVisible(false);
            Timeline shrinkAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo))));
            shrinkAnimation.setOnFinished(ev -> this.showNormalContentWithAnimation());
            shrinkAnimation.play();
        });
        fadeOutDelay.play();
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: rgba(80,80,80,0.95); -fx-text-fill: white; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 5 10; -fx-font-size: 12px;");
        return btn;
    }

    private ToggleButton createStyledToggleButton(String text) {
        ToggleButton btn = new ToggleButton(text);
        String baseStyle = "-fx-background-color: rgba(80,80,80,0.95); -fx-text-fill: white; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 5 10; -fx-font-size: 12px;";
        String selectedStyle = "-fx-background-color: #0078d4; -fx-text-fill: white; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 5 10; -fx-font-size: 12px;";
        btn.setStyle(baseStyle);
        btn.selectedProperty().addListener((obs, oldVal, newVal) -> btn.setStyle(newVal != false ? selectedStyle : baseStyle));
        return btn;
    }

    private Button createToolButton(String emoji, String tooltip) {
        Button btn = new Button(emoji);
        btn.setStyle("-fx-background-color: rgba(80,80,80,0.95); -fx-text-fill: white; -fx-border-radius: 6; -fx-background-radius: 6; -fx-min-width: 35; -fx-min-height: 28; -fx-font-size: 12px;");
        Glow btnGlow = new Glow(0.5);
        btn.setEffect(btnGlow);
        Tooltip tip = new Tooltip(tooltip);
        Tooltip.install(btn, tip);
        return btn;
    }

    private void setupLayout() {
        this.islandShape.layoutXProperty().bind(this.root.widthProperty().subtract(this.islandWidth).divide(2));
        this.islandShape.layoutYProperty().bind(this.root.heightProperty().subtract(this.islandHeight).divide(2));
        this.timeLabel.layoutXProperty().bind(this.islandShape.layoutXProperty().add(15));
        this.timeLabel.layoutYProperty().bind(this.islandShape.layoutYProperty().add(10));
        this.dateLabel.layoutXProperty().bind(this.islandShape.layoutXProperty().add(15));
        this.dateLabel.layoutYProperty().bind(this.islandShape.layoutYProperty().add(32));
        this.weekLabel.layoutXProperty().bind(this.dateLabel.layoutXProperty().add(this.dateLabel.widthProperty()).add(8));
        this.weekLabel.layoutYProperty().bind(this.islandShape.layoutYProperty().add(32));
        this.logoLabel.layoutXProperty().bind(this.islandShape.layoutXProperty().add(this.islandWidth.subtract(this.logoLabel.widthProperty()).subtract(15)));
        this.logoLabel.layoutYProperty().bind(this.islandShape.layoutYProperty().add(15));
        this.searchField.layoutXProperty().bind(this.islandShape.layoutXProperty().add(10));
        this.searchField.layoutYProperty().bind(this.islandShape.layoutYProperty().add(10));
        this.searchButton.layoutXProperty().bind(this.searchField.layoutXProperty().add(this.searchField.widthProperty()).add(5));
        this.searchButton.layoutYProperty().bind(this.islandShape.layoutYProperty().add(10));
        this.buttonContainer.layoutXProperty().bind(this.islandShape.layoutXProperty().add(10));
        this.buttonContainer.layoutYProperty().bind(this.islandShape.layoutYProperty().add(10));
        this.kaomojiLabel.layoutXProperty().bind(this.islandShape.layoutXProperty().add(this.islandWidth.divide(2)).subtract(this.kaomojiLabel.widthProperty().divide(2)));
        this.kaomojiLabel.layoutYProperty().bind(this.islandShape.layoutYProperty().add(this.islandHeight.divide(2)).subtract(this.kaomojiLabel.heightProperty().divide(2)));
        this.themeSettingsContainer.layoutXProperty().bind(this.islandShape.layoutXProperty().add(10));
        this.themeSettingsContainer.layoutYProperty().bind(this.islandShape.layoutYProperty().add(this.islandHeight.divide(2)).subtract(this.themeSettingsContainer.heightProperty().divide(2)));
        this.optionSettingsContainer.layoutXProperty().bind(this.islandShape.layoutXProperty().add(10));
        this.optionSettingsContainer.layoutYProperty().bind(this.islandShape.layoutYProperty().add(10));
        this.noticeSettingsContainer.layoutXProperty().bind(this.islandShape.layoutXProperty().add(10));
        this.noticeSettingsContainer.layoutYProperty().bind(this.islandShape.layoutYProperty().add(10));
        this.quickSettingsContainer.layoutXProperty().bind(this.islandShape.layoutXProperty().add(10));
        this.quickSettingsContainer.layoutYProperty().bind(this.islandShape.layoutYProperty().add(this.islandHeight.divide(2)).subtract(this.quickSettingsContainer.heightProperty().divide(2)));
        this.spotifyTrackLabel.layoutXProperty().bind(this.islandShape.layoutXProperty().add(15));
        this.spotifyTrackLabel.layoutYProperty().bind(this.islandShape.layoutYProperty().add(55));
        this.spotifyArtistLabel.layoutXProperty().bind(this.islandShape.layoutXProperty().add(15));
        this.spotifyArtistLabel.layoutYProperty().bind(this.islandShape.layoutYProperty().add(68));
        this.spotifyStatusLabel.layoutXProperty().bind(this.islandShape.layoutXProperty().add(15));
        this.spotifyStatusLabel.layoutYProperty().bind(this.islandShape.layoutYProperty().add(60));
        this.spotifyLyricLine1.layoutXProperty().bind(this.islandShape.layoutXProperty().add(140));
        this.spotifyLyricLine1.layoutYProperty().bind(this.islandShape.layoutYProperty().add(52));
        this.spotifyLyricLine2.layoutXProperty().bind(this.islandShape.layoutXProperty().add(140));
        this.spotifyLyricLine2.layoutYProperty().bind(this.islandShape.layoutYProperty().add(63));
        this.spotifyLyricLine3.layoutXProperty().bind(this.islandShape.layoutXProperty().add(140));
        this.spotifyLyricLine3.layoutYProperty().bind(this.islandShape.layoutYProperty().add(74));
    }

    private void setupAnimations() {
        this.setupBreathingAnimation();
    }

    private void setupBreathingAnimation() {
        this.breathingAnimation = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.islandShape.scaleXProperty(), 1.0, new CustomInterpolator(Animation::easeInOutExpo)), new KeyValue(this.islandShape.scaleYProperty(), 1.0, new CustomInterpolator(Animation::easeInOutExpo))), new KeyFrame(Duration.millis(3000.0), new KeyValue(this.islandShape.scaleXProperty(), 1.05, new CustomInterpolator(Animation::easeInOutExpo)), new KeyValue(this.islandShape.scaleYProperty(), 1.08, new CustomInterpolator(Animation::easeInOutExpo))));
        this.breathingAnimation.setAutoReverse(true);
        this.breathingAnimation.setCycleCount(-1);
    }

    private void setupDragHandlers() {
        this.logoLabel.toFront();
        this.logoLabel.setOnContextMenuRequested(e -> {
            if (!(this.isKaomojiMode || this.isDragging || this.isSearchMode || this.isButtonMode)) {
                this.showRandomKaomoji();
            }
            e.consume();
        });
        this.logoLabel.setOnMousePressed(e -> {
            if (this.isSearchMode || this.isButtonMode) {
                return;
            }
            this.isDragging = true;
            this.breathingAnimation.pause();
            this.dragOffsetX = e.getSceneX();
            this.dragOffsetY = e.getSceneY();
            this.logoLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 14px; -fx-cursor: closed-hand;");
            e.consume();
        });
        this.logoLabel.setOnMouseDragged(e -> {
            if (!this.isDragging) {
                return;
            }
            Stage stage = (Stage)this.root.getScene().getWindow();
            double newX = e.getScreenX() - this.dragOffsetX;
            double newY = e.getScreenY() - this.dragOffsetY;
            stage.setX(newX);
            stage.setY(newY);
            e.consume();
        });
        this.logoLabel.setOnMouseReleased(e -> {
            if (!this.isDragging) {
                return;
            }
            this.isDragging = false;
            this.logoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
            Stage stage = (Stage)this.root.getScene().getWindow();
            this.originalX = stage.getX();
            this.originalY = stage.getY();
            if (!this.isSearchMode && !this.isButtonMode) {
                this.breathingAnimation.play();
            }
            e.consume();
        });
    }

    private void setupHoverInteractions() {
        Rectangle topHalf = new Rectangle();
        topHalf.widthProperty().bind(this.islandWidth.multiply(0.6));
        topHalf.setHeight(25.0);
        topHalf.setFill(Color.TRANSPARENT);
        topHalf.setMouseTransparent(false);
        topHalf.layoutXProperty().bind(this.islandShape.layoutXProperty().add(this.islandWidth.multiply(0.2)));
        topHalf.layoutYProperty().bind(this.islandShape.layoutYProperty());
        topHalf.setOnMouseEntered(e -> {
            this.resetIdleTimer();
                        if (!(this.isButtonMode || this.isSearchMode || this.isDragging || this.isThemeMode || this.isOptionMode || this.isNoticeMode || this.isQuickMode || this.isClipboardMode || this.isStopwatchMode || this.isTranslateMode || this.isAnimating)) {
                this.showSearchMode();
            }
        });
        Rectangle bottomHalf = new Rectangle();
        bottomHalf.widthProperty().bind(this.islandWidth.multiply(0.6));
        bottomHalf.setHeight(25.0);
        bottomHalf.setFill(Color.TRANSPARENT);
        bottomHalf.setMouseTransparent(false);
        bottomHalf.layoutXProperty().bind(this.islandShape.layoutXProperty().add(this.islandWidth.multiply(0.2)));
        bottomHalf.layoutYProperty().bind(this.islandShape.layoutYProperty().add(25));
        bottomHalf.setOnMouseEntered(e -> {
            this.resetIdleTimer();
                        if (!(this.isSearchMode || this.isButtonMode || this.isDragging || this.isThemeMode || this.isOptionMode || this.isNoticeMode || this.isQuickMode || this.isClipboardMode || this.isStopwatchMode || this.isTranslateMode || this.isAnimating)) {
                this.showButtonMode();
            }
        });
        this.root.setOnMouseMoved(e -> {
            this.resetIdleTimer();
            if (this.isSearchMode || this.isButtonMode || this.isThemeMode || this.isOptionMode || this.isNoticeMode || this.isQuickMode || this.isClipboardMode || this.isStopwatchMode || this.isTranslateMode) {
                double mouseX = e.getSceneX();
                double mouseY = e.getSceneY();
                double islandX = this.islandShape.getLayoutX();
                double islandY = this.islandShape.getLayoutY();
                double islandRight = islandX + this.islandWidth.get();
                double islandBottom = islandY + this.islandHeight.get();
                
                double paddingXY = this.isStopwatchMode ? 15.0 : 10.0;
                double effectiveBottom = this.isStopwatchMode ? 
                    islandY + 120.0 : islandBottom;
                    
                if (mouseX < islandX - paddingXY || mouseX > islandRight + paddingXY || 
                    mouseY < islandY - paddingXY || mouseY > effectiveBottom + paddingXY) {
                    this.hideAllInteractiveModes();
                }
            }
        });
        
        this.root.setOnMouseExited(e -> {
            this.resetIdleTimer();
            if (this.isSearchMode || this.isButtonMode || this.isThemeMode || this.isOptionMode || this.isNoticeMode || this.isQuickMode || this.isClipboardMode || this.isStopwatchMode || this.isTranslateMode) {
                this.hideAllInteractiveModes();
            }
        });
        this.islandContainer.getChildren().addAll((Node[])new Node[]{topHalf, bottomHalf});
    }

    private void stopAllCurrentAnimations() {
        if (this.currentTransitionAnimation != null) {
            this.currentTransitionAnimation.stop();
            this.currentTransitionAnimation = null;
        }
        if (this.currentPauseTransition != null) {
            this.currentPauseTransition.stop();
            this.currentPauseTransition = null;
        }
    }

    private void hideAllInteractiveModes() {
        this.stopAllCurrentAnimations();
        if (this.isSearchMode) {
            this.hideSearchMode();
        }
        if (this.isButtonMode) {
            this.hideButtonMode();
        }
        if (this.isThemeMode) {
            this.hideThemeSettings();
        }
        if (this.isOptionMode) {
            this.hideOptionSettings();
        }
        if (this.isNoticeMode) {
            this.hideNoticeSettings();
        }
        if (this.isQuickMode) {
            this.hideQuickSettings();
        }
        if (this.isClipboardMode) {
            this.hideClipboardMode();
        }
        if (this.isStopwatchMode) {
            this.hideStopwatchMode();
        }
        if (this.isTranslateMode) {
            this.hideTranslateMode();
        }
    }

    private void showSearchMode() {
        PauseTransition fadeOutDelay;
        if (this.isButtonMode || this.isSearchMode || this.isDragging || this.isAnimating) {
            return;
        }
        this.stopAllCurrentAnimations();
        this.buttonContainer.setVisible(false);
        this.optionSettingsContainer.setVisible(false);
        this.themeSettingsContainer.setVisible(false);
        this.noticeSettingsContainer.setVisible(false);
        this.scrollingQuoteText.setVisible(false);
        this.isSearchMode = true;
        this.isAnimating = true;
        this.breathingAnimation.pause();
        this.animateFadeOut(this.timeLabel, 150);
        this.animateFadeOut(this.dateLabel, 150);
        this.animateFadeOut(this.weekLabel, 150);
        this.animateFadeOut(this.logoLabel, 150);
        this.animateFadeOut(this.scrollingQuoteText, 150);
        this.currentPauseTransition = fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            Timeline shrinkAnimation;
            double targetWidth = this.searchField.getPrefWidth() + this.searchButton.getWidth() + 25.0;
            this.currentTransitionAnimation = shrinkAnimation = new Timeline(new KeyFrame(Duration.millis(300.0), new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo))));
            shrinkAnimation.setOnFinished(ev -> {
                this.hideSpotifyTemporarily();
                this.searchField.toFront();
                this.searchButton.toFront();
                this.searchField.setOpacity(0.0);
                this.searchField.setVisible(true);
                this.animateFadeInNode(this.searchField, 200);
                this.searchButton.setOpacity(0.0);
                this.searchButton.setVisible(true);
                this.animateFadeInNode(this.searchButton, 200);
                PauseTransition focusDelay = new PauseTransition(Duration.millis(250.0));
                focusDelay.setOnFinished(evv -> {
                    this.searchField.requestFocus();
                    this.isAnimating = false;
                });
                focusDelay.play();
            });
            shrinkAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void showButtonMode() {
        PauseTransition fadeOutDelay;
        if (this.isSearchMode || this.isButtonMode || this.isDragging || this.isAnimating) {
            return;
        }
        this.stopAllCurrentAnimations();
        this.searchField.setVisible(false);
        this.searchButton.setVisible(false);
        this.optionSettingsContainer.setVisible(false);
        this.themeSettingsContainer.setVisible(false);
        this.noticeSettingsContainer.setVisible(false);
        this.scrollingQuoteText.setVisible(false);
        this.isButtonMode = true;
        this.isAnimating = true;
        this.hideSpotifyTemporarily();
        this.breathingAnimation.pause();
        this.quoteTimeline.pause();
        this.animateFadeOut(this.timeLabel, 150);
        this.animateFadeOut(this.dateLabel, 150);
        this.animateFadeOut(this.weekLabel, 150);
        this.animateFadeOut(this.logoLabel, 150);
        this.animateFadeOut(this.scrollingQuoteText, 150);
        this.currentPauseTransition = fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            Timeline shrinkAnimation;
            this.moveToMousePosition();
            double targetWidth = 184.0;
            this.currentTransitionAnimation = shrinkAnimation = new Timeline(new KeyFrame(Duration.millis(300.0), new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo))));
            shrinkAnimation.setOnFinished(ev -> {
                this.buttonContainer.toFront();
                this.buttonContainer.setVisible(true);
                this.animateFadeInNode(this.buttonContainer, 200);
                PauseTransition unlockDelay = new PauseTransition(Duration.millis(200.0));
                unlockDelay.setOnFinished(evv -> {
                    this.isAnimating = false;
                });
                unlockDelay.play();
            });
            shrinkAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void animateFadeInNode(Node node, int duration) {
        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setInterpolator(new CustomInterpolator(Animation::easeOutCubic));
        fade.setToValue(1.0);
        fade.play();
    }

    private void animateFadeInNode(Node node, int duration, double targetOpacity) {
        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setInterpolator(new CustomInterpolator(Animation::easeOutCubic));
        fade.setToValue(targetOpacity);
        fade.play();
    }

    private void animateFadeOut(Node node, int duration) {
        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setInterpolator(new CustomInterpolator(Animation::easeInCubic));
        fade.setToValue(0.0);
        fade.play();
    }

    private void hideSearchMode() {
        this.isSearchMode = false;
        this.isAnimating = true;
        this.animateFadeOut(this.searchField, 150);
        this.animateFadeOut(this.searchButton, 150);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            this.searchField.setVisible(false);
            this.searchButton.setVisible(false);
            Timeline expandAnimation = new Timeline(new KeyFrame(Duration.millis(300.0), new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo))));
            expandAnimation.setOnFinished(ev -> this.showNormalContentWithAnimation());
            expandAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void hideButtonMode() {
        if (!this.isButtonMode) {
            return;
        }
        this.isButtonMode = false;
        this.isAnimating = true;
        this.animateFadeOut(this.buttonContainer, 150);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            this.buttonContainer.setVisible(false);
            Timeline expandAnimation = new Timeline(new KeyFrame(Duration.millis(300.0), new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo))));
            expandAnimation.setOnFinished(ev -> this.showNormalContentWithAnimation());
            expandAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void showNormalContentWithAnimation() {
        this.moveBackToOriginalPosition();
        this.scrollingQuoteText.setVisible(true);
        this.animateFadeInNode(this.timeLabel, 300);
        this.animateFadeInNode(this.dateLabel, 300, 0.8);
        this.animateFadeInNode(this.weekLabel, 300, 0.8);
        this.animateFadeInNode(this.logoLabel, 300);
        this.animateFadeInNode(this.scrollingQuoteText, 300, 0.7);
        this.restoreSpotifyDisplay();
        PauseTransition resumeDelay = new PauseTransition(Duration.millis(600.0));
        resumeDelay.setOnFinished(e -> {
            this.breathingAnimation.play();
            this.quoteTimeline.play();
            this.isAnimating = false;
        });
        resumeDelay.play();
    }

    private void performSearch() {
        String input = this.searchField.getText().trim();
        if (!input.isEmpty()) {
            try {
                Object url = input.contains(".") && !input.contains(" ") ? (input.startsWith("http") ? input : "https://" + input) : "https://www.bing.com/search?q=" + input.replace(" ", "+");
                Desktop.getDesktop().browse(new URI((String)url));
                this.searchField.clear();
                this.hideSearchMode();
            }
            catch (IOException | URISyntaxException ex) {
                this.showAlert("Error", "Cannot Use Edge: " + ex.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateTime() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.timeLabel.setText(currentTime.format(formatter));
        this.updateLogoAndQuote();
    }

    private void updateDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        this.dateLabel.setText(currentDate.format(formatter));
    }

    private void updateWeek() {
        LocalDate currentDate = LocalDate.now();
        String weekDay = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.CHINA);
        this.weekLabel.setText(weekDay);
    }

    private String[] getCurrentQuotes() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return new String[]{"晨光微露，希望初醒", "每一个清晨都是重新开始的机会", "朝阳不语，却照亮了整个世界", "早起的鸟儿有虫吃，早起的人儿有梦追"};
        }
        if (hour >= 12 && hour < 18) {
            return new String[]{"正午阳光，万物生长", "时光如流水，珍惜当下", "午后的宁静是最美的礼物", "阳光正好，微风不燥"};
        }
        if (hour >= 18 && hour < 22) {
            return new String[]{"夕阳无限好，只是近黄昏", "晚霞是天空写给大地的情书", "暮色温柔，心事悠悠", "黄昏是白昼与黑夜的温柔交替"};
        }
        return new String[]{"夜空中的繁星，又何尝不是一种泪滴", "月光如水，洗净尘世的喧嚣", "深夜是思考者的乐园", "星星不说话，却在倾听所有愿望"};
    }

    private void updateLogoAndQuote() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        String logoEmoji = hour >= 5 && hour < 12 ? "☀" : (hour >= 12 && hour < 18 ? "🌤" : (hour >= 18 && hour < 22 ? "🌇" : "🌙"));
        this.logoLabel.setText(logoEmoji);
    }

    private void startTimeUpdate() {
        this.timeUpdateTimer = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> {
            this.updateTime();
            this.updateDate();
            this.updateWeek();
        }, new KeyValue[0]));
        this.timeUpdateTimer.setCycleCount(-1);
        this.timeUpdateTimer.play();
    }

    private void showRandomKaomoji() {
        this.isKaomojiMode = true;
        this.hideAllInteractiveModes();
        this.breathingAnimation.pause();
        String randomKaomoji = this.kaomojis[(int)(Math.random() * (double)this.kaomojis.length)];
        Text text = new Text(randomKaomoji);
        text.setFont(this.kaomojiLabel.getFont());
        double targetWidth = text.getLayoutBounds().getWidth() + 40.0;
        Timeline shrinkAnimation = new Timeline(new KeyFrame(Duration.millis(300.0), new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo))));
        this.animateFadeOut(this.timeLabel, 150);
        this.animateFadeOut(this.dateLabel, 150);
        this.animateFadeOut(this.weekLabel, 150);
        this.animateFadeOut(this.logoLabel, 150);
        this.animateFadeOut(this.scrollingQuoteText, 150);
        shrinkAnimation.setOnFinished(e -> {
            this.kaomojiLabel.setText(randomKaomoji);
            this.kaomojiLabel.setVisible(true);
            this.animateFadeInNode(this.kaomojiLabel, 200);
        });
        shrinkAnimation.play();
        PauseTransition hideKaomojiDelay = new PauseTransition(Duration.seconds(2.0));
        hideKaomojiDelay.setOnFinished(e -> {
            this.animateFadeOut(this.kaomojiLabel, 200);
            Timeline expandAnimation = new Timeline(new KeyFrame(Duration.millis(300.0), new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo))));
            expandAnimation.setOnFinished(ev -> {
                this.kaomojiLabel.setVisible(false);
                this.showNormalContentWithAnimation();
                this.isKaomojiMode = false;
            });
            expandAnimation.play();
        });
        hideKaomojiDelay.play();
    }

    private void setupQuoteAnimation() {
        this.scrollingQuoteText = new Text();
        this.scrollingQuoteText.setStyle("-fx-fill: rgba(255,255,255,0.7); -fx-font-size: 10px;");
        this.scrollingQuoteText.setOpacity(0.0);
        this.scrollingQuoteText.xProperty().bind(this.weekLabel.layoutXProperty().add(this.weekLabel.widthProperty()).add(8));
        this.scrollingQuoteText.yProperty().bind(this.islandShape.layoutYProperty().add(42));
        this.quoteTimeline = new Timeline();
        this.quoteTimeline.setCycleCount(-1);
        this.quoteTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(8.0), e -> this.switchToNextQuote(), new KeyValue[0]));
    }

    private void switchToNextQuote() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500.0), this.scrollingQuoteText);
        fadeOut.setToValue(0.0);
        fadeOut.setInterpolator(new CustomInterpolator(Animation::easeInCubic));
        fadeOut.setOnFinished(e -> {
            String[] quotes = this.getCurrentQuotes();
            String quote = quotes[(int)(Math.random() * (double)quotes.length)];
            this.scrollingQuoteText.setText(quote);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500.0), this.scrollingQuoteText);
            fadeIn.setToValue(0.7);
            fadeIn.setInterpolator(new CustomInterpolator(Animation::easeOutCubic));
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void startBreathingAnimation() {
        this.breathingAnimation.play();
    }

    private boolean isDarkMode() {
        return ((Rectangle)this.islandShape).getFill().equals(this.darkBg);
    }

    private void applyTheme(boolean dark) {
        this.configManager.getConfig().isDarkTheme = dark;
        this.configManager.saveConfig();
        Color fromBg = this.isDarkMode() ? this.darkBg : this.lightBg;
        Color toBg = dark ? this.darkBg : this.lightBg;
        Color toText = dark ? this.darkText : this.lightText;
        FillTransition bgTransition = new FillTransition(Duration.millis(400.0), (Rectangle)this.islandShape, fromBg, toBg);
        bgTransition.setInterpolator(new CustomInterpolator(Animation::easeInOutCubic));
        bgTransition.play();
        for (Node node : this.islandContainer.getChildren()) {
            if (!(node instanceof Label)) continue;
            Timeline textTransition = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(((Label)node).textFillProperty(), toText, new CustomInterpolator(Animation::easeInOutCubic))));
            textTransition.play();
        }
        Timeline quoteColorTransition = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.scrollingQuoteText.fillProperty(), toText.deriveColor(0.0, 1.0, 1.0, 0.7), new CustomInterpolator(Animation::easeInOutCubic))));
        quoteColorTransition.play();
        
        if (this.idleTextLabel != null) {
            this.updateIdleTextStyle();
        }
        if (this.isTranslateMode && this.translateContainer != null) {
            this.updateTranslateTheme();
        }
    }

    private void toggleBloom(boolean enable) {
        this.configManager.getConfig().bloomEnabled = enable;
        this.configManager.saveConfig();
        double level = enable ? 0.5 : 0.0;
        Glow glow = enable ? new Glow(level) : null;
        this.searchButton.setEffect(glow);
        this.settingsBtn.setEffect(glow);
        this.themeBtn.setEffect(glow);
        this.quickBtn.setEffect(glow);
        this.notifyBtn.setEffect(glow);
    }

    private void showThemeSettings() {
        if (this.isAnimating) {
            return;
        }
        this.isThemeMode = true;
        this.isButtonMode = false;
        this.isAnimating = true;
        this.hideSpotifyTemporarily();
        this.scrollingQuoteText.setVisible(false);
        this.animateFadeOut(this.buttonContainer, 150);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            this.moveToMousePosition(false);
            double targetWidth = 260.0;
            Timeline expandAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo))));
            expandAnimation.setOnFinished(ev -> {
                this.themeSettingsContainer.toFront();
                this.themeSettingsContainer.setVisible(true);
                this.animateFadeInNode(this.themeSettingsContainer, 200);
                PauseTransition unlockDelay = new PauseTransition(Duration.millis(200.0));
                unlockDelay.setOnFinished(evv -> {
                    this.isAnimating = false;
                });
                unlockDelay.play();
            });
            expandAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void hideThemeSettings() {
        if (!this.isThemeMode) {
            return;
        }
        this.isThemeMode = false;
        this.isAnimating = true;
        this.animateFadeOut(this.themeSettingsContainer, 200);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(200.0));
        fadeOutDelay.setOnFinished(e -> {
            this.themeSettingsContainer.setVisible(false);
            Timeline shrinkAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo))));
            shrinkAnimation.setOnFinished(ev -> this.showNormalContentWithAnimation());
            shrinkAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void createNoticeSettingsUI() {
        this.noticeSettingsContainer = new HBox(8.0);
        this.noticeSettingsContainer.setAlignment(Pos.CENTER_LEFT);
        this.noticeSettingsContainer.setVisible(false);
        this.spotifyBtn = this.createStyledToggleButton("Spotify");
        this.spotifyBtn.setSelected(this.isMusicModeEnabled);
        this.spotifyBtn.selectedProperty().addListener((obs, oldVal, newVal) -> {
            this.isMusicModeEnabled = newVal;
            if (newVal.booleanValue()) {
                if (!this.spotifyAPI.isAuthorized()) {
                    this.spotifyAPI.startAuthorization(success -> Platform.runLater(() -> {
                        if (success) {
                            this.startSpotifyMode();
                        } else {
                            this.spotifyBtn.setSelected(false);
                            this.showAlert("Authorization Failed", "Failed to authorize Spotify. Please try again.");
                        }
                    }));
                } else {
                    this.startSpotifyMode();
                }
            } else {
                this.stopSpotifyMode();
            }
        });
        this.noticeSettingsContainer.getChildren().add(this.spotifyBtn);
    }

    private void showNoticeSettings() {
        if (this.isAnimating) {
            return;
        }
        this.isNoticeMode = true;
        this.isButtonMode = false;
        this.isAnimating = true;
        this.hideSpotifyTemporarily();
        this.scrollingQuoteText.setVisible(false);
        this.animateFadeOut(this.buttonContainer, 150);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            this.moveToMousePosition(false);
            double targetWidth = 80.0;
            Timeline expandAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo))));
            expandAnimation.setOnFinished(ev -> {
                this.noticeSettingsContainer.toFront();
                this.noticeSettingsContainer.setVisible(true);
                this.animateFadeInNode(this.noticeSettingsContainer, 200);
                PauseTransition unlockDelay = new PauseTransition(Duration.millis(200.0));
                unlockDelay.setOnFinished(evv -> {
                    this.isAnimating = false;
                });
                unlockDelay.play();
            });
            expandAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void hideNoticeSettings() {
        if (!this.isNoticeMode) {
            return;
        }
        this.isNoticeMode = false;
        this.isAnimating = true;
        this.animateFadeOut(this.noticeSettingsContainer, 200);
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(200.0));
        fadeOutDelay.setOnFinished(e -> {
            this.noticeSettingsContainer.setVisible(false);
            Timeline shrinkAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo))));
            shrinkAnimation.setOnFinished(ev -> this.showNormalContentWithAnimation());
            shrinkAnimation.play();
        });
        fadeOutDelay.play();
    }

    private void startSpotifyMode() {
        Timeline expandAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandHeight, 85, new CustomInterpolator(Animation::easeOutExpo)), new KeyValue(this.cornerRadius, 25, new CustomInterpolator(Animation::easeOutExpo))));
        expandAnimation.setOnFinished(e -> {
            this.spotifyTrackLabel.setVisible(true);
            this.spotifyArtistLabel.setVisible(true);
            this.spotifyLyricLine1.setVisible(true);
            this.spotifyLyricLine2.setVisible(true);
            this.spotifyLyricLine3.setVisible(true);
            this.animateFadeInNode(this.spotifyTrackLabel, 300);
            this.animateFadeInNode(this.spotifyArtistLabel, 300);
            this.animateFadeInNode(this.spotifyLyricLine1, 400);
            this.animateFadeInNode(this.spotifyLyricLine2, 400);
            this.animateFadeInNode(this.spotifyLyricLine3, 400);
        });
        expandAnimation.play();
        this.spotifyUpdateTimer.play();
        new Thread(() -> {
            SpotifyAPI.SpotifyTrack track = this.spotifyAPI.getCurrentTrack();
            Platform.runLater(() -> this.updateSpotifyDisplay(track));
        }).start();
    }

    private void hideSpotifyTemporarily() {
        if (!this.isMusicModeEnabled) {
            return;
        }
        this.spotifyUpdateTimer.pause();
        FadeTransition fade1 = new FadeTransition(Duration.millis(150.0), this.spotifyTrackLabel);
        fade1.setFromValue(this.spotifyTrackLabel.getOpacity());
        fade1.setToValue(0.0);
        fade1.play();
        FadeTransition fade2 = new FadeTransition(Duration.millis(150.0), this.spotifyArtistLabel);
        fade2.setFromValue(this.spotifyArtistLabel.getOpacity());
        fade2.setToValue(0.0);
        fade2.play();
        FadeTransition fade3 = new FadeTransition(Duration.millis(150.0), this.spotifyStatusLabel);
        fade3.setFromValue(this.spotifyStatusLabel.getOpacity());
        fade3.setToValue(0.0);
        fade3.play();
        FadeTransition fade4 = new FadeTransition(Duration.millis(150.0), this.spotifyLyricLine1);
        fade4.setFromValue(this.spotifyLyricLine1.getOpacity());
        fade4.setToValue(0.0);
        fade4.play();
        FadeTransition fade5 = new FadeTransition(Duration.millis(150.0), this.spotifyLyricLine2);
        fade5.setFromValue(this.spotifyLyricLine2.getOpacity());
        fade5.setToValue(0.0);
        fade5.play();
        FadeTransition fade6 = new FadeTransition(Duration.millis(150.0), this.spotifyLyricLine3);
        fade6.setFromValue(this.spotifyLyricLine3.getOpacity());
        fade6.setToValue(0.0);
        fade6.play();
        PauseTransition delay = new PauseTransition(Duration.millis(200.0));
        delay.setOnFinished(e -> {
            this.spotifyTrackLabel.setVisible(false);
            this.spotifyArtistLabel.setVisible(false);
            this.spotifyStatusLabel.setVisible(false);
            this.spotifyLyricLine1.setVisible(false);
            this.spotifyLyricLine2.setVisible(false);
            this.spotifyLyricLine3.setVisible(false);
            Timeline shrinkAnimation = new Timeline(new KeyFrame(Duration.millis(300.0), new KeyValue(this.islandHeight, 50, new CustomInterpolator(Animation::easeInExpo))));
            shrinkAnimation.play();
        });
        delay.play();
    }

    private void restoreSpotifyDisplay() {
        if (this.isMusicModeEnabled) {
            this.spotifyUpdateTimer.play();
            Timeline expandAnimation = new Timeline(new KeyFrame(Duration.millis(300.0), new KeyValue(this.islandHeight, 85, new CustomInterpolator(Animation::easeOutExpo))));
            expandAnimation.setOnFinished(e -> {
                this.spotifyTrackLabel.setVisible(true);
                this.spotifyArtistLabel.setVisible(true);
                this.spotifyLyricLine1.setVisible(true);
                this.spotifyLyricLine2.setVisible(true);
                this.spotifyLyricLine3.setVisible(true);
                this.spotifyTrackLabel.setOpacity(1.0);
                this.spotifyArtistLabel.setOpacity(1.0);
                this.spotifyLyricLine1.setOpacity(0.4);
                this.spotifyLyricLine2.setOpacity(1.0);
                this.spotifyLyricLine3.setOpacity(0.4);
                this.animateFadeInNode(this.spotifyTrackLabel, 200);
                this.animateFadeInNode(this.spotifyArtistLabel, 200);
                this.animateFadeInNode(this.spotifyLyricLine1, 200);
                this.animateFadeInNode(this.spotifyLyricLine2, 200);
                this.animateFadeInNode(this.spotifyLyricLine3, 200);
            });
            expandAnimation.play();
        }
    }

    private void stopSpotifyMode() {
        this.spotifyUpdateTimer.stop();
        this.animateFadeOut(this.spotifyTrackLabel, 200);
        this.animateFadeOut(this.spotifyArtistLabel, 200);
        this.animateFadeOut(this.spotifyStatusLabel, 200);
        this.animateFadeOut(this.spotifyLyricLine1, 200);
        this.animateFadeOut(this.spotifyLyricLine2, 200);
        this.animateFadeOut(this.spotifyLyricLine3, 200);
        PauseTransition delay = new PauseTransition(Duration.millis(250.0));
        delay.setOnFinished(e -> {
            this.spotifyTrackLabel.setVisible(false);
            this.spotifyArtistLabel.setVisible(false);
            this.spotifyStatusLabel.setVisible(false);
            this.spotifyLyricLine1.setVisible(false);
            this.spotifyLyricLine2.setVisible(false);
            this.spotifyLyricLine3.setVisible(false);
            Timeline shrinkAnimation = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(this.islandHeight, 50, new CustomInterpolator(Animation::easeInExpo)), new KeyValue(this.cornerRadius, 25, new CustomInterpolator(Animation::easeInExpo))));
            shrinkAnimation.play();
        });
        delay.play();
    }

    private void updateSpotifyDisplay(SpotifyAPI.SpotifyTrack track) {
        if (!this.spotifyAPI.hasValidToken()) {
            this.spotifyTrackLabel.setVisible(false);
            this.spotifyArtistLabel.setVisible(false);
            this.spotifyStatusLabel.setVisible(true);
            this.spotifyStatusLabel.setText("⚠ No access token");
            this.hideLyrics();
        } else if (track.isEmpty()) {
            this.spotifyTrackLabel.setVisible(false);
            this.spotifyArtistLabel.setVisible(false);
            this.spotifyStatusLabel.setVisible(true);
            this.spotifyStatusLabel.setText("⏸ No track playing");
            this.hideLyrics();
        } else if (!track.isPlaying) {
            this.spotifyTrackLabel.setVisible(false);
            this.spotifyArtistLabel.setVisible(false);
            this.spotifyStatusLabel.setVisible(true);
            this.spotifyStatusLabel.setText("⏸ Paused - " + track.trackName);
            this.hideLyrics();
        } else {
            this.spotifyStatusLabel.setVisible(false);
            this.spotifyTrackLabel.setVisible(true);
            this.spotifyArtistLabel.setVisible(true);
            this.spotifyTrackLabel.setText("♪ " + track.trackName);
            this.spotifyArtistLabel.setText(track.artistName);
            if (!track.trackId.equals(this.currentTrackId)) {
                this.currentTrackId = track.trackId;
                this.currentLyricIndex = 0;
                this.fetchSyncedLyrics(track.trackName, track.artistName);
            }
            this.updateSyncedLyrics(track.progressMs);
        }
    }

    private void hideLyrics() {
        this.spotifyLyricLine1.setVisible(false);
        this.spotifyLyricLine2.setVisible(false);
        this.spotifyLyricLine3.setVisible(false);
    }

    private void fetchSyncedLyrics(String trackName, String artistName) {
        new Thread(() -> {
            List<SpotifyAPI.LyricLine> lyrics = this.spotifyAPI.getLyrics(trackName, artistName);
            Platform.runLater(() -> {
                this.syncedLyrics.clear();
                this.syncedLyrics.addAll(lyrics);
                this.currentLyricIndex = 0;
                System.out.println("Synced lyrics updated: " + this.syncedLyrics.size() + " lines");
                if (!this.syncedLyrics.isEmpty()) {
                    System.out.println("First line: " + this.syncedLyrics.get((int)0).text);
                }
            });
        }).start();
    }

    private void updateSyncedLyrics(int progressMs) {
        if (!this.spotifyLyricLine1.isVisible()) {
            this.spotifyLyricLine1.setVisible(true);
            this.spotifyLyricLine2.setVisible(true);
            this.spotifyLyricLine3.setVisible(true);
        }
        if (this.syncedLyrics.isEmpty()) {
            this.spotifyLyricLine1.setText("");
            this.spotifyLyricLine2.setText("♪ Loading lyrics...");
            this.spotifyLyricLine3.setText("");
            return;
        }
        int newIndex = this.findCurrentLyricIndex(progressMs);
        if (newIndex != this.currentLyricIndex && newIndex < this.syncedLyrics.size()) {
                        this.currentLyricIndex = newIndex;
            this.animateLyricTransition();
        }
        this.updateLyricLines();
    }

    private int findCurrentLyricIndex(int progressMs) {
        for (int i = this.syncedLyrics.size() - 1; i >= 0; --i) {
            if ((long)progressMs < this.syncedLyrics.get((int)i).startTimeMs) continue;
            return i;
        }
        return 0;
    }

    private void updateLyricLines() {
        if (this.syncedLyrics.isEmpty()) {
            return;
        }
        int prevIndex = Math.max(0, this.currentLyricIndex - 1);
        int nextIndex = Math.min(this.syncedLyrics.size() - 1, this.currentLyricIndex + 1);
        this.spotifyLyricLine1.setText(prevIndex < this.currentLyricIndex ? this.syncedLyrics.get((int)prevIndex).text : "");
        this.spotifyLyricLine2.setText(this.syncedLyrics.get((int)this.currentLyricIndex).text);
        this.spotifyLyricLine3.setText(nextIndex > this.currentLyricIndex ? this.syncedLyrics.get((int)nextIndex).text : "");
    }

    private void animateLyricTransition() {
        this.spotifyLyricLine1.setOpacity(0.4);
        this.spotifyLyricLine2.setOpacity(1.0);
        this.spotifyLyricLine3.setOpacity(0.4);
    }

    private void moveToMousePosition() {
        this.moveToMousePosition(true);
    }

    private void moveToMousePosition(boolean smartCheck) {
        Stage stage = (Stage)this.root.getScene().getWindow();
        if (stage == null) {
            return;
        }
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        double targetX = mousePos.x - 200;
        double targetY = mousePos.y - 50;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double newX = Math.max(0.0, Math.min(targetX, screenBounds.getWidth() - 400.0));
        double newY = Math.max(0.0, Math.min(targetY, screenBounds.getHeight() - 100.0));
        if (smartCheck) {
            double currentStageX = stage.getX();
            double currentStageY = stage.getY();
            double stageWidth = 400.0;
            double stageHeight = 100.0;
            double currentCenterX = currentStageX + stageWidth / 2.0;
            double currentCenterY = currentStageY + stageHeight / 2.0;
            double distanceToCurrent = Math.sqrt(Math.pow((double)mousePos.x - currentCenterX, 2.0) + Math.pow((double)mousePos.y - currentCenterY, 2.0));
            if (distanceToCurrent < 150.0) {
                return;
            }
        }
        SimpleDoubleProperty currentX = new SimpleDoubleProperty(stage.getX());
        SimpleDoubleProperty currentY = new SimpleDoubleProperty(stage.getY());
        currentX.addListener((obs, oldVal, newVal) -> stage.setX(newVal.doubleValue()));
        currentY.addListener((obs, oldVal, newVal) -> stage.setY(newVal.doubleValue()));
        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(200.0), new KeyValue(currentX, newX, new CustomInterpolator(Animation::easeOutQuad)), new KeyValue(currentY, newY, new CustomInterpolator(Animation::easeOutQuad))));
        moveTimeline.play();
    }

    private void moveBackToOriginalPosition() {
        Stage stage = (Stage)this.root.getScene().getWindow();
        if (stage == null) {
            return;
        }
        SimpleDoubleProperty currentX = new SimpleDoubleProperty(stage.getX());
        SimpleDoubleProperty currentY = new SimpleDoubleProperty(stage.getY());
        currentX.addListener((obs, oldVal, newVal) -> stage.setX(newVal.doubleValue()));
        currentY.addListener((obs, oldVal, newVal) -> stage.setY(newVal.doubleValue()));
        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(400.0), new KeyValue(currentX, this.originalX, new CustomInterpolator(Animation::easeInOutCubic)), new KeyValue(currentY, this.originalY, new CustomInterpolator(Animation::easeInOutCubic))));
        moveTimeline.play();
    }

    private void createClipboardUI() {
        String userHome = System.getProperty("user.home");
        String appDataPath = userHome + "\\AppData\\NightSky";
        this.clipboardFilePath = appDataPath + "\\Clipboard.json";
        
        File appDataDir = new File(appDataPath);
        if (!appDataDir.exists()) {
            appDataDir.mkdirs();
        }
        
        this.loadClipboardHistory();
        
        this.clipboardContainer = new HBox(10.0);
        this.clipboardContainer.setAlignment(Pos.CENTER_LEFT);
        this.clipboardContainer.setVisible(false);
        
        this.clipboardItemsContainer = new javafx.scene.layout.VBox(5.0);
        this.clipboardItemsContainer.setStyle("-fx-padding: 5;");
        
        this.clipboardScrollPane = new javafx.scene.control.ScrollPane();
        this.clipboardScrollPane.setContent(this.clipboardItemsContainer);
        this.clipboardScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        this.clipboardScrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        this.clipboardScrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.clipboardScrollPane.setPrefSize(320, 120);
        this.clipboardScrollPane.setMaxSize(320, 120);
        this.clipboardScrollPane.setFitToWidth(true);
        
        Button clearAllBtn = new Button("🗑");
        clearAllBtn.setStyle("-fx-background-color: rgba(220,53,69,0.9); -fx-text-fill: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-min-width: 35; -fx-min-height: 28; -fx-font-size: 14px;");
        clearAllBtn.setOnAction(e -> this.clearAllClipboard());
        
        Glow clearGlow = new Glow(0.6);
        clearAllBtn.setEffect(clearGlow);
        Tooltip clearTip = new Tooltip("Clear All");
        Tooltip.install(clearAllBtn, clearTip);
        
        this.clipboardContainer.getChildren().addAll(this.clipboardScrollPane, clearAllBtn);
        
        this.clipboardContainer.layoutXProperty().bind(this.islandShape.layoutXProperty().add(10));
        this.clipboardContainer.layoutYProperty().bind(this.islandShape.layoutYProperty().add(10));
    }
    
    private void initClipboardMonitor() {
        this.clipboardMonitorTimer = new Timeline(new KeyFrame(Duration.millis(500.0), e -> this.checkClipboard()));
        this.clipboardMonitorTimer.setCycleCount(-1);
        this.clipboardMonitorTimer.play();
    }
    
    private void checkClipboard() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = clipboard.getContents(null);
            
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String content = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                
                if (content != null && !content.equals(this.lastClipboardContent) && !content.trim().isEmpty()) {
                    this.lastClipboardContent = content;
                    this.addClipboardItem(content);
                }
            }
        } catch (Exception ex) {
        }
    }
    
    private void addClipboardItem(String content) {
        if (this.clipboardHistory.contains(content)) {
            this.clipboardHistory.remove(content);
        }
        
        this.clipboardHistory.add(0, content);
        
        if (this.clipboardHistory.size() > 10) {
            this.clipboardHistory.remove(this.clipboardHistory.size() - 1);
        }
        
        this.saveClipboardHistory();
        Platform.runLater(() -> this.refreshClipboardUI());
    }
    
    private void refreshClipboardUI() {
        this.clipboardItemsContainer.getChildren().clear();
        
        for (int i = 0; i < this.clipboardHistory.size(); i++) {
            String content = this.clipboardHistory.get(i);
            this.clipboardItemsContainer.getChildren().add(this.createClipboardItemNode(content, i));
        }
    }
    
    private HBox createClipboardItemNode(String content, int index) {
        HBox itemBox = new HBox(8.0);
        itemBox.setAlignment(Pos.CENTER_LEFT);
        itemBox.setStyle("-fx-background-color: rgba(60,60,60,0.95); -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 10; -fx-cursor: hand;");
        
        String displayText = content.length() > 35 ? content.substring(0, 35) + "..." : content;
        displayText = displayText.replace("\n", " ").replace("\r", " ");
        
        Label contentLabel = new Label(displayText);
        contentLabel.setStyle("-fx-text-fill: white; -fx-font-size: 11px;");
        contentLabel.setMaxWidth(250);
        
        Button deleteBtn = new Button("×");
        deleteBtn.setStyle("-fx-background-color: rgba(220,53,69,0.8); -fx-text-fill: white; -fx-border-radius: 6; -fx-background-radius: 6; -fx-min-width: 20; -fx-min-height: 20; -fx-font-size: 12px; -fx-padding: 0;");
        deleteBtn.setOnAction(e -> {
            this.removeClipboardItem(index);
            e.consume();
        });
        
        itemBox.getChildren().addAll(contentLabel, deleteBtn);
        
        itemBox.setOnMouseEntered(e -> {
            itemBox.setStyle("-fx-background-color: rgba(80,80,80,0.95); -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 10; -fx-cursor: hand;");
        });
        
        itemBox.setOnMouseExited(e -> {
            itemBox.setStyle("-fx-background-color: rgba(60,60,60,0.95); -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 10; -fx-cursor: hand;");
        });
        
        itemBox.setOnMouseClicked(e -> {
            if (e.getTarget() != deleteBtn) {
                this.pasteClipboardItem(content);
                this.hideClipboardMode();
            }
        });
        
        return itemBox;
    }
    
    private void pasteClipboardItem(String content) {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(content);
            clipboard.setContents(selection, null);
            
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (Exception ex) {
        }
    }
    
    private void removeClipboardItem(int index) {
        if (index >= 0 && index < this.clipboardHistory.size()) {
            this.clipboardHistory.remove(index);
            this.saveClipboardHistory();
            this.refreshClipboardUI();
        }
    }
    
    private void clearAllClipboard() {
        this.clipboardHistory.clear();
        this.saveClipboardHistory();
        this.refreshClipboardUI();
    }
    
    private void showClipboardMode() {
        if (this.isAnimating || this.isClipboardMode) {
            return;
        }
        
        this.isClipboardMode = true;
        this.isQuickMode = false;
        this.isAnimating = true;
        
        this.hideSpotifyTemporarily();
        this.scrollingQuoteText.setVisible(false);
        
        this.animateFadeOut(this.quickSettingsContainer, 150);
        
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            this.quickSettingsContainer.setVisible(false);
            
            double targetWidth = 380.0;
            double targetHeight = 140.0;
            double targetRadius = 30.0;
            
            Timeline expandAnimation = new Timeline(
                new KeyFrame(Duration.millis(400.0), 
                    new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo)),
                    new KeyValue(this.islandHeight, targetHeight, new CustomInterpolator(Animation::easeOutExpo)),
                    new KeyValue(this.cornerRadius, targetRadius, new CustomInterpolator(Animation::easeOutExpo))
                )
            );
            
            expandAnimation.setOnFinished(ev -> {
                Rectangle rect = (Rectangle)this.islandShape;
                
                DropShadow enhancedShadow = new DropShadow();
                enhancedShadow.setBlurType(BlurType.GAUSSIAN);
                enhancedShadow.setColor(Color.rgb(0, 0, 0, 0.6));
                enhancedShadow.setRadius(25.0);
                enhancedShadow.setSpread(0.2);
                enhancedShadow.setOffsetX(0.0);
                enhancedShadow.setOffsetY(4.0);
                
                if (this.configManager.getConfig().blurEnabled) {
                    enhancedShadow.setInput(this.backgroundBlur);
                }
                rect.setEffect(enhancedShadow);
                
                this.refreshClipboardUI();
                this.clipboardContainer.toFront();
                this.clipboardContainer.setVisible(true);
                this.animateFadeInNode(this.clipboardContainer, 200);
                
                PauseTransition unlockDelay = new PauseTransition(Duration.millis(200.0));
                unlockDelay.setOnFinished(evv -> {
                    this.isAnimating = false;
                });
                unlockDelay.play();
            });
            
            expandAnimation.play();
        });
        
        fadeOutDelay.play();
    }
    
    private void hideClipboardMode() {
        if (!this.isClipboardMode || this.isAnimating) {
            return;
        }
        
        this.isClipboardMode = false;
        this.isAnimating = true;
        
        this.animateFadeOut(this.clipboardContainer, 200);
        
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(200.0));
        fadeOutDelay.setOnFinished(e -> {
            this.clipboardContainer.setVisible(false);
            
            Timeline shrinkAnimation = new Timeline(
                new KeyFrame(Duration.millis(400.0), 
                    new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo)),
                    new KeyValue(this.islandHeight, 50, new CustomInterpolator(Animation::easeInExpo)),
                    new KeyValue(this.cornerRadius, 25, new CustomInterpolator(Animation::easeInExpo))
                )
            );
            
            shrinkAnimation.setOnFinished(ev -> {
                Rectangle rect = (Rectangle)this.islandShape;
                
                DropShadow normalShadow = new DropShadow();
                normalShadow.setBlurType(BlurType.GAUSSIAN);
                normalShadow.setColor(Color.rgb(0, 0, 0, 0.4));
                normalShadow.setRadius(15.0);
                normalShadow.setSpread(0.3);
                normalShadow.setOffsetX(0.0);
                normalShadow.setOffsetY(0.0);
                
                if (this.configManager.getConfig().blurEnabled) {
                    normalShadow.setInput(this.backgroundBlur);
                }
                rect.setEffect(normalShadow);
                this.showNormalContentWithAnimation();
            });
            shrinkAnimation.play();
        });
        
        fadeOutDelay.play();
    }

    private void createIdleUI() {
        this.idleTextLabel = new Label();
        this.updateIdleTextStyle();
        this.idleTextLabel.setMaxWidth(220);
        this.idleTextLabel.setWrapText(true);
        this.idleTextLabel.setVisible(false);
        this.idleTextLabel.setOpacity(0.0);
        this.idleTextLabel.layoutXProperty().bind(this.timeLabel.layoutXProperty().add(50));
        this.idleTextLabel.layoutYProperty().bind(this.timeLabel.layoutYProperty().add(5));
        this.islandContainer.getChildren().add(this.idleTextLabel);
    }
    
    private void initIdleTimer() {
        this.createIdleUI();
        this.idleTimer = new Timeline(new KeyFrame(Duration.millis(300000.0), e -> this.startIdleMode()));
        this.idleTimer.setCycleCount(1);
        this.idleTimer.play();
    }
    
    private void resetIdleTimer() {
        if (this.isIdleMode) {
            this.exitIdleMode();
        }
        if (this.isStopwatchMode || this.isTranslateMode || this.isClipboardMode || 
            this.isQuickMode || this.isSearchMode || this.isButtonMode || 
            this.isThemeMode || this.isOptionMode || this.isNoticeMode) {
            return;
        }
        if (this.idleTimer != null) {
            this.idleTimer.stop();
        }
        this.idleTimer = new Timeline(new KeyFrame(Duration.millis(60000.0), e -> this.startIdleMode()));
        this.idleTimer.setCycleCount(1);
        this.idleTimer.play();
    }
    
    private void startIdleMode() {
        if (this.isIdleMode || this.isAnimating || this.isStopwatchMode || 
            this.isTranslateMode || this.isClipboardMode || this.isQuickMode || 
            this.isSearchMode || this.isButtonMode || this.isThemeMode || 
            this.isOptionMode || this.isNoticeMode) {
            return;
        }
        this.isIdleMode = true;
        this.hideAllInteractiveModes();
        
        this.animateFadeOut(this.scrollingQuoteText, 300);
        this.animateFadeOut(this.logoLabel, 300);
        this.animateFadeOut(this.dateLabel, 300);
        this.animateFadeOut(this.weekLabel, 300);
        this.animateFadeOut(this.kaomojiLabel, 300);
        
        if (this.spotifyTrackLabel.isVisible()) {
            this.animateFadeOut(this.spotifyTrackLabel, 300);
        }
        if (this.spotifyArtistLabel.isVisible()) {
            this.animateFadeOut(this.spotifyArtistLabel, 300);
        }
        if (this.spotifyStatusLabel.isVisible()) {
            this.animateFadeOut(this.spotifyStatusLabel, 300);
        }
        
        PauseTransition delay = new PauseTransition(Duration.millis(300));
        delay.setOnFinished(e -> {
            this.scrollingQuoteText.setVisible(false);
            this.logoLabel.setVisible(false);
            this.dateLabel.setVisible(false);
            this.weekLabel.setVisible(false);
            this.kaomojiLabel.setVisible(false);
            
            if (this.spotifyTrackLabel.isVisible()) {
                this.spotifyTrackLabel.setVisible(false);
            }
            if (this.spotifyArtistLabel.isVisible()) {
                this.spotifyArtistLabel.setVisible(false);
            }
            if (this.spotifyStatusLabel.isVisible()) {
                this.spotifyStatusLabel.setVisible(false);
            }
            
            if (this.spotifyLyricLine1.isVisible()) {
                this.centerSpotifyLyricsForIdle();
            }
            
            int randomIndex = (int)(Math.random() * this.islandtext.length);
            this.idleTextLabel.setText(this.islandtext[randomIndex]);
            this.updateIdleTextStyle();
            this.idleTextLabel.setVisible(true);
            this.idleTextLabel.toFront();
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), this.idleTextLabel);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        delay.play();
        
        this.textRotationTimer = new Timeline(new KeyFrame(Duration.millis(4000.0), e -> this.updateIdleText()));
        this.textRotationTimer.setCycleCount(-1);
        this.textRotationTimer.play();
    }
    
    private void exitIdleMode() {
        if (!this.isIdleMode) {
            return;
        }
        this.isIdleMode = false;
        
        if (this.textRotationTimer != null) {
            this.textRotationTimer.stop();
        }
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), this.idleTextLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(ev -> {
            this.idleTextLabel.setVisible(false);
            
            this.logoLabel.setVisible(true);
            this.dateLabel.setVisible(true);
            this.weekLabel.setVisible(true);
            this.scrollingQuoteText.setVisible(true);
            
            this.animateFadeInNode(this.logoLabel, 400);
            this.animateFadeInNode(this.dateLabel, 400);
            this.animateFadeInNode(this.weekLabel, 400);
            this.animateFadeInNode(this.scrollingQuoteText, 400);
            
            this.restoreSpotifyFromIdle();
        });
        fadeOut.play();
    }
    
    private void updateIdleTextStyle() {
        if (this.configManager.getConfig().isDarkTheme) {
            this.idleTextLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.85); -fx-font-size: 11px; -fx-text-alignment: left;");
        } else {
            this.idleTextLabel.setStyle("-fx-text-fill: rgba(0,0,0,0.85); -fx-font-size: 11px; -fx-text-alignment: left;");
        }
    }
    
    private void updateIdleText() {
        if (this.islandtext.length > 0) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), this.idleTextLabel);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            
            fadeOut.setOnFinished(e -> {
                int randomIndex = (int)(Math.random() * this.islandtext.length);
                this.idleTextLabel.setText(this.islandtext[randomIndex]);
                this.updateIdleTextStyle();
                
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), this.idleTextLabel);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            
            fadeOut.play();
        }
    }
    
    private void centerSpotifyLyricsForIdle() {
        if (this.isMusicModeEnabled && (this.spotifyLyricLine1.isVisible() || this.spotifyLyricLine2.isVisible() || this.spotifyLyricLine3.isVisible())) {
            this.spotifyLyricLine1.setMaxWidth(250.0);
            this.spotifyLyricLine2.setMaxWidth(250.0);
            this.spotifyLyricLine3.setMaxWidth(250.0);
            
            this.spotifyLyricLine1.layoutXProperty().unbind();
            this.spotifyLyricLine2.layoutXProperty().unbind();
            this.spotifyLyricLine3.layoutXProperty().unbind();
            
            this.spotifyLyricLine1.layoutXProperty().bind(this.islandShape.layoutXProperty().add(this.islandWidth.divide(2)).subtract(this.spotifyLyricLine1.widthProperty().divide(2)));
            this.spotifyLyricLine2.layoutXProperty().bind(this.islandShape.layoutXProperty().add(this.islandWidth.divide(2)).subtract(this.spotifyLyricLine2.widthProperty().divide(2)));
            this.spotifyLyricLine3.layoutXProperty().bind(this.islandShape.layoutXProperty().add(this.islandWidth.divide(2)).subtract(this.spotifyLyricLine3.widthProperty().divide(2)));
        }
    }
    
    private void restoreSpotifyLyricsPosition() {
        this.spotifyLyricLine1.setMaxWidth(130.0);
        this.spotifyLyricLine2.setMaxWidth(130.0);
        this.spotifyLyricLine3.setMaxWidth(130.0);
        
        this.spotifyLyricLine1.layoutXProperty().unbind();
        this.spotifyLyricLine2.layoutXProperty().unbind();
        this.spotifyLyricLine3.layoutXProperty().unbind();
        
        this.spotifyLyricLine1.layoutXProperty().bind(this.islandShape.layoutXProperty().add(140));
        this.spotifyLyricLine2.layoutXProperty().bind(this.islandShape.layoutXProperty().add(140));
        this.spotifyLyricLine3.layoutXProperty().bind(this.islandShape.layoutXProperty().add(140));
    }
    
    private void restoreSpotifyFromIdle() {
        if (this.isMusicModeEnabled) {
            this.restoreSpotifyLyricsPosition();
            this.restoreSpotifyDisplay();
        } else {
            this.restoreSpotifyLyricsPosition();
            
            this.spotifyTrackLabel.setVisible(false);
            this.spotifyArtistLabel.setVisible(false);
            this.spotifyStatusLabel.setVisible(false);
            this.spotifyLyricLine1.setVisible(false);
            this.spotifyLyricLine2.setVisible(false);
            this.spotifyLyricLine3.setVisible(false);
            
            this.spotifyTrackLabel.setOpacity(1.0);
            this.spotifyArtistLabel.setOpacity(1.0);
            this.spotifyStatusLabel.setOpacity(1.0);
            this.spotifyLyricLine1.setOpacity(0.4);
            this.spotifyLyricLine2.setOpacity(1.0);
            this.spotifyLyricLine3.setOpacity(0.4);
        }
    }
    
    private void createStopwatchUI() {
        this.stopwatchContainer = new VBox(8.0);
        this.stopwatchContainer.setAlignment(Pos.CENTER);
        this.stopwatchContainer.setVisible(false);
        
        this.stopwatchTimeLabel = new Label("00:00.00");
        this.stopwatchTimeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-alignment: center;");
        
        HBox buttonContainer = new HBox(10.0);
        buttonContainer.setAlignment(Pos.CENTER);
        
        this.stopwatchStartStopBtn = new Button("Start");
        this.stopwatchStartStopBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 15; -fx-pref-width: 60; -fx-pref-height: 25; -fx-font-size: 10px; -fx-font-weight: bold;");
        this.stopwatchStartStopBtn.setOnAction(e -> this.toggleStopwatch());
        
        this.stopwatchResetBtn = new Button("Reset");
        this.stopwatchResetBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 15; -fx-pref-width: 60; -fx-pref-height: 25; -fx-font-size: 10px; -fx-font-weight: bold;");
        this.stopwatchResetBtn.setOnAction(e -> this.resetStopwatch());
        
        buttonContainer.getChildren().addAll(this.stopwatchStartStopBtn, this.stopwatchResetBtn);
        this.stopwatchContainer.getChildren().addAll(this.stopwatchTimeLabel, buttonContainer);
        
        this.stopwatchContainer.layoutXProperty().bind(this.islandShape.layoutXProperty().add(this.islandWidth.divide(2)).subtract(this.stopwatchContainer.widthProperty().divide(2)));
        this.stopwatchContainer.layoutYProperty().bind(this.islandShape.layoutYProperty().add(20));
        
        this.islandContainer.getChildren().add(this.stopwatchContainer);
        
        this.stopwatchTimer = new Timeline(new KeyFrame(Duration.millis(10), e -> this.updateStopwatchDisplay()));
        this.stopwatchTimer.setCycleCount(-1);
    }
    
    private void showStopwatchMode() {
        if (this.isAnimating || this.isStopwatchMode) {
            return;
        }
        
        this.isStopwatchMode = true;
        this.isQuickMode = false;
        this.isAnimating = true;
        
        this.hideSpotifyTemporarily();
        this.scrollingQuoteText.setVisible(false);
        
        this.animateFadeOut(this.quickSettingsContainer, 150);
        
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            this.quickSettingsContainer.setVisible(false);
            
            double targetWidth = 280.0;
            double targetHeight = 120.0;
            
            Timeline expandAnimation = new Timeline(
                new KeyFrame(Duration.millis(400.0), 
                    new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo)),
                    new KeyValue(this.islandHeight, targetHeight, new CustomInterpolator(Animation::easeOutExpo))
                )
            );
            
            expandAnimation.setOnFinished(ev -> {
                if (this.idleTimer != null) {
                    this.idleTimer.stop();
                }
                this.stopwatchContainer.toFront();
                this.stopwatchContainer.setVisible(true);
                this.animateFadeInNode(this.stopwatchContainer, 200);
                this.isAnimating = false;
            });
            
            expandAnimation.play();
        });
        
        fadeOutDelay.play();
    }
    
    private void hideStopwatchMode() {
        if (!this.isStopwatchMode || this.isAnimating) {
            return;
        }
        
        this.isStopwatchMode = false;
        this.isAnimating = true;
        
        this.animateFadeOut(this.stopwatchContainer, 200);
        
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(200.0));
        fadeOutDelay.setOnFinished(e -> {
            this.stopwatchContainer.setVisible(false);
            
            Timeline shrinkAnimation = new Timeline(
                new KeyFrame(Duration.millis(400.0), 
                    new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo)),
                    new KeyValue(this.islandHeight, 50, new CustomInterpolator(Animation::easeInExpo))
                )
            );
            
            shrinkAnimation.setOnFinished(ev -> {
                this.showNormalContentWithAnimation();
                this.resetIdleTimer();
            });
            shrinkAnimation.play();
        });
        
        fadeOutDelay.play();
    }
    
    private void toggleStopwatch() {
        if (this.isStopwatchRunning) {
            this.stopStopwatch();
        } else {
            this.startStopwatch();
        }
    }
    
    private void startStopwatch() {
        this.stopwatchStartTime = System.currentTimeMillis() - this.stopwatchElapsedTime;
        this.isStopwatchRunning = true;
        this.stopwatchStartStopBtn.setText("Stop");
        this.stopwatchStartStopBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 15; -fx-pref-width: 60; -fx-pref-height: 25; -fx-font-size: 10px; -fx-font-weight: bold;");
        this.stopwatchTimer.play();
    }
    
    private void stopStopwatch() {
        this.isStopwatchRunning = false;
        this.stopwatchStartStopBtn.setText("Start");
        this.stopwatchStartStopBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 15; -fx-pref-width: 60; -fx-pref-height: 25; -fx-font-size: 10px; -fx-font-weight: bold;");
        this.stopwatchTimer.stop();
        this.stopwatchElapsedTime = System.currentTimeMillis() - this.stopwatchStartTime;
    }
    
    private void resetStopwatch() {
        this.stopStopwatch();
        this.stopwatchElapsedTime = 0;
        this.updateStopwatchDisplay();
    }
    
    private void updateStopwatchDisplay() {
        long currentTime;
        if (this.isStopwatchRunning) {
            currentTime = System.currentTimeMillis() - this.stopwatchStartTime;
        } else {
            currentTime = this.stopwatchElapsedTime;
        }
        
        long minutes = currentTime / 60000;
        long seconds = (currentTime % 60000) / 1000;
        long centiseconds = (currentTime % 1000) / 10;
        
        String timeText = String.format("%02d:%02d.%02d", minutes, seconds, centiseconds);
        this.stopwatchTimeLabel.setText(timeText);
    }
    
    private void createTranslateUI() {
        this.translateContainer = new HBox(10.0);
        this.translateContainer.setAlignment(Pos.CENTER);
        this.translateContainer.setVisible(false);
        
        this.translateInputField = new TextField();
        this.translateInputField.setPromptText("Empty...");
        this.translateInputField.setPrefWidth(130.0);
        this.translateInputField.setPrefHeight(25.0);
        this.translateInputField.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-prompt-text-fill: #888888; -fx-border-color: white; -fx-border-radius: 5; -fx-border-width: 1;");
        
        this.translateArrow = new javafx.scene.shape.Polygon();
        this.translateArrow.getPoints().addAll(new Double[]{
            0.0, 0.0,
            10.0, 5.0,
            0.0, 10.0,
            2.0, 5.0
        });
        this.translateArrow.setFill(Color.WHITE);
        
        this.translateOutputArea = new TextArea("");
        this.translateOutputArea.setPrefWidth(130.0);
        this.translateOutputArea.setPrefHeight(25.0);
        this.translateOutputArea.setWrapText(true);
        this.translateOutputArea.setEditable(false);
        this.translateOutputArea.setStyle("-fx-text-fill: white; -fx-control-inner-background: transparent; -fx-border-color: white; -fx-border-radius: 5; -fx-border-width: 1; -fx-padding: 2; -fx-background-color: transparent; -fx-background: transparent; -fx-faint-focus-color: transparent; -fx-focus-color: transparent;");
        
        Platform.runLater(() -> {
            this.translateOutputArea.lookup(".scroll-bar:vertical").setVisible(false);
            this.translateOutputArea.lookup(".scroll-bar:horizontal").setVisible(false);
            if (this.translateOutputArea.lookup(".corner") != null) {
                this.translateOutputArea.lookup(".corner").setVisible(false);
            }
        });
        this.translateOutputArea.setFocusTraversable(false);
        
        this.translateContainer.getChildren().addAll(this.translateInputField, this.translateArrow, this.translateOutputArea);
        
        this.translateContainer.layoutXProperty().bind(this.islandShape.layoutXProperty().add(15));
        this.translateContainer.layoutYProperty().bind(this.islandShape.layoutYProperty().add(this.islandHeight.divide(2)).subtract(this.translateContainer.heightProperty().divide(2)));
        
        this.islandContainer.getChildren().add(this.translateContainer);
        
        this.translateInputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                this.scheduleTranslation(newValue);
            }
        });
    }
    
    private void showTranslateMode() {
        if (this.isAnimating || this.isTranslateMode) {
            return;
        }
        
        this.isTranslateMode = true;
        this.isQuickMode = false;
        this.isAnimating = true;
        
        this.hideSpotifyTemporarily();
        this.scrollingQuoteText.setVisible(false);
        
        this.animateFadeOut(this.quickSettingsContainer, 150);
        
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(150.0));
        fadeOutDelay.setOnFinished(e -> {
            this.quickSettingsContainer.setVisible(false);
            
            double targetWidth = 320.0;
            
            Timeline expandAnimation = new Timeline(
                new KeyFrame(Duration.millis(400.0), 
                    new KeyValue(this.islandWidth, targetWidth, new CustomInterpolator(Animation::easeOutExpo))
                )
            );
            
            expandAnimation.setOnFinished(ev -> {
                if (this.idleTimer != null) {
                    this.idleTimer.stop();
                }
                this.updateTranslateTheme();
                this.translateContainer.toFront();
                this.translateContainer.setVisible(true);
                this.animateFadeInNode(this.translateContainer, 200);
                this.translateInputField.requestFocus();
                this.isAnimating = false;
            });
            
            expandAnimation.play();
        });
        
        fadeOutDelay.play();
    }
    
    private void hideTranslateMode() {
        if (!this.isTranslateMode || this.isAnimating) {
            return;
        }
        
        this.isTranslateMode = false;
        this.isAnimating = true;
        
        // 清理防抖定时器
        if (this.translationDebounceTimer != null) {
            this.translationDebounceTimer.stop();
            this.translationDebounceTimer = null;
        }
        
        this.animateFadeOut(this.translateContainer, 200);
        
        PauseTransition fadeOutDelay = new PauseTransition(Duration.millis(200.0));
        fadeOutDelay.setOnFinished(e -> {
            this.translateContainer.setVisible(false);
            this.translateInputField.clear();
            this.translateOutputArea.clear();
            
            Timeline shrinkAnimation = new Timeline(
                new KeyFrame(Duration.millis(400.0), 
                    new KeyValue(this.islandWidth, 280, new CustomInterpolator(Animation::easeInExpo))
                )
            );
            
            shrinkAnimation.setOnFinished(ev -> {
                this.showNormalContentWithAnimation();
                this.resetIdleTimer();
            });
            shrinkAnimation.play();
        });
        
        fadeOutDelay.play();
    }
    
    private void updateTranslateTheme() {
        boolean isDark = this.configManager.getConfig().isDarkTheme;
        String textColor = isDark ? "black" : "white";
        String borderColor = isDark ? "black" : "white";
        String promptColor = isDark ? "#666666" : "#888888";
        
        this.translateInputField.setStyle("-fx-background-color: transparent; -fx-text-fill: " + textColor + "; -fx-prompt-text-fill: " + promptColor + "; -fx-border-color: " + borderColor + "; -fx-border-radius: 5; -fx-border-width: 1;");
        this.translateOutputArea.setStyle("-fx-text-fill: " + textColor + "; -fx-control-inner-background: transparent; -fx-border-color: " + borderColor + "; -fx-border-radius: 5; -fx-border-width: 1; -fx-padding: 2; -fx-background-color: transparent; -fx-background: transparent; -fx-faint-focus-color: transparent; -fx-focus-color: transparent;");
        
        Platform.runLater(() -> {
            if (this.translateOutputArea.lookup(".scroll-bar:vertical") != null) {
                this.translateOutputArea.lookup(".scroll-bar:vertical").setVisible(false);
            }
            if (this.translateOutputArea.lookup(".scroll-bar:horizontal") != null) {
                this.translateOutputArea.lookup(".scroll-bar:horizontal").setVisible(false);
            }
            if (this.translateOutputArea.lookup(".corner") != null) {
                this.translateOutputArea.lookup(".corner").setVisible(false);
            }
        });
        this.translateArrow.setFill(isDark ? Color.BLACK : Color.WHITE);
    }
    
    private void scheduleTranslation(String input) {
        // 取消之前的定时器
        if (this.translationDebounceTimer != null) {
            this.translationDebounceTimer.stop();
        }
        
        // 如果输入为空，立即清空输出
        if (input == null || input.trim().isEmpty()) {
            this.translateOutputArea.setText("");
            return;
        }
        
        // 创建新的防抖定时器，延迟800毫秒后执行翻译
        this.translationDebounceTimer = new Timeline(new KeyFrame(Duration.millis(800.0), e -> {
            this.performTranslation(input);
        }));
        this.translationDebounceTimer.play();
    }
    
    private void performTranslation(String input) {
        if (input == null || input.trim().isEmpty()) {
            this.translateOutputArea.setText("");
            return;
        }
        
        Thread translationThread = new Thread(() -> {
            String result = this.translateText(input.trim());
            Platform.runLater(() -> {
                if (this.isTranslateMode) {
                    this.translateOutputArea.setText(result);
                }
            });
        });
        translationThread.setDaemon(true);
        translationThread.start();
    }
    
    private String translateText(String text) {
        if (this.isChinese(text)) {
            return this.translateToEnglish(text);
        } else if (this.isEnglish(text)) {
            return this.translateToChinese(text);
        } else {
            return this.translateToChinese(text);
        }
    }
    
    private boolean isChinese(String text) {
        return text.matches(".*[\\u4e00-\\u9fa5].*");
    }
    
    private boolean isEnglish(String text) {
        return text.matches("^[a-zA-Z\\s.,!?;:'-]+$");
    }
    
    private void initBaiduTranslateApi() {
        if (ApiKey.isBaiduTranslateConfigured()) {
            String[] config = ApiKey.getBaiduTranslateConfig();
            this.baiduTransApi = new sb.justwindow.translate.TransApi(config[0], config[1]);
        } else {
            System.out.println("百度翻译API未配置，请在ApiKey.java中设置相关密钥");
        }
    }
    
    private String translateToEnglish(String chineseText) {
        if (this.baiduTransApi == null) {
            return "请配置百度翻译API";
        }
        
        try {
            String result = this.baiduTransApi.getTransResult(chineseText, "zh", "en");
            return this.parseTranslationResult(result);
        } catch (Exception e) {
            return "翻译失败: " + e.getMessage();
        }
    }
    
    private String translateToChinese(String text) {
        if (this.baiduTransApi == null) {
            return "请配置百度翻译API";
        }
        
        try {
            String result = this.baiduTransApi.getTransResult(text, "auto", "zh");
            return this.parseTranslationResult(result);
        } catch (Exception e) {
            return "翻译失败: " + e.getMessage();
        }
    }
    
    private String parseTranslationResult(String jsonResult) {
        try {
            sb.justwindow.translate.TranslationResult result = sb.justwindow.translate.TranslationResult.parseFromJson(jsonResult);
            if (result != null && result.getTransResult() != null && !result.getTransResult().isEmpty()) {
                return result.getTransResult().get(0).getDst();
            } else if (result != null && result.getErrorCode() != null) {
                return "错误: " + result.getErrorMsg();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "解析翻译结果失败";
    }
    
    private void loadClipboardHistory() {
        try {
            File file = new File(this.clipboardFilePath);
            if (file.exists()) {
                String json = new String(Files.readAllBytes(Paths.get(this.clipboardFilePath)));
                if (!json.trim().isEmpty()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<String>>(){}.getType();
                    this.clipboardHistory = gson.fromJson(json, listType);
                    if (this.clipboardHistory == null) {
                        this.clipboardHistory = new ArrayList<>();
                    }
                } else {
                    this.clipboardHistory = new ArrayList<>();
                }
            } else {
                this.clipboardHistory = new ArrayList<>();
            }
        } catch (Exception e) {
            this.clipboardHistory = new ArrayList<>();
        }
    }
    
    private void saveClipboardHistory() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this.clipboardHistory);
            FileWriter writer = new FileWriter(this.clipboardFilePath);
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}