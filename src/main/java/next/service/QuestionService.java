package next.service;

import java.util.List;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.Result;
import next.model.User;

public class QuestionService {
    private QuestionDao questionDao = QuestionDao.getInstance();
    private AnswerDao answerDao = AnswerDao.getInstance();

    public Result deleteQuestion(User currentUser, long questionId) throws Exception {
        Question question = questionDao.findById(questionId);

        if (!checkWriter(question, currentUser)) {
            return Result.fail("UnAuthorized");
        }

        List<Answer> answers = answerDao.findAllByQuestionId(questionId);

        if (canDelete(currentUser, answers)) {
            answers.forEach(e -> answerDao.delete(e.getAnswerId()));
            questionDao.delete(questionId);
            return Result.ok();
        } else {
            return Result.fail("다른 사람들의 답변이 있으면 삭제할 수 없습니다.");
        }
    }

    private boolean canDelete(User writer, List<Answer> answers) {
        long answerCountByOtherPeople = answers
                .stream()
                .filter(a -> !a.getWriter().equals(writer.getUserId()))
                .count();

        return answerCountByOtherPeople == 0;
    }

    private boolean checkWriter(Question question, User user) {
        return question.getWriter().equals(user.getUserId());
    }
}
