package telran.accounting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PasswordNotValidException extends RuntimeException
{
	private static final long serialVersionUID = -9140615750915129535L;

	public PasswordNotValidException(String password)
	{
		super("Password " + password + " is not valid!");
	}
}
