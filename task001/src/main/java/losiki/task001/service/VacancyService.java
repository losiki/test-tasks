package losiki.task001.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import losiki.task001.data.DigitalFutureDao;
import losiki.task001.data.VacancyRepository;
import losiki.task001.model.Vacancy;

@RestController
@RequestMapping("/vacancy")
public class VacancyService {
	private static final Logger LOG = LoggerFactory.getLogger(VacancyService.class);
	@Autowired
	private VacancyRepository vacancyRepository;
	@Autowired
	private DigitalFutureDao dataTools;

	@PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> createVacancy(@RequestBody(required = true) Vacancy vacancy) {
		LOG.info("got {}", vacancy);
		validate(vacancy);
		Vacancy saved = vacancyRepository.save(vacancy);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	private void validate(Vacancy vacancy) {
		if (vacancy.getSallary() <= 0) {
			LOG.error("Vacancy has non-positive sallary={}", vacancy.getSallary());
			throw new ServiceException(HttpStatus.BAD_REQUEST, "invalid sallary");
		}

		if (StringUtils.isEmpty(vacancy.getName())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "null or empty name");
		}

		if (StringUtils.isEmpty(vacancy.getPosition())) {
			LOG.error("Vacancy has null or empty position");
			throw new ServiceException(HttpStatus.BAD_REQUEST, "null or empty position");
		}
		if (StringUtils.isEmpty(vacancy.getCity())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST, "null or empty city");
		}
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Vacancy>> getVacancies(@RequestParam(required=false) String name,
			@RequestParam(required=false) String position,
			@RequestParam(required=false) String city) {
		return ResponseEntity.ok(dataTools.getVacancies(name, position, city));
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Vacancy> getVacancy(@PathVariable Integer id) {
		Optional<Vacancy> vacancy = vacancyRepository.findById(id);
		if (vacancy.isPresent()) {
			return ResponseEntity.ok(vacancy.get());
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * Updates a given vacancy. Updates only fields passed in a request. To unset a field pass null-value explicitly.
	 *
	 * @param id The vacancy id.
	 * @param vacancy The values to update.
	 * @return an updated entity.
	 */
	@PatchMapping(value = "/{id}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Vacancy> updateVacancy(@PathVariable Integer id, @RequestBody String vacancy) {
		Optional<Vacancy> storedVacancy = vacancyRepository.findById(id);
		if (!storedVacancy.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Vacancy sv = storedVacancy.get();
		ObjectMapper om = new ObjectMapper();
		try {
			sv = om.readerForUpdating(sv).readValue(vacancy);
		} catch (JsonProcessingException e) {
			LOG.error("Failed to parse an entity", e);
			return ResponseEntity.badRequest().build();
		}

		// vacancy id should not be updated
		if(!id.equals(sv.getVacancyId())) {
			LOG.error("Trying to update vacancy id: {}->{}", id, sv.getVacancyId());
			return ResponseEntity.badRequest().build();
		}
		validate(sv);

		vacancyRepository.save(sv);

		return ResponseEntity.ok(sv);
	}

	@DeleteMapping(value = "/{id}")
	public void deleteVacancy(@PathVariable Integer id) {
		vacancyRepository.deleteById(id);
	}
}
