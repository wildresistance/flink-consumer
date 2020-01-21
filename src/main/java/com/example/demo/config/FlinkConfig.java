package com.example.demo.config;

import com.example.demo.model.ObjectPosition;
import com.example.demo.model.ObjectPositionSerializationSchema;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.api.java.typeutils.runtime.kryo.JavaSerializer;
import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlinkConfig {

	@Bean
	public StreamExecutionEnvironment flinkEnvironment() {
		return StreamExecutionEnvironment.
				getExecutionEnvironment().
				setParallelism(1);
	}

	@Bean
	public RMQConnectionConfig rmqConnectionConfig(@Value("${spring.rabbitmq.host}") String host,
	                                               @Value("${spring.rabbitmq.port}") int port,
	                                               @Value("${spring.rabbitmq.username}") String username,
	                                               @Value("${spring.rabbitmq.password}") String password) {
		return new RMQConnectionConfig.Builder().
				setHost(host).
				setPort(port).
				setUserName(username).
				setVirtualHost("/").
				setPassword(password).build();

	}

	@Bean
	public RMQSource<ObjectPosition> rmqSource(RMQConnectionConfig rmqConnectionConfig, @Value("${spring.cloud.stream.bindings.output.destination}") String queueName
	) {
		return new RMQSource<>(rmqConnectionConfig,
				queueName, new ObjectPositionSerializationSchema());

	}
}
