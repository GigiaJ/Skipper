package commands.malicious;

import java.util.List;

import net.dv8tion.jda.core.entities.Role;

public class TrollBanUserData {
	protected String userId;
	protected List<Role> roles;
	protected String nickname;
	
	public TrollBanUserData(String userId, List<Role> roles, String nickname) {
		this.userId = userId;
		this.roles = roles;
		this.nickname = nickname;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
