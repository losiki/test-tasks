package losiki.task001.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import losiki.task001.model.Subscriber;

@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber, Integer> {
}
