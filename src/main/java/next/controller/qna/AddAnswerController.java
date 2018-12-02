package next.controller.qna;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;

import core.mvc.Controller;
import next.dao.AnswerDao;
import next.model.Answer;
import next.model.User;

public class AddAnswerController implements Controller {
    private Logger logger = LoggerFactory.getLogger(AddAnswerController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String writer = req.getParameter("writer");
        String contents = req.getParameter("contents");
        Long questionId = Long.parseLong(req.getParameter("questionId"));

        logger.debug("addAnswer reqeust. writer={}, contents={}, questionId = {}, requestUset = {}",
                     writer, contents, questionId, req.getSession().getAttribute("user"));

        AnswerDao answerDao = new AnswerDao();
        Answer result = answerDao.insert(new Answer(writer, contents, questionId));

        resp.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = resp.getWriter();
        out.print(objectMapper.writeValueAsString(result));

        return null;
    }
}
