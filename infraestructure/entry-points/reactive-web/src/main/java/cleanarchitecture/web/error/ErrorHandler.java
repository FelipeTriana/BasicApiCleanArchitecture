package cleanarchitecture.web.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import cleanarchitecture.domain.common.ex.BusinessException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorHandler {

    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(Exception exception) {
        LOGGER.error("Error: ", exception);
        return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);        
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<String> handleConstraintViolationExceptions(ConstraintViolationException exception) {
        LOGGER.error("ConstraintViolationException: ", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<String> handleBusinessExceptions(BusinessException exception) {
        LOGGER.error("BusinessException: ", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private String getMessagesErrorAnotations(WebExchangeBindException webExchangeException) {
    	if(webExchangeException.getFieldErrors() != null) {
    		StringBuilder errorDesc = new StringBuilder();
    		for (FieldError error : webExchangeException.getFieldErrors()) {
    			errorDesc.append(error.getField()).append(":").append(error.getDefaultMessage()).append(". ");
			}
    		return errorDesc.toString();
    	}
		return null;
    }

    @ExceptionHandler(ServerWebInputException.class)
    public final ResponseEntity<String> handleAllExceptions(ServerWebInputException exception) {
        LOGGER.error("Error: ", exception);
        if(exception instanceof WebExchangeBindException) {
       	 WebExchangeBindException  webExchangeException =  (WebExchangeBindException) exception;
       	  if(webExchangeException.getStatusCode() == HttpStatus.BAD_REQUEST) {
       		   String messageError = getMessagesErrorAnotations(webExchangeException);
       		   if(messageError == null) {
       			   	return new ResponseEntity<>(exception.getReason(), HttpStatus.BAD_REQUEST);
       		   }else {
            		return new ResponseEntity<>(messageError, HttpStatus.BAD_REQUEST);
       		   }
       	  }     	  
       }
        return new ResponseEntity<>(exception.getReason(), HttpStatus.BAD_REQUEST);
    }
}
