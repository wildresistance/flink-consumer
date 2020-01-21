package com.example.demo;

import com.example.demo.consumer.FlinkConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	@Resource
	public FlinkConsumer flinkConsumer;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@EventListener(classes = ApplicationReadyEvent.class)
	public void onApplicationStart() {
		CompletableFuture.runAsync(() -> {
			try {
				flinkConsumer.runJob();
			} catch (Exception e) {
				log.error("Error running flink job", e);
			}
		});
	}

}
