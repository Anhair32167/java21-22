package telran.cars.controllers;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static telran.cars.api.RentCompanyErrorMessages.*;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RentCompanyValidationErrorsController {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e)
	{
		String message = e.getAllErrors().stream().map(er -> er.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return returnResponse(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HandlerMethodValidationException.class)
	ResponseEntity<String> handlerMethodValidationExceptionHandler(HandlerMethodValidationException e)
	{
		String message = e.getAllErrors().stream().map(er -> er.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return returnResponse(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	ResponseEntity<String> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e)
	{
		return returnResponse(TYPE_MISMATCH, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<String> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e)
	{
		return returnResponse(JSON_TYPE_MISMATCH, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<String> illegalArgumentException(IllegalArgumentException e)
	{
		return returnResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> returnResponse(String message, HttpStatus status)
	{
		log.error(message);
		return new ResponseEntity<String>(message, status);
	}

}
