package messages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

import main.JNA;
import main.TextTransfer;

public class PreventMessageEvent {
	private static HHOOK hhk;
	private static LowLevelKeyboardProc keyboardHook;
	private static User32 lib;
	public static boolean messageDeleted = true;
	private static boolean second = false;

	public static void blockEnterKey() {

		if (isWindows()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					lib = User32.INSTANCE;
					HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
					keyboardHook = new LowLevelKeyboardProc() {
						public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
							if (nCode >= 0) {
								try {
									if (isDiscord()) {

										TextTransfer textTransfer = new TextTransfer();
										switch (info.vkCode) {

										case 0x0D:
											if (GlobalListeners.isWithinMessageBox == true
													&& GlobalListeners.autoCompleteMenuOpen == false) {
												
												Robot robot;

												try {
													robot = new Robot();
													robot.keyPress(KeyEvent.VK_CONTROL);
													robot.keyPress(KeyEvent.VK_A);
													robot.keyRelease(KeyEvent.VK_CONTROL);
													robot.keyRelease(KeyEvent.VK_A);
													robot.keyPress(KeyEvent.VK_CONTROL);
													robot.keyPress(KeyEvent.VK_X);
													robot.keyRelease(KeyEvent.VK_CONTROL);
													robot.keyRelease(KeyEvent.VK_X);

												} catch (AWTException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}

												if (second == true) {
													String lastTypedMessage = textTransfer.getClipboardContents();
													if (lastTypedMessage != null && !lastTypedMessage.isEmpty()
															&& lastTypedMessage != "") {
														messages.MessageEffects.triggerMessageEffects(lastTypedMessage);
														second = false;
														messageDeleted = false;
														textTransfer.setClipboardContents("");
														return new LRESULT(1);
													}
												}
												messageDeleted = false;
												second = true;
											}
											return null;
										default: // do nothing
										}

									}
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							return lib.CallNextHookEx(hhk, nCode, wParam, new LPARAM(info.getPointer().getLong(0)));
						}
					};
					hhk = lib.SetWindowsHookEx(13, keyboardHook, hMod, 0);

					// This bit never returns from GetMessage
					int result;
					MSG msg = new MSG();
					while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
						if (result == -1) {
							break;
						} else {
							lib.TranslateMessage(msg);
							lib.DispatchMessage(msg);
						}
					}
					lib.UnhookWindowsHookEx(hhk);
				}
			}).start();
		}

	}

	public static void unblockEnterKey() {
		if (isWindows() && lib != null) {
			lib.UnhookWindowsHookEx(hhk);
			messageDeleted = true;
		}
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}
	
	public static boolean isDiscord() {
		try {
			if (JNA.currentWindow().endsWith("- Discord")) {
				return true;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
