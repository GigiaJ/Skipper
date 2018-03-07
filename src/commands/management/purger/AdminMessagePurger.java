package commands.management.purger;

import java.util.concurrent.ExecutionException;

import eventInfo.MessageInfo;
import net.dv8tion.jda.core.entities.Message;
import static commands.CommandList.cmdAdminPurgeAll;
import static commands.CommandList.cmdAdminPurgeUser;

public class AdminMessagePurger extends MessageInfo {

	public static Message purge() {
		try {
			String rawMessageContent = message.getContentRaw();
			
			if (rawMessageContent.startsWith(cmdAdminPurgeUser.getCommand())) {
				AdminPurgeUser.purgeUser();
			}

			if (rawMessageContent.startsWith(cmdAdminPurgeAll.getCommand())) {
				AdminPurgeAll.purgeAll();
			}
		} catch (NullPointerException | InterruptedException | ExecutionException ignore) {
		}
		return null;
	}
}