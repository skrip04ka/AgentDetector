package org.example.helper;

import lombok.SneakyThrows;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonParser {
    private static ObjectMapper mapper = new ObjectMapper();

    private JsonParser (){}

    @SneakyThrows
    public static <T> String dataToString(T dataClass) {
        return mapper.writeValueAsString(dataClass);
    }
    @SneakyThrows
    public static <T> T parseData(String dataString, Class<T> clazz) {
        return mapper.readValue(dataString, clazz);
    }
}
