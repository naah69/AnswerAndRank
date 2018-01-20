package com.xyl.game.Service;

import java.io.InputStream;

/**
 * 用解析exct表格
 * @author dazhi
 */
public interface AnnualMeetingQuestionExctFileSerivce {
	public Integer savaDataForExct(InputStream exctFileStream) throws Exception;
}
