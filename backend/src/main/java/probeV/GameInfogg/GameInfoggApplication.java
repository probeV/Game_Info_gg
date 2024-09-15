package probeV.GameInfogg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GameInfoggApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameInfoggApplication.class, args);
	}

}
