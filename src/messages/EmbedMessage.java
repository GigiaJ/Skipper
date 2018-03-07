package messages;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class EmbedMessage {
	public static Message embMsg(String title, String embContent, String imageUrl, String embName, String embIconUrl,
			Color embColor) throws IllegalStateException {
		try {
			EmbedBuilder emb = new EmbedBuilder();
			MessageEmbed embBuild = emb.setTitle(title).setDescription(embContent).setColor(embColor).setImage(imageUrl)
					.setAuthor(embName, null, embIconUrl).build();
			Message embedMessage = new MessageBuilder().setContent("ㅤ").setEmbed(embBuild).build();
			return embedMessage;
		} catch (IllegalStateException ignore) {
			// JDA issue, can't resolve
		}
		return null;
	}

}