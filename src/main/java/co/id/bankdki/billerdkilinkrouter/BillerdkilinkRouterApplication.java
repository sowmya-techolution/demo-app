package co.id.bankdki.billerdkilinkrouter;

import co.id.bankdki.billerdkilinkrouter.iso.repository.HistoryService;
import org.jpos.util.NameRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BillerdkilinkRouterApplication {

	public static ApplicationContext ctx;
	public static void main(String[] args) {
		ctx = SpringApplication.run(BillerdkilinkRouterApplication.class, args);
		System.out.println("Let's inspect the beans provided by Spring Boot:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		//Arrays.sort(beanNames);
		//	for (String beanName : beanNames) {
		//		System.out.println(beanName);
		//	}

		HistoryService historyService = (HistoryService) ctx.getBean("historyImpl");
		NameRegistrar.register("historyImpl", historyService);
	}
}
