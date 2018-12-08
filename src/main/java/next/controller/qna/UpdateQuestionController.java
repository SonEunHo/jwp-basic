package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;
import sun.plugin.dom.exception.InvalidStateException;

public class UpdateQuestionController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(UpdateQuestionController.class);
    private QuestionDao questionDao = new QuestionDao();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        Question question = questionDao.findById(questionId);

        if(checkWriter(question, request)) {
            Question updatedQuestion = new Question(question.getQuestionId(),
                                                    question.getWriter(),
                                                    title,
                                                    contents,
                                                    question.getCreatedDate(),
                                                    question.getCountOfComment());

            questionDao.update(updatedQuestion);
        } else {
            logger.info("question is not user's question = {}, sessionUser = {}", question, request.getSession().getAttribute("user"));
            throw new InvalidStateException("user mathcing fail");
        }

        return jspView("redirect:/qna/show?questionId="+questionId);
    }

    private boolean checkWriter(Question question, HttpServletRequest req) {
        return question.getWriter().equals(((User)(req.getSession().getAttribute("user"))).getUserId());
    }
}
