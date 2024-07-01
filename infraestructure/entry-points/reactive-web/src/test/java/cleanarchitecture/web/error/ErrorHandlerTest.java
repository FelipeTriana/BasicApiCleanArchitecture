package cleanarchitecture.web.error;

import cleanarchitecture.domain.common.ex.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.ConstraintViolationException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    @BeforeEach
    public void setup() {
        errorHandler = new ErrorHandler();
    }

    @Test
    public void testHandleAllExceptions() {
        Exception exception = new Exception("Test exception");
        ResponseEntity<String> response = errorHandler.handleAllExceptions(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody());
    }

    @Test
    public void testHandleConstraintViolationExceptions() {
        ConstraintViolationException exception = new ConstraintViolationException("Test violation", null);
        ResponseEntity<String> response = errorHandler.handleConstraintViolationExceptions(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Test violation", response.getBody());
    }

    @Test
    public void testHandleBusinessExceptions() {
        BusinessException   exception = (BusinessException) BusinessException.Type.USER_NOT_EXIST.defer().get();
        ResponseEntity<String> response = errorHandler.handleBusinessExceptions(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(exception.getMessage(), response.getBody());
    }

    @Test
    public void testHandleAllExceptionsServerWebInputException() {
        ServerWebInputException exception = new ServerWebInputException("Test server web input exception");
        ResponseEntity<String> response = errorHandler.handleAllExceptions(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(exception.getReason(), response.getBody());
    }

    @Test
    public void testHandleAllExceptionsWebExchangeBindException() {
        WebExchangeBindException exception = Mockito.mock(WebExchangeBindException.class);
        Mockito.when(exception.getReason()).thenReturn("Test web exchange bind exception");
        Mockito.when(exception.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        ResponseEntity<String> response = errorHandler.handleAllExceptions(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}