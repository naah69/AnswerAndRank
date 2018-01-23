package com.xyl.game.contrller;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.Page;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.QuestionUtils;
import com.xyl.game.websocket.AnswerWebSocket;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * sendAnswer
 *
 * @author Naah
 * @date 2018-01-23
 */
@RequestMapping("/admin")
@RestController
public class SendController {
    @PostMapping("/sendAnswer")
    public GridPage send() {
        GridPage result = new GridPage();
        if (HeapVariable.beginTime == null||HeapVariable.now==null) {
            result.setErrorCode("8");
            result.setMessage("还没有题目！");
            return result;
        }
        result.setErrorCode("0");
        try {
            GridPage answer = new GridPage();
            answer.setMethod("answer");
            answer.setErrorCode("0");
            answer.setMessage(QuestionUtils.getAnswerNow());
            AnswerWebSocket.sendGridPageToAll(answer);
        } catch (Exception e) {
            result.setErrorCode("500");
        }
        return result;
    }

    @PostMapping("/sendQuestion")
    public GridPage sendQuestion() {
        GridPage result = new GridPage();
        if (HeapVariable.beginTime == null) {
            result.setErrorCode("8");
            result.setMessage("没有游戏场次");
            return result;
        }

        result.setErrorCode("0");
        GridPage question = new GridPage();
        question.setMethod("question");
        QuestionDTO questionDTO = QuestionUtils.nextQuestion();
        if (questionDTO == null) {
            result.setErrorCode("5");
            result.setMessage("游戏结束!");
            question.setErrorCode("5");
            question.setMessage("游戏结束!");
        } else {
            Page<QuestionDTO> page = new Page<>();
            Page<AnnualMeetingGameQuestion> manage = new Page<>();
            manage.add(QuestionUtils.getQuestion(questionDTO.getId()));
            page.add(questionDTO);
            question.setErrorCode("0");
            question.setMessage("next");
            question.setPageList(page);
            result.setPageList(manage);
        }

        AnswerWebSocket.sendGridPageToAll(question);

        return result;
    }
}
