package com.xyl.game.Service;

import java.io.InputStream;

import com.xyl.game.vo.AnnualMeetingGameQuestionVo;

/**
 * 用解析exct表格
 * @author dazhi
 */
public interface AnnualMeetingQuestionExctFileSerivce {
	public AnnualMeetingGameQuestionVo savaDataForExct(InputStream exctFileStream);
}
