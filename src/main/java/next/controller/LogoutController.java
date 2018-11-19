package next.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.controller.annotation.RequestMapping;

public class LogoutController extends HttpServlet implements Controller {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    @RequestMapping(method = "GET", value = "/users/logout")
    public void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        resp.sendRedirect("/");
    }
}
