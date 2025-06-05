package losiki.task001.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;

import losiki.task001.model.Vacancy;
import losiki.task001.model.VacancySubscriber;

@Service
public class DigitalFutureDao {
	@Autowired
	private NamedParameterJdbcOperations jdbcTemplate;

	public List<Vacancy> getVacancies(String name, String position, String city) {
		String query = "SELECT * FROM vacancies WHERE 1=1";
		Map<String, Object> params = new HashMap<>();
		if(name != null) {
			query += " AND name=:name";
			params.put("name", name);
		}
		if(city != null) {
			query += " AND city=:city";
			params.put("city", city);
		}
		if(position != null) {
			query += " AND position=:position";
			params.put("position", position);
		}

		return jdbcTemplate.query(query , params, new BeanPropertyRowMapper<>(Vacancy.class));
	}
	
	public List<VacancySubscriber> getNotifications() {
		return jdbcTemplate.query("SELECT v.*, s.subscriber_id, s.email, s.fullname FROM vacancies v, subscribers s "
				+ "WHERE v.city=s.city AND v.position=s.position", new BeanPropertyRowMapper<>(VacancySubscriber.class));
	}
}
