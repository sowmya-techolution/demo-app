package co.id.bankdki.billerdkilinkrouter;

import org.jpos.q2.Q2;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by bankdki on 3/30/15.
 */

@Configuration
public class SpringQ2 extends Q2 {
    @PostConstruct
    public void init() {
        SpringQ2 q2 = new SpringQ2();
        q2.start();
    }
}