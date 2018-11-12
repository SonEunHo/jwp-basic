package next.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.db.DataBase;
import next.model.User;

@WebServlet("/user/update")
public class UpdateUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("user", DataBase.findUserById(req.getParameter("userId")));
        RequestDispatcher rd = req.getRequestDispatcher("update.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                             req.getParameter("email"));
        User alreadyInUser = DataBase.findUserById(req.getParameter("userId"));
        alreadyInUser.changeInformation(user.getPassword(), user.getName(), user.getEmail());

        resp.sendRedirect("/user/list");
    }
}
