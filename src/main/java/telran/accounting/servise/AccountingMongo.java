package telran.accounting.servise;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import telran.accounting.dto.RolesResponseDto;
import telran.accounting.dto.UserAccountResponseDto;
import telran.accounting.dto.UserRegisterDto;
import telran.accounting.dto.UserUpdateDto;
import telran.accounting.entities.UserAccount;
import telran.accounting.exceptions.AccountActivationException;
import telran.accounting.exceptions.AccountRevokeException;
import telran.accounting.exceptions.PasswordNotValidException;
import telran.accounting.exceptions.RoleExistsException;
import telran.accounting.exceptions.RoleNotExistsException;
import telran.accounting.exceptions.UserExistsException;
import telran.accounting.exceptions.UserNotFoundException;
import telran.accounting.repo.UserAccountRepository;

@Service
public class AccountingMongo implements IAccountingManagment, CommandLineRunner {

	@Autowired
	UserAccountRepository repo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Value("${password_length:6}")
	private int passwordLength;

	@Value("${n_last_hash:3}")
	private int n_last_hash;

	private UserAccount getUserAccount(String login) {
		return repo.findById(login).orElseThrow(() -> new UserNotFoundException(login));
	}

	@Override
	public UserAccountResponseDto registration(UserRegisterDto account) {
		if (repo.existsById(account.getLogin()))
			throw new UserExistsException(account.getLogin());
		if (!isPasswordValid(account.getPassword()))
			throw new PasswordNotValidException(account.getPassword());
		UserAccount acc = new UserAccount(account.getLogin(), getHash(account.getPassword()), account.getFirsName(),
				account.getLastName());
		repo.save(acc);
		return UserAccountResponseDto.build(acc);
	}

	private String getHash(String password) {
		return encoder.encode(password);
	}

	private boolean isPasswordValid(String password) {
		return password.length() >= passwordLength;
	}

	@Override
	public UserAccountResponseDto getUser(String login) {
		UserAccount user = getUserAccount(login);
		return UserAccountResponseDto.build(user);
	}

	@Override
	public UserAccountResponseDto editUser(String login, UserUpdateDto acc) {
		UserAccount user = getUserAccount(login);
		if (acc.getFirsName() == null || acc.getLastName() == null)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "First name and last name can't be null");
		user.setFirsName(acc.getFirsName());
		user.setLastName(acc.getLastName());
		repo.save(user);
		return UserAccountResponseDto.build(user);

	}

	@Override
	public UserAccountResponseDto removeUser(String login) {
		UserAccount user = getUserAccount(login);
		repo.delete(user);
		return UserAccountResponseDto.build(user);
	}

	@Override
	public boolean updatePassword(String login, String newPassword) {
		if (!isPasswordValid(newPassword) || newPassword == null)
			throw new PasswordNotValidException(newPassword);
		UserAccount user = getUserAccount(login);
		if (encoder.matches(newPassword, user.getHashCode()))
			throw new PasswordNotValidException(newPassword);
		LinkedList<String> lastHash = user.getLastHashCodes();
		if (isPasswordFromLast(newPassword, lastHash))
			throw new PasswordNotValidException(newPassword);

		if (lastHash.size() == n_last_hash)
			lastHash.removeFirst();
		lastHash.add(user.getHashCode());
		user.setHashCode(encoder.encode(newPassword));
		user.setActivationDate(LocalDateTime.now());
		repo.save(user);
		return true;
	}

	private boolean isPasswordFromLast(String newPassword, LinkedList<String> lastHash) {
		return lastHash.stream().anyMatch(p -> encoder.matches(newPassword, p));
	}

	@Override
	public boolean revokeAccount(String login) {
		UserAccount user = getUserAccount(login);
		if(user.isRevoked())
			throw new AccountRevokeException(login);
		user.setRevoked(true);
		repo.save(user);
		return true;
	}

	@Override
	public boolean activateAccount(String login) {
		UserAccount user = getUserAccount(login);
		if(!user.isRevoked())
			throw new AccountActivationException(login);
		user.setRevoked(false);
		user.setActivationDate(LocalDateTime.now());
		repo.save(user);
		return true;
	}

	@Override
	public String getPasswordHash(String login) {
		UserAccount user = getUserAccount(login);
		return user.getHashCode();
	}

	@Override
	public LocalDateTime getActivationDate(String login) {
		UserAccount user = getUserAccount(login);
		return user.getActivationDate();
	}

	@Override
	public RolesResponseDto getRoles(String login) {
		UserAccount user = getUserAccount(login);
		return RolesResponseDto.build(user);
	}

	@Override
	public RolesResponseDto addRoles(String login, String role) {
		UserAccount user = getUserAccount(login);
		boolean res = user.getRoles().add(role);
		if (!res)
			throw new RoleExistsException(role);
		repo.save(user);
		return RolesResponseDto.build(user);
	}

	@Override
	public RolesResponseDto removeRoles(String login, String role) {
		UserAccount user = getUserAccount(login);
		boolean res = user.getRoles().remove(role);
		if (!res)
			throw new RoleNotExistsException(role);
		repo.save(user);
		return RolesResponseDto.build(user);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!repo.existsById("admin")) {
			UserAccount admin = new UserAccount("admin", encoder.encode("administrator"), "", "");
			admin.setRoles(new HashSet<String>(List.of("ADMIN")));
			repo.save(admin);
		}
	}

}
