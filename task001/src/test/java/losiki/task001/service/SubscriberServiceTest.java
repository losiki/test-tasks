package losiki.task001.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import losiki.task001.TestUtils;
import losiki.task001.conf.DaoConfiguration;
import losiki.task001.conf.WebAdvice;
import losiki.task001.data.SubscriberRepository;
import losiki.task001.model.Subscriber;
import losiki.task001.service.SubscriberService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { SubscriberService.class, WebAdvice.class, DaoConfiguration.class })
public class SubscriberServiceTest {
	@Autowired
	private TestRestTemplate		restTemplate;
	@Autowired
	private SubscriberRepository	subscriberRepository;

	@Test
	public void testCreateSubscriber() {
		Subscriber subscriber = createSubscriber();

		ResponseEntity<Void> response = restTemplate.exchange(RequestEntity.put("/subscriber").body(subscriber),
			Void.class);
		URI location = response.getHeaders().getLocation();
		assertNotNull(location);

		// retrieve a subscriber using returned location
		Subscriber subscriber2 = restTemplate.getForObject(location, Subscriber.class);
		assertThat(subscriber2, samePropertyValuesAs(subscriber, "subscriberId"));

		// retrieve and check a subscriber directly from repo
		Optional<Subscriber> subscriber3 = subscriberRepository.findById(subscriber2.getSubscriberId());
		assertTrue(subscriber3.isPresent());
		assertThat(subscriber3.get(), samePropertyValuesAs(subscriber, "subscriberId"));

		// number of subscribers before invalid tests
		long count = subscriberRepository.count();

		subscriber = createSubscriber();
		subscriber.setEmail(null);
		postAndCheckBadRequest(subscriber, count);
		subscriber.setEmail("");
		postAndCheckBadRequest(subscriber, count);

		subscriber = createSubscriber();
		subscriber.setPosition(null);
		postAndCheckBadRequest(subscriber, count);
		subscriber.setPosition("");
		postAndCheckBadRequest(subscriber, count);

		subscriber = createSubscriber();
		subscriber.setFullname(null);
		postAndCheckBadRequest(subscriber, count);
		subscriber.setFullname("");
		postAndCheckBadRequest(subscriber, count);

		subscriber = createSubscriber();
		subscriber.setCity(null);
		postAndCheckBadRequest(subscriber, count);
		subscriber.setCity("");
		postAndCheckBadRequest(subscriber, count);
	}

	private void postAndCheckBadRequest(Subscriber subscriber, long count) {
		ResponseEntity<Void> response = restTemplate.exchange(RequestEntity.put("/subscriber").body(subscriber),
			Void.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(count, subscriberRepository.count(), "number of subscribers changed");
	}

	private Subscriber createSubscriber() {
		Subscriber subscriber = new Subscriber();
		subscriber.setEmail(TestUtils.randomName() + "@example.org");
		subscriber.setFullname(TestUtils.randomName());
		subscriber.setCity(TestUtils.randomCity());
		subscriber.setPosition(TestUtils.randomName());
		return subscriber;
	}
}
