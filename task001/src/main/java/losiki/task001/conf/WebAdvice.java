package losiki.task001.conf;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import lombok.extern.slf4j.Slf4j;
import losiki.task001.service.ServiceException;
import losiki.task001.service.SubscriberService;

@Slf4j
@EnableWebMvc
@ControllerAdvice(basePackageClasses = SubscriberService.class)
public class WebAdvice{
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> errorHandle(ServiceException e) {
    	log.warn("service exception thrown", e);
    	return ResponseEntity.status(e.getStatus()).body(e.getMessage());
   }
}
