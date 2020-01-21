package com.example.demo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

@Slf4j
public class ObjectPositionSerializationSchema
		implements DeserializationSchema<ObjectPosition>, SerializationSchema<ObjectPosition> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public ObjectPosition deserialize(byte[] message) throws IOException {
		return objectMapper.readValue(message, ObjectPosition.class);
	}

	@Override
	public boolean isEndOfStream(ObjectPosition nextElement) {
		return false;
	}

	@Override
	public byte[] serialize(ObjectPosition element)  {
		try {
			return objectMapper.writeValueAsBytes(element);
		} catch (JsonProcessingException e) {
			log.error("Error serializing {}", element);
			return null;
		}
	}

	@Override
	public TypeInformation<ObjectPosition> getProducedType() {
		return TypeInformation.of(ObjectPosition.class);
	}
}
