package com.irctn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class CustomRedisSerializer implements RedisSerializer<Object> {

	private Converter<Object, byte[]> serializer = new SerializingConverter();
	private Converter<byte[], Object> deserializer = new DeserializingConverter();

	static final byte[] EMPTY_ARRAY = new byte[0];

	private static final Logger LOGGER = LoggerFactory.getLogger("com.uniboard.config.CustomRedisSerializer");
	
	public Object deserialize(byte[] bytes) {
		if (isEmpty(bytes)) {
			return null;
		}
		try {
			return deserializer.convert(bytes);
		} catch (Exception ex) {
			throw new SerializationException("Cannot deserialize", ex);
		}
	}

	public byte[] serialize(Object object) {
		if (object == null) {
			return EMPTY_ARRAY;
		}
		try {
			return serializer.convert(object);
		} catch (Exception ex) {
			LOGGER.debug("serialize exception ......" + ex);
			return EMPTY_ARRAY;
		}
	}

	private boolean isEmpty(byte[] data) {
		return (data == null || data.length == 0);
	}
}