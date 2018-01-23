package com.xyl.game.utils;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.po.Answer;
import com.xyl.game.po.Page;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * QuestionUtils
 *
 * @author Naah
 * @date 2018-01-22
 */
public class QuestionUtils {
    public static Page<QuestionDTO> getNextQuestion(int id){
        List<QuestionDTO> list = HeapVariable.questionDTOList;
        Page<QuestionDTO> page=new Page<>();
        if (list.size()>=id+1) {
            page.add(list.get(id));
        }
        return page;
    }
    public static Page<QuestionDTO> getNowQuestion(){
        Page<QuestionDTO> page=new Page<>();
        page.add(HeapVariable.now);
        return page;
    }

    public static AnnualMeetingGameQuestion getQuestion(int id){
         return HeapVariable.questionsList.get(id - 1);
    }

    public static Map<Integer,ConcurrentLinkedQueue<Answer>> getAnswerMap(int id){
        return HeapVariable.answerList.get(id-1);
    }
}
