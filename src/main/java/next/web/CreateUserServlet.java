package next.web;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.db.DataBase;

@WebServlet("/user/create")
public class CreateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(CreateUserServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("user : {}", user);

        User alreadySignedUser = DataBase.findUserById(user.getUserId());
        if(Objects.isNull(alreadySignedUser)) {
            log.debug("[signup] user: {}", user);
            DataBase.addUser(user);
        } else {
            log.debug("[change Member Information before: {}, after: {}", alreadySignedUser, user);
            alreadySignedUser.changeInformation(user.getPassword(), user.getName(), user.getEmail());
        }

        resp.sendRedirect("/user/list");
    }
}
