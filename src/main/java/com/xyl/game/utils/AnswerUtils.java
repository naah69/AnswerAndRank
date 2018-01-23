package com.xyl.game.utils;

import com.xyl.game.dto.AnswerDTO;
import com.xyl.game.po.Page;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AnswerUtils
 *
 * @author Naah
 * @date 2018-01-23
 */
public class AnswerUtils {
     public static int getAnswerNow() {
        return HeapVariable.questionsList.get(HeapVariable.now.getId() - 1).getRightAnswer() ;
    }
    public static Page<AnswerDTO> getAnswerCount(){
        int id=HeapVariable.now.getId();
        int rightAnswer = AnswerUtils.getAnswerNow();
        Map<Integer, AtomicInteger> resultMap = HeapVariable.answerList.get(id-1);
        int A=resultMap.get(1).get();
        int B=resultMap.get(2).get();
        int C=resultMap.get(3).get();
        int D=resultMap.get(4).get();
        int NULL=resultMap.get(0).get();
         AnswerDTO count=new AnswerDTO(id,rightAnswer,A,B,C,D,NULL);
         Page<AnswerDTO> page=new Page<>();
         page.add(count);
         return  page;
    }
}
