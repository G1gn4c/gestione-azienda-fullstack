package com.gignac.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DefaultExceptionHandler {

	@ExceptionHandler(UtenteNonTrovatoException.class)
	public ResponseEntity<ApiError> handleException(
			UtenteNonTrovatoException utenteNonTrovatoException,
			HttpServletRequest httpServletRequest
	) {
		ApiError apiError = new ApiError();
		apiError.setPath(httpServletRequest.getRequestURI());
		apiError.setMessage(utenteNonTrovatoException.getMessage());
		apiError.setStatusCode(HttpStatus.NOT_FOUND.value());
		apiError.setLocalDateTime(LocalDateTime.now());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AziendaNonTrovataException.class)
	public ResponseEntity<ApiError> handleException(
			AziendaNonTrovataException aziendaNonTrovataException,
			HttpServletRequest httpServletRequest
	) {
		ApiError apiError = new ApiError();
		apiError.setPath(httpServletRequest.getRequestURI());
		apiError.setMessage(aziendaNonTrovataException.getMessage());
		apiError.setStatusCode(HttpStatus.NOT_FOUND.value());
		apiError.setLocalDateTime(LocalDateTime.now());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ImpiegatoNonTrovatoException.class)
	public ResponseEntity<ApiError> handleException(
			ImpiegatoNonTrovatoException impiegatoNonTrovatoException,
			HttpServletRequest httpServletRequest
	) {
		ApiError apiError = new ApiError();
		apiError.setPath(httpServletRequest.getRequestURI());
		apiError.setMessage(impiegatoNonTrovatoException.getMessage());
		apiError.setStatusCode(HttpStatus.NOT_FOUND.value());
		apiError.setLocalDateTime(LocalDateTime.now());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<ApiError> handleException(
			InsufficientAuthenticationException insufficientAuthenticationException,
			HttpServletRequest httpServletRequest
	) {
		ApiError apiError = new ApiError();
		apiError.setPath(httpServletRequest.getRequestURI());
		apiError.setMessage(insufficientAuthenticationException.getMessage());
		apiError.setStatusCode(HttpStatus.FORBIDDEN.value());
		apiError.setLocalDateTime(LocalDateTime.now());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> handleException(
			BadCredentialsException badCredentialsException,
			HttpServletRequest httpServletRequest
	) {
		ApiError apiError = new ApiError();
		apiError.setPath(httpServletRequest.getRequestURI());
		apiError.setMessage(badCredentialsException.getMessage());
		apiError.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		apiError.setLocalDateTime(LocalDateTime.now());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException(
			Exception exception, 
			HttpServletRequest httpServletRequest
	) {
		ApiError apiError = new ApiError();
		apiError.setPath(httpServletRequest.getRequestURI());
		apiError.setMessage(exception.getMessage());
		apiError.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		apiError.setLocalDateTime(LocalDateTime.now());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
