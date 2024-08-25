package telran.accounting.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "user_account")
public class UserAccount {
	@Id
	@Setter(value =  AccessLevel.NONE)
	private String login;
	private String hashCode;
	private String firsName;
	private String lastName;
	private HashSet<String> roles;
	private LocalDateTime activationDate;
	private boolean revoked;
	private LinkedList<String> lastHashCodes;
	
	public UserAccount() {
		this.activationDate = LocalDateTime.now();
		roles = new HashSet<>();
		roles.add("USER");
	}
	
	public UserAccount(String login, String hashCode, String firsName, String lastName) {
		super();
		this.login = login;
		this.hashCode = hashCode;
		this.firsName = firsName;
		this.lastName = lastName;
		this.activationDate = LocalDateTime.now();
		roles = new HashSet<>();
		roles.add("USER");
	}
	
	
}
