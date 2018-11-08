package com.hclc.kafkastreamsinspring.notifications;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;
import java.util.Map;

public class SerdeFactory {

    private SerdeFactory() {
        throw new AssertionError();
    }

    public static <T> Serde<T> serdeForType(Class<T> clazz) {
        Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<T> ser = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", clazz);
        ser.configure(serdeProps, false);

        final Deserializer<T> de = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", clazz);
        de.configure(serdeProps, false);

        return Serdes.serdeFrom(ser, de);
    }
}
