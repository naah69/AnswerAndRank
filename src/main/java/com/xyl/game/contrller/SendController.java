package com.xyl.game.contrller;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.Page;
import com.xyl.game.utils.QuestionUtils;
import com.xyl.game.websocket.AnswerWebSocket;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * sendAnswer
 *
 * @author Naah
 * @date 2018-01-23
 */
@RestController
public class SendController {
    @PostMapping("sendAnswer")
    public GridPage send() {
        GridPage result = new GridPage();
        result.setErrorCode("0");
        try {
            GridPage answer = new GridPage();
            answer.setErrorCode("4");
            answer.setMessage(QuestionUtils.getAnswerNow());
            AnswerWebSocket.sendGridPageToAll(answer);
        } catch (Exception e) {
            result.setErrorCode("500");
        }
        return result;
    }

    @PostMapping("sendQuestion")
    public GridPage sendQuestion() {
        GridPage result = new GridPage();
        result.setErrorCode("0");
        try {
            GridPage question = new GridPage();
            QuestionDTO questionDTO = QuestionUtils.nextQuestion();
            if (questionDTO == null) {
                result.setErrorCode("5");
                result.setMessage("done!");
                question.setErrorCode("6");
                question.setMessage("done");
            } else {
                Page<QuestionDTO> page = new Page<>();
                page.add(questionDTO);
                question.setErrorCode("5");
                question.setMessage("next");
                question.setPageList(page);
            }

            AnswerWebSocket.sendGridPageToAll(question);
        } catch (Exception e) {
            result.setErrorCode("500");
        }
        return result;
    }
}
