package at.srfg.iasset.repository.api.exception;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import at.srfg.iasset.repository.api.model.Message;
import at.srfg.iasset.repository.api.model.MessageType;
import at.srfg.iasset.repository.api.model.Result;

@ControllerAdvice
public class RepositoryExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value= {RepositoryNotFoundException.class})
	protected ResponseEntity<Object> handleConflict(RepositoryException ex, WebRequest request) {
		Result result = new Result();
		Message message = new Message()
				.messageType(MessageType.EXCEPTION)
				.code(String.format("%s", ex.getStatus().value()))
				.timestamp(Instant.now().toString())
				.text(ex.getLocalizedMessage());
			
		result.addMessagesItem(message).setSuccess(false);
		return new ResponseEntity<Object>(result,ex.getStatus() );
	}

}
