package com.example.demo.consumer;

import com.example.demo.model.ObjectPosition;
import com.example.demo.model.ObjectPositionProcessed;
import com.example.demo.util.DistanceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Slf4j
public class FlinkConsumer {

	@Resource
	StreamExecutionEnvironment streamExecutionEnvironment;

	@Resource
	RMQSource<ObjectPosition> rmqSource;

	public void runJob() throws Exception {
		final Tuple2<BigDecimal, BigDecimal> initial = new Tuple2<>(BigDecimal.valueOf(-65.5), BigDecimal.valueOf(-43.5));
		final long initialTime = LocalDateTime.of(2019, 1, 9, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);

		DataStreamSource<ObjectPosition> source = streamExecutionEnvironment
				.addSource(rmqSource)
				.setParallelism(1);
		log.info("Connected to Rabbit MQ");

		KeyedStream<ObjectPosition, String> keyedEdits = source.keyBy(ObjectPosition::getObject);
		DataStream<Tuple2<String, ObjectPositionProcessed>> result = keyedEdits
				.timeWindow(Time.seconds(5))
				.aggregate(new AggregateFunction<ObjectPosition, Tuple2<String, ObjectPositionProcessed>, Tuple2<String, ObjectPositionProcessed>>() {
					@Override
					public Tuple2<String, ObjectPositionProcessed> createAccumulator() {
						return new Tuple2<>("", new ObjectPositionProcessed("", initial.f0, initial.f1, initial.f0, initial.f1, initialTime, initialTime, 0.0, 0.0));
					}

					@Override
					public Tuple2<String, ObjectPositionProcessed> add(ObjectPosition value, Tuple2<String, ObjectPositionProcessed> accumulator) {
						accumulator.f0 = value.getObject();
						double distance = DistanceUtil.calculate(accumulator.f1.getLatitude2().doubleValue(),
								accumulator.f1.getLongitude2().doubleValue(),
								value.getLatitude().doubleValue(),
								value.getLongitude().doubleValue());
						double speed = distance / Math.abs(accumulator.f1.getTo() - value.getTimestamp()) * 1000;
						accumulator.f1 = new ObjectPositionProcessed(value.getObject(), accumulator.f1.getLatitude2(), accumulator.f1.getLongitude2(), value.getLatitude(),
								value.getLongitude(), accumulator.f1.getTo(), value.getTimestamp(), accumulator.f1.getDistance() + distance, speed);
						return accumulator;
					}

					@Override
					public Tuple2<String, ObjectPositionProcessed> getResult(Tuple2<String, ObjectPositionProcessed> accumulator) {
						return accumulator;
					}

					@Override
					public Tuple2<String, ObjectPositionProcessed> merge(Tuple2<String, ObjectPositionProcessed> a, Tuple2<String, ObjectPositionProcessed> b) {
						return b;
					}
				});
		result.print();
		streamExecutionEnvironment.execute();
	}
}
