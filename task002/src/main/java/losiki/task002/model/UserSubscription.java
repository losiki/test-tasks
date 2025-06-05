package losiki.task002.model;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table("user_subscriptions")
public class UserSubscription {
	private Integer userId;
	private Integer subscriptionId;
}
