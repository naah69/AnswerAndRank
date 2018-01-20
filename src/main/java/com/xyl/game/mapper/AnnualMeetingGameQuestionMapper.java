package com.xyl.game.mapper;

import com.xyl.game.po.AnnualMeetingGameQuestion;

import java.util.List;

public interface AnnualMeetingGameQuestionMapper {
    public void insertQuestion(List<AnnualMeetingGameQuestion> list);
    public List<AnnualMeetingGameQuestion> selectAll();
    
    /**
     * 获得数据的总数
     * @return
     */
    public Integer getAllConut();
}
