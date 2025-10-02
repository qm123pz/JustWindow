package sb.justwindow;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class WindowsUtils {
    private static final int GWL_EXSTYLE = -20;
    private static final int WS_EX_TOOLWINDOW = 0x00000080;
    private static final int WS_EX_APPWINDOW = 0x00040000;

    interface User32Ex extends User32 {
        User32Ex INSTANCE = Native.load("user32", User32Ex.class);
        WinDef.HWND FindWindowW(String lpClassName, String lpWindowName);
    }

    public static void hideFromTaskbar(javafx.stage.Stage stage) {
        try {
            WinDef.HWND hwnd = getWindowHandle(stage);
            if (hwnd == null) return;

            int style = User32.INSTANCE.GetWindowLong(hwnd, GWL_EXSTYLE);
            style &= ~WS_EX_APPWINDOW;
            style |= WS_EX_TOOLWINDOW;

            User32.INSTANCE.SetWindowLong(hwnd, GWL_EXSTYLE, style);
            User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_HIDE);
            User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_SHOW);
        } catch (Exception e) {
        }
    }

    public static void showInTaskbar(javafx.stage.Stage stage) {
        try {
            WinDef.HWND hwnd = getWindowHandle(stage);
            if (hwnd == null) return;

            int style = User32.INSTANCE.GetWindowLong(hwnd, GWL_EXSTYLE);
            style |= WS_EX_APPWINDOW;
            style &= ~WS_EX_TOOLWINDOW;

            User32.INSTANCE.SetWindowLong(hwnd, GWL_EXSTYLE, style);
            User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_HIDE);
            User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_SHOW);
        } catch (Exception e) {
        }
    }

    private static WinDef.HWND getWindowHandle(javafx.stage.Stage stage) {
        try {
            String title = stage.getTitle();
            if (title != null && !title.isEmpty()) {
                WinDef.HWND hwnd = User32Ex.INSTANCE.FindWindowW(null, title);
                if (hwnd != null) return hwnd;
            }
            return findJavaFXWindow();
        } catch (Exception e) {
            return null;
        }
    }

    private static WinDef.HWND findJavaFXWindow() {
        final WinDef.HWND[] result = new WinDef.HWND[1];

        User32.INSTANCE.EnumWindows((hwnd, data) -> {
            char[] className = new char[512];
            User32.INSTANCE.GetClassName(hwnd, className, 512);
            String classNameStr = Native.toString(className);

            if (classNameStr.toLowerCase().contains("javafx") ||
                    classNameStr.toLowerCase().contains("glass")) {
                result[0] = hwnd;
                return false;
            }
            return true;
        }, null);

        return result[0];
    }
}