package losiki.task001.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import losiki.task001.model.Vacancy;

@Repository
public interface VacancyRepository extends CrudRepository<Vacancy, Integer> {
	Vacancy findByName(String name);
}
