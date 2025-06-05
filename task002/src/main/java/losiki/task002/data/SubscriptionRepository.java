package losiki.task002.data;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import losiki.task002.model.Subscription;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

	@Query("SELECT s.* FROM subscriptions s JOIN (SELECT subscription_id, COUNT(*) as sub_count FROM user_subscriptions"
		+ " GROUP BY subscription_id) t ON s.subscription_id=t.subscription_id ORDER BY sub_count DESC LIMIT :limit")
	public List<Subscription> findTopN(@Param("limit") int limit);

	public Subscription findByProviderAndType(String provider, String type);

	@Modifying
	@Query("INSERT INTO user_subscriptions(user_id, subscription_id) VALUES(:userId, :subscriptionId)")
	public void subscribe(@Param("userId") Integer userId, @Param("subscriptionId") Integer subscriptionId);

	@Modifying
	@Query("DELETE FROM user_subscriptions WHERE user_id=:userId AND subscription_id=:subscriptionId")
	public void unsubscribe(@Param("userId") Integer userId, @Param("subscriptionId") Integer subscriptionId);

	@Query("SELECT s.* FROM subscriptions s JOIN user_subscriptions us ON s.subscription_id=us.subscription_id WHERE user_id=:userId")
	public List<Subscription> findAllByUserId(@Param("userId") Integer userId);


}