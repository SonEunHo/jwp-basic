package next.service;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;

public class QuestionServiceTest {
    private QuestionDao questionDao;
    private AnswerDao answerDao;
    private User user;
    private Question question;
    private List<Answer> answers;
    private QuestionService questionService;

    @Before
    public void init() {
        questionDao = mock(QuestionDao.class);
        answerDao = mock(AnswerDao.class);
        questionService = new QuestionService(questionDao, answerDao);

        user = new User("test", "password", "name", "test@email.com");
        question = new Question(user.getUserId(), "title", "contents");
    }

    @Test
    public void deleteSuccess() throws Exception {
        long questionId = 1;
        answers = Collections.EMPTY_LIST;

        when(questionDao.findById(questionId)).thenReturn(question);
        when(answerDao.findAllByQuestionId(questionId)).thenReturn(answers);
        assert questionService.deleteQuestion(user, questionId).isStatus();
    }

    @Test
    public void deleteFail() throws Exception {
        long questionId = 1;
        answers = Lists.newArrayList(new Answer("anotherWriter", "title", questionId));

        when(questionDao.findById(questionId)).thenReturn(question);
        when(answerDao.findAllByQuestionId(questionId)).thenReturn(answers);
        assert !questionService.deleteQuestion(user, questionId).isStatus();
    }
}
