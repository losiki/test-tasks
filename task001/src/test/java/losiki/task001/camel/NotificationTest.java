package losiki.task001.camel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import losiki.task001.camel.DFRouteBuilder;
import losiki.task001.conf.DaoConfiguration;
import losiki.task001.data.SubscriberRepository;
import losiki.task001.data.VacancyRepository;
import losiki.task001.model.Subscriber;
import losiki.task001.model.Vacancy;

@SpringBootTest
@CamelSpringBootTest
@ContextConfiguration(classes = { DFRouteBuilder.class, DaoConfiguration.class })
@MockEndpoints("direct:send-log")
public class NotificationTest {
	@Autowired
	private ProducerTemplate template;
	@EndpointInject("mock:direct:send-log")
	private MockEndpoint endpoint;

	@Autowired
	private VacancyRepository vacancyRepo;
	@Autowired
	private SubscriberRepository subscriberRepo;

	@Test
	public void testSend() throws InterruptedException {
		vacancyRepo.deleteAll();
		subscriberRepo.deleteAll();

		endpoint.expectedMessageCount(1);

		Vacancy v = new Vacancy();
		v.setName("Dream Work");
		v.setCity("city");
		v.setPosition("position");
		v.setSallary(1);
		v.setDescription("description");
		//v.setRequiredExpirience("required experience");
		v = vacancyRepo.save(v);

		Subscriber s = new Subscriber();
		s.setEmail("a@example.org");
		s.setFullname("Full Name");
		s.setPosition(v.getPosition());
		s.setCity(v.getCity());
		s = subscriberRepo.save(s);

		template.sendBody("direct:process","");
		endpoint.assertIsSatisfied();
		List<Exchange> exchanges = endpoint.getReceivedExchanges();
		String msg = exchanges.get(0).getIn().getBody(String.class);
		assertThat(msg).contains(v.getPosition());
	}

}
