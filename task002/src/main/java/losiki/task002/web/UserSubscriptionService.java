package losiki.task002.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import losiki.task002.data.SubscriptionRepository;
import losiki.task002.data.UserRepository;
import losiki.task002.model.Subscription;
import losiki.task002.model.User;

@RestController
@RequestMapping("/users/{userId}/subscriptions")
@Slf4j
public class UserSubscriptionService {
	@Autowired
	private UserRepository			userRepo;
	@Autowired
	private SubscriptionRepository	subRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> subscribeUser(@PathVariable("userId") Integer userId,
		@RequestBody Subscription subscription) {
		Optional<User> user = userRepo.findById(userId);
		if (user.isEmpty()) {
			throw new ServiceException(HttpStatus.NOT_FOUND, "user not found");
		}
		Subscription sub = subRepo.findByProviderAndType(subscription.getProvider(), subscription.getType());
		if (sub == null) {
			subscription.setSubscriptionId(null);
			sub = subRepo.save(subscription);
		}
		try {
			subRepo.subscribe(user.get().getUserId(), sub.getSubscriptionId());
		} catch (DuplicateKeyException e) {
			log.debug("ignoring duplicate key user={}, sub={}", user.get(), sub, e);
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Subscription>> getUserSubscriptions(@PathVariable("userId") Integer userId) {
		Optional<User> user = userRepo.findById(userId);
		if (user.isEmpty()) {
			throw new ServiceException(HttpStatus.NOT_FOUND, "user not found");
		}

		return ResponseEntity.ok(subRepo.findAllByUserId(userId));
	}

	@DeleteMapping(value="/{subId}")
	public ResponseEntity<Void> unsubscribeUser(@PathVariable("userId") Integer userId, @PathVariable("subId") Integer subscriptionId) {
		log.info("deleting subscription user_id={}, subscriptionId={}", userId, subscriptionId);
		subRepo.unsubscribe(userId, subscriptionId);
		return ResponseEntity.ok().build();
	}

}
