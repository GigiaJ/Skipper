package commands.color.colorchat;

import static messages.EmbedData.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.regex.Pattern;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.entities.Message;

public class ChatColorSetter {
	public static Color color;
	public static String hexColor = "";
	private static Pattern toFind = Pattern.compile("([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})");

	public static Color applyColor() throws Exception {
		if (bot.Bot.settings.getColorChatColor() != null) {
			String hex = bot.Bot.settings.getColorChatColor();
			hexColor = hex;
			int r = Color.decode(hex).getRed();
			int g = Color.decode(hex).getGreen();
			int b = Color.decode(hex).getBlue();

			float[] hsv = new float[3];
			float[] hsb = Color.RGBtoHSB(r, g, b, hsv);

			float h = 0;
			float s = 0;
			float v = 0;
			String hsbString = Arrays.toString(hsb);

			h = Float.parseFloat(hsbString.substring(hsbString.indexOf("[") + 1, hsbString.indexOf(",")).trim());
			s = Float.parseFloat(hsbString.substring(hsbString.indexOf(",") + 1, hsbString.lastIndexOf(",")).trim());
			v = Float
					.parseFloat(hsbString.substring(hsbString.lastIndexOf(",") + 1, hsbString.lastIndexOf("]")).trim());
			color = Color.getHSBColor(h, s, v);
		}
		return color;
	}

	public static Message updateChatColor() {
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		String filterCommandOut = rawMessageContent.replaceAll(commands.CommandList.cmdSetChatColor.getCommand(), "")
				.trim();
		if (!filterCommandOut.contains("#")) {
			filterCommandOut = "#" + filterCommandOut;
		}
		if (toFind.matcher(filterCommandOut).find()) {
			toFind.matcher(filterCommandOut).reset();
			String hex = filterCommandOut;
			hexColor = hex;
			int r = Color.decode(hex).getRed();
			int g = Color.decode(hex).getGreen();
			int b = Color.decode(hex).getBlue();

			float[] hsv = new float[3];
			float[] hsb = Color.RGBtoHSB(r, g, b, hsv);

			float h = 0;
			float s = 0;
			float v = 0;
			String hsbString = Arrays.toString(hsb);

			h = Float.parseFloat(hsbString.substring(hsbString.indexOf("[") + 1, hsbString.indexOf(",")).trim());
			s = Float.parseFloat(hsbString.substring(hsbString.indexOf(",") + 1, hsbString.lastIndexOf(",")).trim());
			v = Float
					.parseFloat(hsbString.substring(hsbString.lastIndexOf(",") + 1, hsbString.lastIndexOf("]")).trim());
			color = Color.getHSBColor(h, s, v);

			embColor = color;
			embContent = "Color successfully set to " + hex + ".";

			bot.Bot.settings = bot.Bot.settings.debuild().setColorChatColor(hex).build();
		} else {
			embContent = "Invalid color code. Please try again.";
		}
		return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("setchatcolor", cmdSign, "", CommandType.COLOR,
				"allows you to set the color of your color chat.", "thisCommand (Hexdecimal Color Code Here)")
						.addFirstExample("thisCommand #ffff00").addSecondExample("thisCommand #ffffff")
						.addMethod(ChatColorSetter.class.getMethod("updateChatColor")).build();
	}
}