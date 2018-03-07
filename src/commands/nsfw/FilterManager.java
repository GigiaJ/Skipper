package commands.nsfw;

import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.entities.Message;

public class FilterManager {
	final static String SAFE = "safe";
	final static String QUESTIONABLE = "questionable";
	final static String EXPLICIT = "explicit";
	final static int FILTER_COUNT = 3;

	static boolean[] filters = new boolean[FILTER_COUNT];

	public static void applySettings() {
		if (bot.Bot.settings.getNsfwFilters() != null) {
			filters = bot.Bot.settings.getNsfwFilters();
		}
	}

	public static Message changeFilter() {
		embColor = Color.MAGENTA;
		title = "NSFW Settings";

		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		rawMessageContent.replace(commands.CommandList.cmdFilterNSFW.getCommand() + " ", "");
		if (rawMessageContent.toLowerCase().contains(SAFE)) {
			filters[0] = !filters[0];
			if (filters[1] == filters[0] || filters[2] == filters[0]) {
				filters[1] = !filters[1];
				filters[2] = !filters[2];
				// Later include a notification as to why it reverts obviously being that you
				// can't search with no filters
			}
		}
		if (rawMessageContent.toLowerCase().contains(QUESTIONABLE)) {
			filters[1] = !filters[1];
			if (filters[0] != filters[1] || filters[2] != filters[1]) {
				filters[0] = !filters[0];
				filters[2] = !filters[2];
			}
		}
		if (rawMessageContent.toLowerCase().contains(EXPLICIT)) {
			filters[2] = !filters[2];
			if (filters[1] != filters[2] || filters[0] != filters[2]) {
				filters[1] = !filters[1];
				filters[0] = !filters[0];
			}
		}

		bot.Bot.settings = bot.Bot.settings.debuild().setNsfwFilters(filters).build();

		embContent = "Safe: " + Boolean.valueOf(filters[0]) + "\n" + "Questionable: " + Boolean.valueOf(filters[1])
				+ "\n" + "Explicit: " + Boolean.valueOf(filters[2]);
		return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);

	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("nsfwfilter", cmdSign, "", CommandType.NSFW,
				"allows you toggle what content the NSFW command returns.",
				"thisCommand (safe/questionable/explicit) (**Note: Two can be used at once)")
						.addFirstExample("thisCommand safe").addSecondExample("thisCommand safe questionable")
						.addMethod(FilterManager.class.getMethod("changeFilter")).build();
	}
}
