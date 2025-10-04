package sb.justwindow;

import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.Base64;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpServer;

public class SpotifyAuth {
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String REDIRECT_URI;
    private static final String SCOPES = "user-read-currently-playing user-read-playback-state";
    private static final long TOKEN_REFRESH_THRESHOLD_MS = 60000; // 提前1分钟刷新

    private String accessToken = "";
    private String refreshToken = "";
    private long tokenExpireTime = 0;

    public interface AuthCallback {
        void onAuthComplete(boolean success);
    }

    /**
     * 构造函数 - 从配置文件加载Spotify配置
     */
    public SpotifyAuth() {
        this.CLIENT_ID = ApiKey.getSpotifyClientId();
        this.CLIENT_SECRET = ApiKey.getSpotifyClientSecret();
        this.REDIRECT_URI = ApiKey.getSpotifyRedirectUri();

        // 验证配置是否完整
        if (!ApiKey.isSpotifyConfigured()) {
            System.err.println("警告: Spotify配置不完整，请检查 api-config.yml");
        }
    }

    /**
     * 开始OAuth认证流程
     */
    public void startAuth(AuthCallback callback) {
        // 检查配置
        if (!ApiKey.isSpotifyConfigured()) {
            System.err.println("错误: Spotify未配置，无法进行认证");
            callback.onAuthComplete(false);
            return;
        }

        new Thread(() -> {
            try {
                // 从REDIRECT_URI中提取端口号
                URI uri = new URI(REDIRECT_URI);
                int port = uri.getPort() > 0 ? uri.getPort() : 8000;

                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                server.createContext("/callback", exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String response = "<html><body><h1>Authorization Complete!</h1><p>You can close this window.</p></body></html>";
                    if (query != null && query.contains("code=")) {
                        String code = query.split("code=")[1].split("&")[0];
                        if (exchangeCodeForToken(code)) {
                            response = "<html><body><h1>✓ Success!</h1><p>Spotify connected. You can close this window.</p></body></html>";
                            callback.onAuthComplete(true);
                        } else {
                            response = "<html><body><h1>✗ Failed!</h1><p>Failed to get access token.</p></body></html>";
                            callback.onAuthComplete(false);
                        }
                    }
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                            server.stop(0);
                        } catch (Exception e) {}
                    }).start();
                });
                server.start();
                System.out.println("✓ Auth server started on port " + port);

                String authUrl = "https://accounts.spotify.com/authorize?" +
                        "client_id=" + CLIENT_ID +
                        "&response_type=code" +
                        "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
                        "&scope=" + URLEncoder.encode(SCOPES, "UTF-8");

                Desktop.getDesktop().browse(new URI(authUrl));
                System.out.println("✓ Browser opened for authorization");

            } catch (Exception e) {
                System.err.println("Auth error: " + e.getMessage());
                callback.onAuthComplete(false);
            }
        }).start();
    }

    /**
     * 使用授权码交换访问令牌
     */
    private boolean exchangeCodeForToken(String code) {
        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String auth = CLIENT_ID + ":" + CLIENT_SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String data = "grant_type=authorization_code" +
                    "&code=" + code +
                    "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8");

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                accessToken = json.optString("access_token", "");
                refreshToken = json.optString("refresh_token", "");
                int expiresIn = json.optInt("expires_in", 3600);
                tokenExpireTime = System.currentTimeMillis() + (expiresIn * 1000);

                System.out.println("✓ Got access token (expires in " + expiresIn + "s)");
                return true;
            } else {
                // 读取错误响应
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorReader.close();
                System.err.println("Token exchange failed: " + errorResponse.toString());
            }

            conn.disconnect();
        } catch (Exception e) {
            System.err.println("Token exchange error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 刷新访问令牌
     */
    public boolean refreshAccessToken() {
        if (refreshToken.isEmpty()) {
            System.err.println("No refresh token available");
            return false;
        }

        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String auth = CLIENT_ID + ":" + CLIENT_SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String data = "grant_type=refresh_token&refresh_token=" + refreshToken;

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                accessToken = json.optString("access_token", "");
                int expiresIn = json.optInt("expires_in", 3600);
                tokenExpireTime = System.currentTimeMillis() + (expiresIn * 1000);

                System.out.println("✓ Refreshed access token");
                return true;
            } else {
                System.err.println("Token refresh failed with code: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            System.err.println("Token refresh error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取访问令牌（自动刷新）
     */
    public String getAccessToken() {
        // 如果令牌将在1分钟内过期，自动刷新
        if (System.currentTimeMillis() >= tokenExpireTime - TOKEN_REFRESH_THRESHOLD_MS) {
            if (!refreshAccessToken()) {
                System.err.println("Failed to refresh token");
            }
        }
        return accessToken;
    }

    /**
     * 检查是否有有效的令牌
     */
    public boolean hasValidToken() {
        return !accessToken.isEmpty() && System.currentTimeMillis() < tokenExpireTime;
    }

    /**
     * 获取令牌剩余有效时间（秒）
     */
    public long getTokenRemainingTime() {
        if (accessToken.isEmpty()) {
            return 0;
        }
        long remaining = (tokenExpireTime - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }
}