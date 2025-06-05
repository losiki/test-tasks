package losiki.task002.test;

import java.util.UUID;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import losiki.task002.model.User;

@Testcontainers
public class AbstractPsqlTest {
	protected static final String USER_URI = "/users/{userId}";
	protected static final String USERS_URI = "/users";
	protected static final String URI_USER_SUBSCRIPTIONS = USER_URI+"/subscriptions";
	@Container
	public static PostgreSQLContainer<PostgresqlContainer> postgreSQLContainer = PostgresqlContainer.getInstance();

	protected User createRandomUser() {
		User user = new User();
		user.setName(UUID.randomUUID().toString());
		user.setEmail(user.getName()+"@example.org");
		return user;
	}

}
