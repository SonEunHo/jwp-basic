package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;

public class AddQuestionController extends AbstractController {
    private QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        String writer = ((User)(request.getSession().getAttribute("user"))).getUserId();
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        Question question = new Question(writer, title, contents);
        questionDao.insert(question);

        return jspView("redirect:/");
    }
}
