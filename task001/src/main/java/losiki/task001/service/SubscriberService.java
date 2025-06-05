package losiki.task001.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import losiki.task001.data.SubscriberRepository;
import losiki.task001.model.Subscriber;

@RestController
@RequestMapping("/subscriber")
public class SubscriberService {
	@Autowired
	private SubscriberRepository subscriberRepository;

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createSubscriber(@RequestBody Subscriber subscriber, UriComponentsBuilder location) {
		if (subscriber == null) {
			return ResponseEntity.badRequest().build();
		}
		subscriber.setSubscriberId(null);
		validate(subscriber);
		Subscriber saved = subscriberRepository.save(subscriber);

		return ResponseEntity
			.created(location.pathSegment("subscriber", String.valueOf(saved.getSubscriberId())).build().toUri())
			.build();
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subscriber> getSubscriber(@PathVariable Integer id) {
		Optional<Subscriber> subscriber = subscriberRepository.findById(id);
		if (subscriber.isPresent()) {
			return ResponseEntity.ok(subscriber.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subscriber> updateSubscriber(@PathVariable Integer id, String value) {
		Optional<Subscriber> subscriber = subscriberRepository.findById(id);
		if (!subscriber.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		ObjectMapper om = new ObjectMapper();
		Subscriber updated;
		try {
			updated = om.readerForUpdating(subscriber.get()).readValue(value);
		} catch (JsonProcessingException e) {
			throw new ServiceException(HttpStatus.BAD_REQUEST);
		}
		if (!id.equals(updated.getSubscriberId())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "attempt to modify subscriber id");
		}
		validate(updated);
		subscriberRepository.save(updated);
		return ResponseEntity.ok(subscriber.get());
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteSubscriber(@PathVariable Integer id) {
		subscriberRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

	private void validate(Subscriber subscriber) {
		if (StringUtils.isEmpty(subscriber.getEmail())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "null or empty email");
		}
		if (StringUtils.isEmpty(subscriber.getFullname())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "null or empty full name");
		}
		if (StringUtils.isEmpty(subscriber.getPosition())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "null or empty position");
		}
		if (StringUtils.isEmpty(subscriber.getCity())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "null or empty city");
		}
	}
}
