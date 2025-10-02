package sb.justwindow;

import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.Base64;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpServer;

public class SpotifyAuth {
    private static final String CLIENT_ID = ApiKey.SPOTIFY_CLIENT_ID;
    private static final String CLIENT_SECRET = ApiKey.SPOTIFY_CLIENT_SECRET;
    private static final String REDIRECT_URI = ApiKey.SPOTIFY_REDIRECT_URI;
    private static final String SCOPES = "user-read-currently-playing user-read-playback-state";
    
    private String accessToken = "";
    private String refreshToken = "";
    private long tokenExpireTime = 0;
    
    public interface AuthCallback {
        void onAuthComplete(boolean success);
    }
    
    public void startAuth(AuthCallback callback) {
        new Thread(() -> {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
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
                System.out.println("✓ Auth server started on port 8000");
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
            }
            
            conn.disconnect();
        } catch (Exception e) {
            System.err.println("Token exchange error: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean refreshAccessToken() {
        if (refreshToken.isEmpty()) {
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
            }
            
            conn.disconnect();
        } catch (Exception e) {
            System.err.println("Token refresh error: " + e.getMessage());
        }
        
        return false;
    }
    
    public String getAccessToken() {
        if (System.currentTimeMillis() >= tokenExpireTime - 60000) {
            refreshAccessToken();
        }
        return accessToken;
    }
    
    public boolean hasValidToken() {
        return !accessToken.isEmpty();
    }
}
