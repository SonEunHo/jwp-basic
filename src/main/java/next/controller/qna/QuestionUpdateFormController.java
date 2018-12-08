package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;
import sun.plugin.dom.exception.InvalidStateException;

public class QuestionUpdateFormController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(QuestionUpdateFormController.class);
    private QuestionDao questionDao = new QuestionDao();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = questionDao.findById(questionId);

        if(!checkWriter(question, request)) {
            logger.info("question is not user's question = {}, sessionUser = {}", question, request.getSession().getAttribute("user"));
            throw new InvalidStateException("user mathcing fail");
        }

        return jspView("/qna/update.jsp")
                .addObject("question", question);
    }

    private boolean checkWriter(Question question, HttpServletRequest req) {
        return question.getWriter().equals(((User)(req.getSession().getAttribute("user"))).getUserId());
    }
}
