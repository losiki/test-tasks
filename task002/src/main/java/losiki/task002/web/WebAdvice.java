package losiki.task002.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackageClasses = UserService.class)
public class WebAdvice {
	@ExceptionHandler(exception = ServiceException.class, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> errorHandle(ServiceException e) {
		log.warn("caught service exception", e);
		try {
			ObjectMapper om = new ObjectMapper();
			return ResponseEntity.status(e.getStatus()).body(om.writeValueAsString(e.getMessage()));
		} catch (JsonProcessingException e1) {
			log.error("exception thrown", e1);
			return ResponseEntity.internalServerError().build();
		}
	}
}
