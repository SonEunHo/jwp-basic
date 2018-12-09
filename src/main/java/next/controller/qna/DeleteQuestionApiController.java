package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.model.Result;
import next.model.User;
import next.service.QuestionService;

public class DeleteQuestionApiController extends AbstractController {
    private QuestionService questionService;

    public DeleteQuestionApiController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        User currentUser = (User) request.getSession().getAttribute("user");

        Result result = questionService.deleteQuestion(currentUser, questionId);

        return jsonView().addObject("result", result);
    }
}
