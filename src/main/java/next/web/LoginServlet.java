package next.web;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.http.HTTPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.db.DataBase;
import next.model.User;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        logger.info("[login request] userId = {}, password = {}", userId, password);

        User user = DataBase.findUserById(userId);
        if(Objects.isNull(user)) {
            throw new HTTPException(400);
        }

        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("user", user);

        resp.sendRedirect("/index.jsp");
    }
}
