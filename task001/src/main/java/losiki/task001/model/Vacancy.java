package losiki.task001.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "vacancies")
public class Vacancy {
	@Id
	@Column("vacancy_id")
	private Integer vacancyId;
	private String name;
	private String description;
	private String position;
	private int sallary;
	@JsonProperty(value ="required_experience")
	@Column("required_experience")
	private String requiredExperience;
	private String city;
}
