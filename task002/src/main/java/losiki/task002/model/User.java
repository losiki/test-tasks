package losiki.task002.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table("users")
public class User {
	@Id
	private Integer userId;
	private String name;
	private String email;
	// here goes other user properties such as password, cards, birth date and so on
}
