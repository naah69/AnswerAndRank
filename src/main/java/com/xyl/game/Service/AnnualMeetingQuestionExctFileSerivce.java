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
	
	//public AnnualMeetingGameQuestionVo getAllGameQuestion();
	
	/**
	 * 修改问题数据的缓存
	 * @param vo
	 * @param id
	 * @param fieldValue
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public Boolean updataData(AnnualMeetingGameQuestionVo vo,Integer id,String fieldValue,String fieldName) throws Exception;
	
}
