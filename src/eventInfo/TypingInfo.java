package eventInfo;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.user.UserTypingEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class TypingInfo extends ListenerAdapter {

	public static MessageChannel channel = null;
	public static Message message = null;
	public static User author = null;
	public static User botUser = null;
	public static Guild guild = null;
	public static String userIcon = "";
	public static String authorName = "";
	public static JDA jda = null;
	public static ChannelType channelType = null;

	@Override
	public void onUserTyping(UserTypingEvent event) throws NullPointerException {
		try {
			author = event.getUser();
			botUser = event.getJDA().getSelfUser();
			if (author.equals(botUser)) {
				guild = event.getGuild();
				userIcon = event.getUser().getAvatarUrl();
				authorName = event.getUser().getName();
				jda = event.getJDA();
				channel = event.getChannel();
			}
		} catch (
		NullPointerException ignore) {	
			// ignore is a message that has been deleted and the library being used has no
			// method of acting upon the deleted message in order to handle this exception
			// Any attempt to handle this would constantly be invoked upon message send or
			// receive
		}
	}
}