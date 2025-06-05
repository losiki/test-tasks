package losiki.task001.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "subscribers")
public class Subscriber {
	@Id
	@Column("subscriber_id")
	private Integer subscriberId;
	private String email;
	private String fullname;
	private String city;
	private String position;
}
