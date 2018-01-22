package com.xyl.game.utils;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.Page;

import java.util.List;

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
        if (list.size()>id+1) {
            page.add(list.get(id));
        }
        return page;
    }
}
