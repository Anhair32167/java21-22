package telran.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.accounting.entities.UserAccount;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserAccountResponseDto {	
	private String login;
	private String firsName;
	private String lastName;
	private Set<String> roles;
	
	public static UserAccountResponseDto build(UserAccount user) {
		return new UserAccountResponseDto(user.getLogin(), user.getFirsName(), user.getLastName(), user.getRoles());
	}
}
