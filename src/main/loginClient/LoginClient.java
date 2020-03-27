/**
 * @author		GigiaJ
 * @filename	LoginClient.java
 * @date		Mar 27, 2020
 * @description 
 */


package main.loginClient;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.ProtocolHandler;
import com.teamdev.jxbrowser.chromium.ProtocolService;
import com.teamdev.jxbrowser.chromium.URLRequest;
import com.teamdev.jxbrowser.chromium.URLResponse;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.Component;
import java.awt.Toolkit;
import java.util.UUID;
import javax.swing.JFrame;
import main.Main;
import main.loginClient.jxLicense.JxBrowserHackUtil;
import main.loginClient.jxLicense.JxVersion;

public class LoginClient extends Thread {
    public LoginClient() {
    }

    public void run() {
        this.retrieveLoginToken();
    }

    public synchronized void retrieveLoginToken() {
        JxBrowserHackUtil.hack(JxVersion.V6_22);
        String identity = UUID.randomUUID().toString();
        BrowserContextParams params = new BrowserContextParams(Main.browserCookiesFolder.getAbsolutePath());
        final Browser browser = new Browser(new BrowserContext(params));
        BrowserView view = new BrowserView(browser);
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(0);
        frame.add(view, "Center");
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setTitle("Skipper");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/SkipperLogo.png")));
        BrowserContext browserContext = browser.getContext();
        ProtocolService protocolService = browserContext.getProtocolService();
        protocolService.setProtocolHandler("https", new ProtocolHandler() {
            public URLResponse onRequest(URLRequest request) {
                try {
                    if ((request.getRequestHeaders().getHeaders().get("Authorization")).get(0) != null && !(request.getRequestHeaders().getHeaders().get("Authorization")).get(0).isEmpty()) {
                        Main.setToken((String)(request.getRequestHeaders().getHeaders().get("Authorization")).get(0));
                        browser.getCacheStorage().clearCache();
                        browser.dispose();
                        frame.dispose();
                    }
                } catch (NullPointerException e) {
                    e.getMessage();
                }

                return null;
            }
        });
        browser.loadURL("https://discordapp.com/login");
    }
}

/*
class LoginClient$1 implements ProtocolHandler {
    LoginClient$1(LoginClient var1, Browser var2, JFrame var3) {
        this.this$0 = var1;
        this.val$browser = var2;
        this.val$frame = var3;
    }

    public URLResponse onRequest(URLRequest request) {
        try {
            if (((List)request.getRequestHeaders().getHeaders().get("Authorization")).get(0) != null && !((String)((List)request.getRequestHeaders().getHeaders().get("Authorization")).get(0)).isEmpty()) {
                Main.setToken((String)((List)request.getRequestHeaders().getHeaders().get("Authorization")).get(0));
                this.val$browser.getCacheStorage().clearCache();
                this.val$browser.dispose();
                this.val$frame.dispose();
            }
        } catch (NullPointerException var3) {
            var3.getMessage();
        }

        return null;
    }
}
*/

