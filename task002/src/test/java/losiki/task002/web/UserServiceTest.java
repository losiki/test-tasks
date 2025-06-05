package losiki.task002.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import losiki.task002.WebRiseApp;
import losiki.task002.data.UserRepository;
import losiki.task002.model.User;
import losiki.task002.test.AbstractPsqlTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = WebRiseApp.class)
public class UserServiceTest extends AbstractPsqlTest {
	@Autowired
	private TestRestTemplate	restTemplate;
	@Autowired
	private UserRepository		userRepository;

	@Test
	public void testCreateUser() {
		User user = createRandomUser();

		// check that stored user is returned
		ResponseEntity<User> response = restTemplate.postForEntity(USERS_URI, user, User.class);
		assertNotNull(response, "response is null");
		User responseUser = response.getBody();
		assertThat(responseUser, samePropertyValuesAs(user, "userId"));
		URI location = response.getHeaders().getLocation();
		assertNotNull(location, "no location for just created object");

		// check that user is stored
		Optional<User> daoUser = userRepository.findById(responseUser.getUserId());
		assertTrue(daoUser.isPresent());
		assertEquals(responseUser, daoUser.get());

		// check that the service returns just create user
		response = restTemplate.getForEntity(location, User.class);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value(), "bad status code");
		assertEquals(responseUser, response.getBody(), "invalid user entity returned");

		// check that an user list contains the user
		User[] users = restTemplate.getForObject(USERS_URI, User[].class);
		assertThat(users, hasItemInArray(responseUser));
	}

	@Test
	public void testUniqName() {
		User user = createRandomUser();

		User[] users = restTemplate.getForObject(USERS_URI, User[].class);
		ResponseEntity<User> response1 = restTemplate.postForEntity(USERS_URI, user, User.class);
		assertTrue(response1.getStatusCode().is2xxSuccessful(), "success expected");
		ResponseEntity<User> response2 = restTemplate.postForEntity(USERS_URI, user, User.class);
		assertFalse(response2.getStatusCode().is2xxSuccessful(), "failure expected");
		assertEquals(users.length + 1, restTemplate.getForObject(USERS_URI, User[].class).length,
			"wrong number of user entries");
		assertEquals(response1.getBody(), restTemplate.getForObject(response1.getHeaders().getLocation(), User.class),
			"invalid user entry returned");
	}

	@Test
	public void testInvalidUser() {
		User[] users = restTemplate.getForObject(USERS_URI, User[].class);

		ResponseEntity<?> response = restTemplate
			.exchange(RequestEntity.post(USERS_URI).contentType(MediaType.APPLICATION_JSON).body(null), Object.class);
		assertEquals(users.length, restTemplate.getForObject(USERS_URI, User[].class).length,
			"wrong number of user entries");
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());

		User user = createRandomUser();
		user.setEmail("");
		response = restTemplate
			.exchange(RequestEntity.post(USERS_URI).contentType(MediaType.APPLICATION_JSON).body(user), Object.class);
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
		assertEquals(users.length, restTemplate.getForObject(USERS_URI, User[].class).length,
			"wrong number of user entries");

		user = createRandomUser();
		user.setName("");
		response = restTemplate
			.exchange(RequestEntity.post(USERS_URI).contentType(MediaType.APPLICATION_JSON).body(user), Object.class);
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
		assertEquals(users.length, restTemplate.getForObject(USERS_URI, User[].class).length,
			"wrong number of user entries");
	}

	@Test
	public void testDeleteUser() {
		User[] users = restTemplate.getForObject(USERS_URI, User[].class);

		User user = restTemplate.postForObject(USERS_URI, createRandomUser(), User.class);
		assertEquals(users.length+1, restTemplate.getForObject(USERS_URI, User[].class).length,
			"wrong number of user entries");

		restTemplate.delete(USER_URI, user.getUserId());
		assertEquals(users.length, restTemplate.getForObject(USERS_URI, User[].class).length,
			"wrong number of user entries");
	}

	@Test
	public void testReplaceUser() {
		User[] users = restTemplate.getForObject(USERS_URI, User[].class);

		User user = restTemplate.postForObject(USERS_URI, createRandomUser(), User.class);
		assertEquals(users.length+1, restTemplate.getForObject(USERS_URI, User[].class).length,
			"wrong number of user entries");

		user.setEmail(user.getEmail()+".org");
		restTemplate.put(USER_URI, user, user.getUserId());
		assertEquals(users.length+1, restTemplate.getForObject(USERS_URI, User[].class).length,
			"wrong number of user entries");

		User user1 = restTemplate.getForObject(USER_URI, User.class, user.getUserId());
		assertEquals(user, user1);
	}

	@Test
	public void testNotExistingUser() {
		ResponseEntity<?> response = restTemplate.getForEntity(USER_URI, Object.class, -4);
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
	}
}
