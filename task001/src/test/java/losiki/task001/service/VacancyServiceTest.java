package losiki.task001.service;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import losiki.task001.TestUtils;
import losiki.task001.conf.DaoConfiguration;
import losiki.task001.conf.WebAdvice;
import losiki.task001.data.VacancyRepository;
import losiki.task001.model.Vacancy;
import losiki.task001.service.VacancyService;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { VacancyService.class, WebAdvice.class, DaoConfiguration.class })
public class VacancyServiceTest {
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private VacancyRepository vacancyRepository;

	@Test
	public void testCreateVacancy() {
		Vacancy vacancy = new Vacancy();
		vacancy.setName(TestUtils.randomName());
		vacancy.setCity(TestUtils.randomCity());
		vacancy.setPosition(TestUtils.randomPosition());
		vacancy.setSallary(200000);
		restTemplate.put("/vacancy", vacancy);

		// check that the vacancy is persisted and math to the original entity
		Vacancy v2 = vacancyRepository.findByName(vacancy.getName());
		assertNotNull(v2);
		assertNotNull(v2.getVacancyId());
		assertThat(v2, samePropertyValuesAs(vacancy, "vacancyId"));

		Vacancy v3 = restTemplate.getForObject("/vacancy/" + v2.getVacancyId(), Vacancy.class);
		assertNotNull(v3);
		assertEquals(v2, v3);

		List<Vacancy> vacancies = getVacancies(null, null, null);
		assertThat(vacancies, hasItem(v3));
		vacancies = getVacancies(v3.getName(), null, null);
		assertThat(vacancies, hasItem(v3));
		vacancies = getVacancies(v3.getName(), v3.getPosition(), null);
		assertThat(vacancies, hasItem(v3));
		vacancies = getVacancies(v3.getName(), v3.getPosition(), v3.getCity());
		assertThat(vacancies, hasItem(v3));
		vacancies = getVacancies(null, v3.getPosition(), null);
		assertThat(vacancies, hasItem(v3));
		vacancies = getVacancies(null, v3.getPosition(), v3.getCity());
		assertThat(vacancies, hasItem(v3));
		vacancies = getVacancies(null, null, v3.getCity());
		assertThat(vacancies, hasItem(v3));
	}

	private List<Vacancy> getVacancies(String name, String position, String city) {
		String url = "/vacancy?1";
		if (name != null) {
			try {
				url += "&name=" + URLEncoder.encode(name, StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (position != null) {
			try {
				url += "&position=" + URLEncoder.encode(position, StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (city != null) {
			try {
				url += "&city=" + URLEncoder.encode(city, StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Vacancy[] vacancies = restTemplate.getForObject(url, Vacancy[].class);
		assertNotNull(vacancies);
		return Stream.of(vacancies).collect(Collectors.toList());
	}
	
	@Test
	public void testDeleteVacation() {
		Vacancy vacancy = new Vacancy();
		vacancy.setName(TestUtils.randomName());
		vacancy.setCity(TestUtils.randomCity());
		vacancy.setPosition(TestUtils.randomPosition());
		vacancy.setSallary(200000);
		restTemplate.put("/vacancy", vacancy);
		
		Vacancy v2 = vacancyRepository.findByName(vacancy.getName());
		
		ResponseEntity<Vacancy> entity = restTemplate.getForEntity("/vacancy/"+v2.getVacancyId(), Vacancy.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(v2, entity.getBody());
		
		restTemplate.delete("/vacancy/"+v2.getVacancyId());
		
		Optional<Vacancy> v3 = vacancyRepository .findById(v2.getVacancyId());
		assertFalse(v3.isPresent());
		
		entity = restTemplate.getForEntity("/vacancy/"+v2.getVacancyId(), Vacancy.class);
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
	}
}
