package messages;

import java.util.List;
import java.util.concurrent.ExecutionException;

import handler.CommandHandler;
import net.dv8tion.jda.core.entities.Message;

public class Vandalizer extends CommandHandler {
	public static void vandalize() throws InterruptedException, ExecutionException {
		int i = 0;
		List<Message> messageHistory = channel.getHistory().retrievePast(100).submit().get();
		if (author.getId().equals("AUTHOR ID HERE") ) {
			messageHistory.get(i).addReaction("🇲").submit();
			messageHistory.get(i).addReaction("🇮").submit();
			messageHistory.get(i).addReaction("🇩").submit();
			messageHistory.get(i).addReaction("🇬").submit();
			messageHistory.get(i).addReaction("🇪").submit();
			messageHistory.get(i).addReaction("🇹").submit();
			i = i + 1;
		}
	}
}
