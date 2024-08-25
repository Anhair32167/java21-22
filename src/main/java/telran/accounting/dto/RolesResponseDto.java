package telran.accounting.dto;


import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.accounting.entities.UserAccount;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RolesResponseDto {
	private String login;
	private Set<String> roles;
	
	public static RolesResponseDto build(UserAccount user) {
		return new RolesResponseDto(user.getLogin(), user.getRoles());
	}
}
