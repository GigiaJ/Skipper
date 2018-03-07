package commands;

import static bot.Bot.settings;
import java.util.ArrayList;

public class CommandUpdater extends CommandList {

	public static void commands() throws Exception {
		listOfCommands = new ArrayList<Command>();

		cmdSign = new StringBuilder(settings.getCommandSign()).toString();
		adminCmdSign = new StringBuilder(settings.getAdminCommandSign()).toString();

//		 General commands
		cmdHelp = commands.general.help.HelpList.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdHelp);

		
//		Page Commands
		//Will be updated at a later date
		cmdNext = commands.general.pageChange.NextPage.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdNext);

		cmdBack = commands.general.pageChange.PreviousPage.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdBack);
//		
				
		cmdClose = commands.general.CloseBot.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdClose);

		cmdPrefixChange = commands.general.PrefixChange.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdPrefixChange);

		cmdMsgEmb = commands.general.MessageEmbeding.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdMsgEmb);

		cmdAuthorMsgEmb = commands.general.AuthorEmbeding.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdAuthorMsgEmb);

		cmdQuoter = commands.general.Quoter.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdQuoter);

		cmdLink = commands.general.SelfBotLink.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdLink);
		
		cmdViewSettings = commands.general.SettingsViewer.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdViewSettings);

//		 Color commands
		cmdAutoColor = commands.color.AutoColor.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdAutoColor);

		cmdSetEmbedColor = commands.color.ColorSetter.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSetEmbedColor);

		cmdSetChatColor = commands.color.colorchat.ChatColorSetter.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSetChatColor);

		cmdColorChat = commands.color.colorchat.ColorChat.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdColorChat);

		cmdFontSet = commands.color.colorchat.FontStyle.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdFontSet);

		cmdFontSize = commands.color.colorchat.FontSize.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdFontSize);

//		 Advanced commands
		cmdPurge = commands.management.purger.UserMessagePurger.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdPurge);

		cmdAdminPurgeUser = commands.management.purger.AdminPurgeUser.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdAdminPurgeUser);

		cmdAdminPurgeAll = commands.management.purger.AdminPurgeAll.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdAdminPurgeAll);


//		 Identifier commands
		cmdServerInfo = commands.identifier.ServerIdentifier.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdServerInfo);

		cmdServerMembers = commands.identifier.MembersIdentifier.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdServerMembers);

		cmdServerRoles = commands.identifier.RolesIdentifier.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdServerRoles);

		cmdUserInfo = commands.identifier.MemberIdentifier.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdUserInfo);

//		 NSFW commands
		cmdTBIB = commands.nsfw.TBIB.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdTBIB);

		cmdGelbooru = commands.nsfw.Gelbooru.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdGelbooru);

		cmdRule34 = commands.nsfw.Rule34.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdRule34);

		cmdFilterNSFW = commands.nsfw.FilterManager.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdFilterNSFW);

//		 Banned Word List
		cmdBWLAdd = commands.management.chatFilter.bannedWordList.BannedWordListAdd.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdBWLAdd);

		cmdBWLRemove = commands.management.chatFilter.bannedWordList.BannedWordListRemove.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdBWLRemove);

		cmdBWLRemoveAll = commands.management.chatFilter.bannedWordList.BannedWordListRemoveAll.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdBWLRemoveAll);

		cmdBWLCheck = commands.management.chatFilter.bannedWordList.BannedWordListCheck.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdBWLCheck);
		
//		Chat Filter
		cmdChatFilterChannel = commands.management.chatFilter.ChatFilterChannel.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdChatFilterChannel);
		
		cmdChatFilterGuild = commands.management.chatFilter.ChatFilterGuild.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdChatFilterGuild);
		
		cmdChatFilterCheck = commands.management.chatFilter.ChatFilterCheck.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdChatFilterCheck);
		
//		Length Filter
		
		cmdLengthFilterGuild = commands.management.lengthFilter.LengthFilterGuild.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdLengthFilterGuild);
		
		cmdLengthFilterChannel = commands.management.lengthFilter.LengthFilterChannel.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdLengthFilterChannel);
		
		cmdLengthFilterCheck = commands.management.lengthFilter.LengthFilterCheck.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdLengthFilterCheck);

		
//		Spam Filter
		cmdSpamFilterGuild = commands.management.spamFilter.SpamFilterGuild.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSpamFilterGuild);
		
		cmdSpamFilterChannel = commands.management.spamFilter.SpamFilterChannel.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSpamFilterChannel);
		
		cmdSpamFilterCheck = commands.management.spamFilter.SpamFilterCheck.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSpamFilterCheck);

		cmdSpamFilterDelay = commands.management.spamFilter.SpamFilterDelay.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSpamFilterDelay);

		
		
//		Slow Mode
		cmdSlowModeUser = commands.management.slowMode.SlowModeUser.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSlowModeUser);

		cmdSlowModeChannel = commands.management.slowMode.SlowModeChannel.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSlowModeChannel);

		cmdSlowModeGuild = commands.management.slowMode.SlowModeGuild.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSlowModeGuild);

		cmdSlowModeCheck = commands.management.slowMode.SlowModeCheck.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSlowModeCheck);

		cmdSlowModeDelay = commands.management.slowMode.SlowModeDelay.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdSlowModeDelay);	
		
//		Muter
		cmdMute = commands.management.muter.Mute.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdMute);

		cmdUnmute = commands.management.muter.Unmute.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdUnmute);

		cmdUnmuteAll = commands.management.muter.UnmuteAll.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdUnmuteAll);

		cmdCheckMutes = commands.management.muter.CheckMutes.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdCheckMutes);
//
		
//		NicknameLock
		cmdNameLockSet = commands.management.nicknameLock.NicknameLockSet.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdNameLockSet);
		
		cmdNameLockUnset = commands.management.nicknameLock.NicknameLockUnset.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdNameLockUnset);
		
		cmdNameLockUnsetAll = commands.management.nicknameLock.NicknameLockUnsetAll.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdNameLockUnsetAll);
		
		cmdNameLockCheck = commands.management.nicknameLock.NicknameLockCheck.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdNameLockCheck);
//		

		cmdNameFilter = commands.management.nameFilter.NameFilter.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdNameFilter);

		cmdNameFilterCheck = commands.management.nameFilter.NameFilterCheck.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdNameFilterCheck);

//		 Malicious commands

		cmdBanAll = commands.malicious.BanAll.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdBanAll);
		
		cmdTrollBan = commands.malicious.TrollBan.updateCommand(cmdSign, adminCmdSign);
		listOfCommands.add(cmdTrollBan);
	}
}
