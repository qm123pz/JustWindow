package sb.justwindow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.*;

public class ConfigManager {
    private static final String CONFIG_DIR = System.getProperty("user.home") + "\\AppData\\NightSky";
    private static final String CONFIG_FILE = CONFIG_DIR + "\\JustWindow.json";
    private static ConfigManager instance;
    private Config config;

    public static class Config {
        public boolean autoStart = false;
        public boolean hideTaskbar = false;
        public boolean isDarkTheme = true;
        public boolean bloomEnabled = true;
        public boolean blurEnabled = false;
        public double alpha = 0.85;
    }

    private ConfigManager() {
        loadConfig();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public Config getConfig() {
        return config;
    }

    public void loadConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                String json = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
                Gson gson = new Gson();
                config = gson.fromJson(json, Config.class);
            } else {
                config = new Config();
                saveConfig();
            }
        } catch (IOException e) {
            config = new Config();
            System.err.println("Failed to load config: " + e.getMessage());
        }
    }

    public void saveConfig() {
        try {
            File dir = new File(CONFIG_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(config);
            Files.write(Paths.get(CONFIG_FILE), json.getBytes());
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    public void setAutoStart(boolean enabled) {
        config.autoStart = enabled;
        saveConfig();
        applyAutoStart(enabled);
    }

    public void setHideTaskbar(boolean enabled) {
        config.hideTaskbar = enabled;
        saveConfig();
    }

    private void applyAutoStart(boolean enabled) {
        try {
            String appPath = getExecutablePath();
            if (appPath == null || appPath.isEmpty()) {
                return;
            }

            if (enabled) {
                addToStartup(appPath);
            } else {
                removeFromStartup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getExecutablePath() {
        try {
            String path = ConfigManager.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath();
            
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            
            String absolutePath = file.getAbsolutePath();
            
            if (absolutePath.endsWith(".jar")) {
                String javaHome = System.getProperty("java.home");
                String javawPath = javaHome + "\\bin\\javaw.exe";
                return "\"" + javawPath + "\" -jar \"" + absolutePath + "\"";
            } else if (absolutePath.endsWith(".exe")) {
                return "\"" + absolutePath + "\"";
            } else {
                String javaHome = System.getProperty("java.home");
                String javawPath = javaHome + "\\bin\\javaw.exe";
                return "\"" + javawPath + "\" -jar \"" + absolutePath + "\"";
            }
        } catch (Exception e) {
            return null;
        }
    }

    private void addToStartup(String command) throws IOException {
        String regCommand = "cmd.exe /c reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\" " +
                "/v \"JustWindow\" /t REG_SZ /d " + command + " /f";
        
        Process process = Runtime.getRuntime().exec(regCommand);
        try {
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Successfully added to startup");
            } else {
                System.err.println("Failed to add to startup. Exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while adding to startup");
        }
    }

    private void removeFromStartup() throws IOException {
        String regCommand = "cmd.exe /c reg delete \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\" /v \"JustWindow\" /f";
        
        Process process = Runtime.getRuntime().exec(regCommand);
        try {
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Successfully removed from startup");
            } else {
                System.err.println("Failed to remove from startup. Exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while removing from startup");
        }
    }
}