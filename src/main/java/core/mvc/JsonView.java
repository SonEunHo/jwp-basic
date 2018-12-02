package core.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonView implements View {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private Object payload;

    public JsonView(Object payload) {
        this.payload = payload;
    }

    @Override
    public void render(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        response.getWriter().write(objectMapper.writeValueAsString(payload));
    }
}
