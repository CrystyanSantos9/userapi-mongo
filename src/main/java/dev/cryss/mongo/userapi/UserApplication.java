package dev.cryss.mongo.userapi;

import dev.cryss.mongo.userapi.domain.model.UserDetailEntity;
import dev.cryss.mongo.userapi.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import javax.annotation.Resources;

@SpringBootApplication
@RequiredArgsConstructor
public class UserApplication implements CommandLineRunner {

	private final MongoTemplate mongoTemplate;
	private final SequenceGeneratorService sequence;

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(AmqpTemplate template) {
		UserDetailEntity entity = mongoTemplate.findById (2L, UserDetailEntity.class);
		return args -> template.convertAndSend("mongo.user.exchange", "", entity);
	}


	@RabbitListener(queues = "mongo.user.queue")
	public void listen(UserDetailEntity in) {
		System.out.println("Da fila: " + in);
	}


	@Override
	public void run(String... args) throws Exception {
//		UserDetailEntity user = UserDetailEntity
//				.builder ()
//				.id (sequence.generateSequence ("seq_user"))
//				.lastName ("Paulo")
//				.firstName ("Gustavo")
//				.build ();
//
//		mongoTemplate.save (user);
//mongoTemplate.remove (Query.query (Criteria.where ("_id").is (1L)), UserDetailEntity.class);
//		System.out.println (mongoTemplate.findAll (UserDetailEntity.class, "UserDetail"));
	}
}
