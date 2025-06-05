package losiki.task002.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import losiki.task002.data.SubscriptionRepository;
import losiki.task002.model.Subscription;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionService {
	@Autowired
	private SubscriptionRepository subRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
		if (subscription.getSubscriptionId() != null) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "subscription id mus tbe null");
		}
		Subscription saveSub = subRepo.save(subscription);
		MvcUriComponentsBuilder builder = MvcUriComponentsBuilder
			.relativeTo(ServletUriComponentsBuilder.fromCurrentContextPath());
		UriComponents uri = builder
			.withMethodCall(on(SubscriptionService.class).getSubscription(saveSub.getSubscriptionId())).build();
		return ResponseEntity.created(uri.encode().toUri()).body(saveSub);
	}

	@GetMapping(value = "/{subscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subscription> getSubscription(@PathVariable("subscriptionId") Integer subscriptionId) {
		return subRepo.findById(subscriptionId).map(s -> ResponseEntity.ok(s))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping(value = "/{subscriptionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subscription> replaseSubscription(@PathVariable("subscriptionId") Integer subscriptionId,
		@RequestBody Subscription subscription) {
		if(subscription.getSubscriptionId() != null && !subscription.getSubscriptionId().equals(subscriptionId)) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "subscription id must be null or equal to one, given in the path");
		}
		subscription.setSubscriptionId(subscriptionId);
		Subscription savedSub = subRepo.save(subscription);
		return ResponseEntity.ok(savedSub);
	}

	@DeleteMapping(value = "/{subscriptionId}")
	public void deleteSubscription(@PathVariable("subscriptionId") Integer subscriptionId) {
		subRepo.deleteById(subscriptionId);
	}

	@GetMapping(value="/top", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Subscription>> getTopNSubscriptions(@RequestParam(value="limit", required = false) Integer limit) {
		if (limit == null) {
			limit = 3;
		}

		if( limit <1 ) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "limit must be grater than or equal to 1");
		}

		return ResponseEntity.ok(subRepo.findTopN(limit));
	}
}
