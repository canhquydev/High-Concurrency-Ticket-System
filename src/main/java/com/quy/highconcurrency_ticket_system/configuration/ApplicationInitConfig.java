package com.quy.highconcurrency_ticket_system.configuration;

import com.quy.highconcurrency_ticket_system.model.Ticket;
import com.quy.highconcurrency_ticket_system.repository.TicketRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@Configuration
public class ApplicationInitConfig {

    private final StringRedisTemplate redisTemplate;

    public ApplicationInitConfig(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    ApplicationRunner applicationRunner(TicketRepository ticketRepository){
        return args -> {
            List<Ticket> ticketList = ticketRepository.findAll();
            for(Ticket t: ticketList){
                String redisKey = "ticket:stock:" + t.getId();
                redisTemplate.opsForValue().set(redisKey, String.valueOf(t.getAvailableStock()));
            }
        };
    }
}
