package com.xyl.game.vo;

import java.util.List;

import com.xyl.game.po.AnnualMeetingGameQuestion;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnnualMeetingGameQuestionVo {
	//exct文件所有数据
	private List<AnnualMeetingGameQuestion> allQuestions;
	//加载后的状态：0->无状态,1->成功,2->异常
	private int state;
	//加载后的状态的信息
	private String stateInfo;
	//数据的数量
	private Integer count;
}
