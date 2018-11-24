package next.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.controller.RequestMapper.RequestMapNode;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestMapNode dispatch = getRequestMethod(req);
        Method m = dispatch.method;
        Controller controller = dispatch.controller;
        try {
            m.invoke(controller, req, resp);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestMapNode dispatch = getRequestMethod(req);
        Method m = dispatch.method;
        Controller controller = dispatch.controller;
        try {
            m.invoke(controller, req, resp);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public RequestMapNode getRequestMethod(HttpServletRequest req) {
        //TODO requestUri가 맞을지..
        return RequestMapper.getMethod(req.getRequestURI());
    }

}
