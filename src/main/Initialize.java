/**
 * @author		GigiaJ
 * @filename	Initialize.java
 * @date		Mar 27, 2020
 * @description 
 */

package main;

import java.awt.AWTException;
import java.awt.Component;
import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;
import messages.GlobalListeners;

public class Initialize extends Thread {
    public Initialize() {
    }

    public synchronized void run() {
        while(Main.getToken() == "") {
            try {
                this.wait(2000L);
            } catch (InterruptedException var6) {
                var6.printStackTrace();
            }
        }

        try {
            try {
                Main.createJDA();
            } catch (InterruptedException var4) {
                var4.printStackTrace();
            }
        } catch (LoginException var5) {
            JOptionPane.showMessageDialog((Component)null, var5.toString(), "Error", 0);
            var5.printStackTrace();
        }

        try {
            GlobalListeners.mouseListener();
            GlobalListeners.keyboardListener();
            FileLoader.loadFiles();
        } catch (InterruptedException | AWTException var2) {
            var2.printStackTrace();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
