package losiki.task001.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import losiki.task001.TestUtils;
import losiki.task001.conf.DaoConfiguration;
import losiki.task001.data.VacancyRepository;
import losiki.task001.model.Vacancy;

@SpringBootTest
@ContextConfiguration(classes = { VacancyRepository.class, DaoConfiguration.class })
public class VacancyRepositoryTest {
	@Autowired
	private VacancyRepository vacancyRepository;
	@Test
	public void testInsert() {
		Vacancy vacancy = new Vacancy();
		vacancy.setName(TestUtils.randomName());
		vacancy.setPosition(TestUtils.randomPosition());
		vacancy.setCity(TestUtils.randomCity());
		vacancy.setSallary(15);
		vacancyRepository.save(vacancy);
	}
}
