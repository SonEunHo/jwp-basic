package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import core.mvc.Controller;
import core.mvc.JsonView;
import core.mvc.View;
import next.dao.AnswerDao;
import next.model.Result;

public class DeleteAnswerController implements Controller {
    @Override
    public View execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Long answerId = Long.parseLong(req.getParameter("answerId"));
        AnswerDao answerDao = new AnswerDao();

        answerDao.delete(answerId);

        ObjectMapper mapper = new ObjectMapper();
        return new JsonView(Result.ok());
    }
}
