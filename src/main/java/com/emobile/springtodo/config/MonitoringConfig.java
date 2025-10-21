package com.emobile.springtodo.config;

import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.repository.TodoRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringConfig {
    @Bean
    public ApplicationRunner customMetrics(TodoRepository repository, MeterRegistry meterRegistry) {
        return args -> {
            meterRegistry.gauge("todos.completed", repository, repo ->
                    repo.findAll(1000, 0).stream().filter(t -> t.status() == Todo.Status.DONE).count());
        };
    }
}
