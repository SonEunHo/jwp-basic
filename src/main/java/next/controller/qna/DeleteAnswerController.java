package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;

import core.mvc.Controller;
import next.dao.AnswerDao;
import next.model.Answer;
import next.model.Result;

public class DeleteAnswerController implements Controller {
    private Logger logger = LoggerFactory.getLogger(DeleteAnswerController.class);
    private AnswerDao answerDao = new AnswerDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Result result;
        Long answerId = Long.parseLong(req.getParameter("answerId"));
        logger.debug("[deleteAnswer request] answerId = {}, requestUser = {}",
                     answerId, req.getSession().getAttribute("user"));

        Answer answer = answerDao.findById(answerId);
        if(answer != null) {
            result = tryToDeleteAnswer(answerId);
        } else {
            result = Result.fail("there is no the answer");
        }
        logger.debug("[deleteAnswer result] {}",
                     result.getMessage().isEmpty() ? "success" : result.getMessage());
        resp.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        resp.getWriter().print(new ObjectMapper().writeValueAsString(result));
        return null;
    }

    private Result tryToDeleteAnswer(long answerId) {
        answerDao.deleteByQuestionId(answerId);
        Answer answer = answerDao.findById(answerId);
        if(answer == null) {
            return Result.ok();
        } else {
            return Result.fail("fail to delete answer");
        }
    }
}
