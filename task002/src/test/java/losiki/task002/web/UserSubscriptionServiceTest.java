package losiki.task002.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import losiki.task002.WebRiseApp;
import losiki.task002.model.Subscription;
import losiki.task002.model.User;
import losiki.task002.test.AbstractPsqlTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = WebRiseApp.class)
public class UserSubscriptionServiceTest extends AbstractPsqlTest {
	@Autowired
	private TestRestTemplate	restTemplate;

	@Test
	public void testTop() {
		// create users
		User[] users = new User[5];
		for (int i = 0; i < 5; i++) {
			users[i] = restTemplate.postForObject(USERS_URI, createRandomUser(), User.class);
		}

		// create subscription and subscribe users
		// the first subscription is the least subscribed, the last is the most subscribed
		Subscription[] subs = new Subscription[5];
		for (int i = 0; i < 5; i++) {
			subs[i] = new Subscription();
			subs[i].setProvider(UUID.randomUUID().toString());
			subs[i].setType(String.valueOf(i));
			for (int j = 0; j <= i; j++) {
				ResponseEntity<?> rsp = restTemplate.postForEntity(URI_USER_SUBSCRIPTIONS, subs[i], Object.class,
					users[j].getUserId());
				assertTrue(rsp.getStatusCode().is2xxSuccessful());
			}
		}

		// check order
		Subscription[] topSub = restTemplate.getForObject("/subscriptions/top", Subscription[].class);
		assertEquals(3, topSub.length);
		for (int i = 0; i < 3; i++) {
			Assertions.assertThat(topSub[i]).usingRecursiveComparison().ignoringFields("subscriptionId")
				.isEqualTo(subs[4-i]);
		}
		topSub = restTemplate.getForObject("/subscriptions/top?limit=2", Subscription[].class);
		assertEquals(2, topSub.length);
		for (int i = 0; i < 2; i++) {
			Assertions.assertThat(topSub[i]).usingRecursiveComparison().ignoringFields("subscriptionId")
				.isEqualTo(subs[4-i]);
		}

		// get the first user subscriptions and unsubscribe
		Subscription[] user4subs = restTemplate.getForObject(URI_USER_SUBSCRIPTIONS, Subscription[].class, users[0].getUserId());
		assertEquals(5, user4subs.length);

		for (int j = 0; j < 5; j++) {
			restTemplate.delete(URI_USER_SUBSCRIPTIONS+"/{subId}", users[0].getUserId(), user4subs[j].getSubscriptionId());
		}

		user4subs = restTemplate.getForObject(URI_USER_SUBSCRIPTIONS, Subscription[].class, users[0].getUserId());
		assertEquals(0, user4subs.length);
	}
}
