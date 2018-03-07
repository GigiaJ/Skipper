package eventInfo;

import handler.CommandHandler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageInfo extends ListenerAdapter {

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
	public void onMessageReceived(MessageReceivedEvent event) throws NullPointerException {
		try {
			author = event.getAuthor();
			botUser = event.getJDA().getSelfUser();
			guild = event.getGuild();
			userIcon = event.getAuthor().getAvatarUrl();
			authorName = event.getAuthor().getName();
			jda = event.getJDA();
			message = event.getMessage();
			channel = event.getChannel();
			channelType = event.getChannelType();
			//Begins to handle the event
			CommandHandler.handler();
		} catch (
		NullPointerException ignore) {	
			// ignore is a message that has been deleting and the library being used has no
			// method of acting upon the deleted message in order to handle this exception
			// Any attempt to handle this would constantly be invoked upon message send or
			// receive
		}
	}
}