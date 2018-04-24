package main;

import static main.Main.settings;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import bot.settings.SettingSaver;
import commands.general.help.HelpGUI;
import macro.MacroManager;

public class SystemTrayManager implements ItemListener, ActionListener {
	public static boolean systemTrayCheck() {
		if (!SystemTray.isSupported()) {
			return false;
		} else {
			return true;
		}
	}

	static Menu displayMenu = new Menu("Settings");
	static CheckboxMenuItem cb1 = new CheckboxMenuItem("Color Chat");
	static CheckboxMenuItem cb2 = new CheckboxMenuItem("Embedded Messages");
	static CheckboxMenuItem cb3 = new CheckboxMenuItem("Author Emb Messages");
	static CheckboxMenuItem cb4 = new CheckboxMenuItem("Auto Color");
	static MenuItem exit = new MenuItem("Exit");
	static MenuItem aboutItem = new MenuItem("Credits");
	static MenuItem macro = new MenuItem("Macro");
	static MenuItem help = new MenuItem("Help");
	static ItemListener listener = null;
	static BufferedImage img = null;
	static TrayIcon trayIcon = null;

	public SystemTrayManager() {
		if (OSCheck.isMac() == false) {
			if (systemTrayCheck() == true) {
				cb1.addItemListener(this);
				cb2.addItemListener(this);
				cb3.addItemListener(this);
				cb4.addItemListener(this);
				exit.addActionListener(this);
				macro.addActionListener(this);
				help.addActionListener(this);
				aboutItem.addActionListener(this);
				try {
					img = ImageIO.read(getClass().getResource("/images/SkipperTrayLogo.png"));
					trayIcon = new TrayIcon(img);
				} catch (IOException e1) {
				}
				
			}
		}
	}


	public static void executeTray(boolean initial) {
		if (OSCheck.isWindows()) {
			if (systemTrayCheck() == true) {
				final PopupMenu popup = new PopupMenu();
				// Create a pop-up menu components

				MenuItem colorChatFont = new MenuItem("Color Chat Font:" + settings.getCurrentFontStyle());
				MenuItem colorChatSize = new MenuItem("Color Chat Size:" + Integer.toString(settings.getCurrentFontSize()));
				MenuItem colorChatColor = new MenuItem("Color Chat Color:" + settings.getColorChatColor());
				MenuItem embColor = new MenuItem("Embedded Message Color:" + settings.getEmbedColor());

				if (initial == true) {
					listener = new SystemTrayManager();
					final SystemTray tray = SystemTray.getSystemTray();
					stateChange();
					// Add components to pop-up menu
					popup.add(aboutItem);
					popup.addSeparator();
					popup.add(cb1);
					popup.add(cb2);
					popup.add(cb3);
					popup.add(cb4);
					popup.addSeparator();
					popup.add(displayMenu);
					displayMenu.add(colorChatFont);
					displayMenu.add(colorChatSize);
					displayMenu.add(colorChatColor);
					displayMenu.add(embColor);
					popup.add(macro);
					popup.add(help);
					popup.add(exit);

					trayIcon.setPopupMenu(popup);
					try {
						tray.add(trayIcon);
					} catch (AWTException e) {
						System.out.println("TrayIcon could not be added.");
					}
				}
				if (initial == false) {
					stateChange();
					displayMenu.removeAll();
					displayMenu.add(colorChatFont);
					displayMenu.add(colorChatSize);
					displayMenu.add(colorChatColor);
					displayMenu.add(embColor);
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if (source == cb1) {
			cb1.setState(cb1.getState());
			systemTrayCheckBox(1);
		}
		if (source == cb2) {
			cb3.setState(false);
			cb2.setState(cb2.getState());
			systemTrayCheckBox(2);
		}
		if (source == cb3) {
			cb2.setState(false);
			cb3.setState(cb3.getState());
			systemTrayCheckBox(3);
		}
		if (source == cb4) {
			cb4.setState(cb4.getState());
			systemTrayCheckBox(4);
		}
		SettingSaver.saveSettings();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == exit) {
			SettingSaver.saveSettings();
			System.exit(0);
		}
		if (source == help) {
			HelpGUI.main(null);
		}
		if (source == macro) {
			MacroManager.main(null);
		}
		if (source == aboutItem) {
			CreditsInterface.main(null);
		}
	}
	
	private static void stateChange() {
		if (settings.getColorChatStatus() == true) {
			cb1.setState(true);
		} else {
			cb1.setState(false);
		}
		if (settings.getEmbedMessageStatus() == true) {
			cb3.setState(false);
			cb2.setState(true);
		} else {
			cb2.setState(false);
		}
		if (settings.getAuthorEmbedStatus() == true) {
			cb2.setState(false);
			cb3.setState(true);
		} else {
			cb3.setState(false);
		}
		if (settings.getAutoColorStatus() == true) {
			cb4.setState(true);
		} else {
			cb4.setState(false);
		}
	}
	
	static void systemTrayCheckBox(Integer settingToChange) {
		if (settingToChange == 1) {
			commands.color.colorchat.ColorChat.colorChat();
		}
		if (settingToChange == 2) {
			commands.general.MessageEmbeding.embedMessage();
		}
		if (settingToChange == 3) {
			commands.general.AuthorEmbeding.authorEmbedMessage();
		}
		if (settingToChange == 4) {
			commands.color.AutoColor.autoColor();
		}
		bot.settings.SettingSaver.saveSettings();
	}

}
