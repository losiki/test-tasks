package losiki.task002.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table("subscriptions")
public class Subscription {
	@Id
	private Integer subscriptionId;
	/** Provider id: url, name, etc. */
	private String provider;
	/** Subscription type: basic, elite, advanced... */
	private String type;
	// other attributes
}
