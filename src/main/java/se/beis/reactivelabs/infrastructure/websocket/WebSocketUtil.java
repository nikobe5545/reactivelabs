package se.beis.reactivelabs.infrastructure.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.reactive.socket.WebSocketMessage;
import se.beis.reactivelabs.domain.Customer;

import java.nio.charset.StandardCharsets;

class WebSocketUtil {

    private static DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

    private static ObjectMapper objectMapper = new ObjectMapper();

    static WebSocketMessage jsonToWebSocketMessage(String json) {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        return new WebSocketMessage(WebSocketMessage.Type.TEXT, dataBufferFactory.wrap(bytes));
    }

    static String customerToJson(Customer customer) {
        try {
            return objectMapper.writeValueAsString(customer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }
}
