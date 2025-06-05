package losiki.task001.service;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/notify")
public class NotifyService {
	@Autowired
	private ProducerTemplate template;

	/**
	 * Trigger notifications immediately.
	 */
	@PostMapping
	public void notifySubscribers() {
		template.asyncSendBody("direct:process", "");
	}
}
