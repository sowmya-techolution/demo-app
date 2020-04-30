package co.id.bankdki.billerdkilinkrouter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value="file:./config/application.properties")
public class BillerdkilinkRouterApplication {

	public static ApplicationContext ctx;
	public static void main(String[] args) {
		ctx = SpringApplication.run(BillerdkilinkRouterApplication.class, args);
		System.out.println("Let's inspect the beans provided by Spring Boot:");
		String[] beanNames = ctx.getBeanDefinitionNames();
	}
}