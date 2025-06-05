package losiki.task002.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import lombok.extern.slf4j.Slf4j;
import losiki.task002.data.UserRepository;
import losiki.task002.model.User;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserService {
	@Autowired
	private UserRepository repository;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		validate(user);
		if(user.getUserId() != null) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "user id given");
		}
		User saved = repository.save(user);
		MvcUriComponentsBuilder builder = MvcUriComponentsBuilder
			.relativeTo(ServletUriComponentsBuilder.fromCurrentContextPath());
		UriComponents uri = builder.withMethodCall(on(UserService.class).getUser(saved.getUserId())).build();
		return ResponseEntity.created(uri.encode().toUri()).body(saved);
	}

	private void validate(User user) {
		if (user == null) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "Missing body");
		}
		if(StringUtils.isEmpty(user.getName())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "Missing user name");
		}
		if (StringUtils.isEmpty(user.getEmail())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "Missing user email");
		}
		// TODO email validation
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<User>> getUsers() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable("userId") Integer userId) {
		Optional<User> user = repository.findById(userId);
		log.debug("id={}->user={}", userId, user);
		return user.map(u -> ResponseEntity.ok(u)).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping(value = "/{userId}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> replaceUser(@PathVariable("userId") Integer userId, @RequestBody User user) {
		user.setUserId(userId);
		validate(user);
		User savedUser = repository.save(user);
		return ResponseEntity.ok(savedUser);
	}

	@DeleteMapping(value = "/{userId}")
	public ResponseEntity<?> removeUser(@PathVariable("userId") Integer userId) {
		log.info("deleting {}", userId);
		repository.deleteById(userId);
		return ResponseEntity.noContent().build();
	}
}
