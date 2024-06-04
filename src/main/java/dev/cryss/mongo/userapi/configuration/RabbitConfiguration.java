package dev.cryss.mongo.userapi.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

@Configuration
public class RabbitConfiguration {

    @Bean
    @Qualifier("mongoConnectionFactory")
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");



        return connectionFactory;
    }


    @Bean
    @Primary
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new ParameterNamesModule ())
                .registerModule(new Jdk8Module ())
                .registerModule(new JavaTimeModule());

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);// will remove value properties
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return new Jackson2JsonMessageConverter(mapper);
    }


    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin (connectionFactory());
    }

    @Bean
    @Resource(name = "mongoConnectionFactory")
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        template.setUsePublisherConnection (true);
        return template;
    }


    void declarMongoUserQueue(){
        amqpAdmin().declareQueue (mongoUserQueue());
        amqpAdmin().declareExchange (marketDataExchange());
    }

    @Bean
    public FanoutExchange marketDataExchange() {
        return new FanoutExchange("mongo.user.exchange");
    }

    @Bean
    public Queue mongoUserQueue() {
        return new Queue("mongo.user.queue");
    }


    @Bean
    public Binding mongoUserDataBinding() {
        return BindingBuilder
                .bind(
                mongoUserQueue())
                .to(marketDataExchange());
    }

}
