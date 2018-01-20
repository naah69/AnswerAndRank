package com.xyl.game.Service;

import java.io.InputStream;

import com.xyl.game.vo.AnnualMeetingGameQuestionVo;

/**
 * 用解析exct表格
 * @author dazhi
 */
public interface AnnualMeetingQuestionExctFileSerivce {
	/**
	 * 解析exct表格
	 * @param exctFileStream
	 * @return
	 */
	public AnnualMeetingGameQuestionVo savaDataForExct(InputStream exctFileStream);
	
	
	
}
