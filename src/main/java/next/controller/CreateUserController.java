package next.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.db.DataBase;
import next.controller.annotation.RequestMapping;
import next.model.User;

public class CreateUserController implements Controller {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @RequestMapping(method = "GET", value="/users/form")
    public void userFormPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/user/form.jsp");
        rd.forward(req, resp);
    }

    @RequestMapping(method = "POST", value = "/users/create")
    public void createUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                             req.getParameter("email"));
        log.debug("User : {}", user);

        DataBase.addUser(user);

        resp.sendRedirect("/");
    }
}
