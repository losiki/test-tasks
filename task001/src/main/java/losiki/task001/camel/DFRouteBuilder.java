package losiki.task001.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import losiki.task001.data.DigitalFutureDao;

@Component
public class DFRouteBuilder extends RouteBuilder {
	@Autowired
	private DigitalFutureDao dao;

	@Override
	public void configure() throws Exception {
		from("direct:process").bean(dao, "getNotifications")
			.split(body())
				.setHeader("To", simple("${body.email}"))
				.to("freemarker:{{message.template}}")
				.to("{{processing.uri}}")
			.end();
		from("direct:send-log")
			.to("log:camel?showAll=true&multiline=true");
		from("direct:send-smtp")
			.setHeader("Subject", constant("Новая вакансия"))
			.to("smtp:{{smtp.server}}");
		// run at 8:15 every day
		from("quartz:scheduler?cron=0+15+8+*+*+?").to("direct:process");
	}

}
