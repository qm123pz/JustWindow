package sb.justwindow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class SpotifyAPI {
    private SpotifyAuth auth;
    
    public SpotifyAPI() {
        auth = new SpotifyAuth();
    }
    
    public void startAuthorization(SpotifyAuth.AuthCallback callback) {
        auth.startAuth(callback);
    }
    
    public static class SpotifyTrack {
        public String trackName = "";
        public String artistName = "";
        public String albumName = "";
        public int progressMs = 0;
        public int durationMs = 0;
        public boolean isPlaying = false;
        public String trackId = "";
        
        public boolean isEmpty() {
            return trackName.isEmpty();
        }

    }
    
    public static class LyricLine {
        public long startTimeMs;
        public String text;
        
        public LyricLine(long startTimeMs, String text) {
            this.startTimeMs = startTimeMs;
            this.text = text;
        }
    }
    
    public SpotifyTrack getCurrentTrack() {
        SpotifyTrack track = new SpotifyTrack();
        
        if (!auth.hasValidToken()) {
            return track;
        }
        
        try {
            String apiUrl = "https://api.spotify.com/v1/me/player/currently-playing";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + auth.getAccessToken());
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            
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
                track.isPlaying = json.optBoolean("is_playing", false);
                track.progressMs = json.optInt("progress_ms", 0);
                
                JSONObject item = json.optJSONObject("item");
                if (item != null) {
                    track.trackName = item.optString("name", "");
                    track.trackId = item.optString("id", "");
                    track.durationMs = item.optInt("duration_ms", 0);
                    
                    if (item.has("artists")) {
                        StringBuilder artists = new StringBuilder();
                        var artistsArray = item.getJSONArray("artists");
                        for (int i = 0; i < artistsArray.length(); i++) {
                            if (i > 0) artists.append(", ");
                            artists.append(artistsArray.getJSONObject(i).optString("name", ""));
                        }
                        track.artistName = artists.toString();
                    }
                    
                    JSONObject album = item.optJSONObject("album");
                    if (album != null) {
                        track.albumName = album.optString("name", "");
                    }
                }
            } else if (responseCode == 204) {
                return track;
            }
            
            conn.disconnect();
        } catch (Exception e) {
        }
        
        return track;
    }
    
    public boolean hasValidToken() {
        return auth.hasValidToken();
    }
    
    public boolean isAuthorized() {
        return auth.hasValidToken();
    }
    
    public java.util.List<LyricLine> getLyrics(String trackName, String artistName) {
        java.util.List<LyricLine> lyrics = new java.util.ArrayList<>();
        
        if (trackName == null || trackName.isEmpty()) {
            return lyrics;
        }
        
        try {
            String apiUrl = "https://lrclib.net/api/search?track_name=" + 
                java.net.URLEncoder.encode(trackName, "UTF-8") + 
                "&artist_name=" + java.net.URLEncoder.encode(artistName, "UTF-8");
            
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "JustWindow/1.0 (https://github.com)");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                
                String jsonStr = response.toString();

                org.json.JSONArray jsonArray = new org.json.JSONArray(jsonStr);
                
                if (jsonArray.length() == 0) {
                    System.out.println("No results from lrclib");
                    lyrics.add(new LyricLine(0, "♪ " + trackName));
                    lyrics.add(new LyricLine(2000, "by " + artistName));
                    lyrics.add(new LyricLine(4000, "Enjoy the music ♫"));
                    return lyrics;
                }

                JSONObject firstResult = jsonArray.getJSONObject(0);
                String syncedLyrics = firstResult.optString("syncedLyrics", "");
                String plainLyrics = firstResult.optString("plainLyrics", "");

                if (!syncedLyrics.isEmpty()) {
                    lyrics.addAll(parseLRCFormat(syncedLyrics));
                } else if (!plainLyrics.isEmpty()) {
                    String[] linesArray = plainLyrics.split("\\r?\\n");
                    long timeMs = 0;
                    
                    for (String lyricLine : linesArray) {
                        String trimmed = lyricLine.trim();
                        if (!trimmed.isEmpty() && trimmed.length() < 100) {
                            lyrics.add(new LyricLine(timeMs, trimmed));
                            timeMs += 3000;
                        }
                    }
                } else {
                    lyrics.add(new LyricLine(0, "♪ No lyrics available"));
                }

            } else {

                if (responseCode == 404) {
                    lyrics.add(new LyricLine(0, "♪ " + trackName));
                    lyrics.add(new LyricLine(2000, "by " + artistName));
                    lyrics.add(new LyricLine(4000, "Enjoy the music ♫"));
                } else {
                    lyrics.add(new LyricLine(0, "♪ Lyrics unavailable"));
                    lyrics.add(new LyricLine(1000, "Enjoy the music"));
                    lyrics.add(new LyricLine(2000, ""));
                }
            }
            
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            lyrics.add(new LyricLine(0, "♪ Enjoy the music"));
            lyrics.add(new LyricLine(1000, ""));
            lyrics.add(new LyricLine(2000, ""));
        }
        
        return lyrics;
    }

    private java.util.List<LyricLine> parseLRCFormat(String lrcText) {
        java.util.List<LyricLine> lyrics = new java.util.ArrayList<>();
        String[] lines = lrcText.split("\\r?\\n");
        
        for (String line : lines) {
            if (line.contains("[") && line.contains("]")) {
                try {
                    int endBracket = line.indexOf("]");
                    String timeStr = line.substring(1, endBracket);
                    String text = line.substring(endBracket + 1).trim();
                    
                    if (text.isEmpty()) continue;

                    String[] parts = timeStr.split(":");
                    if (parts.length == 2) {
                        int minutes = Integer.parseInt(parts[0]);
                        double seconds = Double.parseDouble(parts[1]);
                        long timeMs = (long)(minutes * 60000 + seconds * 1000);
                        
                        lyrics.add(new LyricLine(timeMs, text));
                    }
                } catch (Exception e) {
                }
            }
        }
        
        return lyrics;
    }
}