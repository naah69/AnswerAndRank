package com.xyl.game.Service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.game.Service.AnnualMeetingQuestionExctFileSerivce;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;

@Service
public class AnnualMeetingQuestionExctFileServiceImpl implements AnnualMeetingQuestionExctFileSerivce{

	private static final Logger logger = LoggerFactory.getLogger(AnnualMeetingQuestionExctFileServiceImpl.class);
	/*@Autowired
	private AnnualMeetingGameQuestionMapper annualMeetingGameQuestionMapper;*/
	
	@Override
	public AnnualMeetingGameQuestionVo savaDataForExct(InputStream exctFileStream){
		//创建对Excel工作簿文件的引用
		Workbook workbook = null;
		AnnualMeetingGameQuestionVo annualMeetingGameQuestionVo= new AnnualMeetingGameQuestionVo();
		
		try {
			try{
				workbook = new HSSFWorkbook(exctFileStream);
			}catch(Exception e){
				//上传的文件可能是offic2007以上的版本,记录日志
				logger.info("可能上传offic2007以上的版本");
				workbook = new XSSFWorkbook();
			}
			List<AnnualMeetingGameQuestion> analyzingExctData = analyzingExctData(workbook);
			
			//封装解析出来的所有exct数据
			annualMeetingGameQuestionVo.setAllQuestions(analyzingExctData);
			annualMeetingGameQuestionVo.setState(1);
			annualMeetingGameQuestionVo.setStateInfo("success");
			
			return annualMeetingGameQuestionVo;
		} catch (Exception e) {
			logger.error("文件加载异常",e.toString());
			annualMeetingGameQuestionVo.setState(2);
			annualMeetingGameQuestionVo.setStateInfo("文件加载异常");
			
			return annualMeetingGameQuestionVo;
		} finally {
			if(workbook != null){
				try {
					workbook.close();
				} catch (IOException e) {
					logger.warn("workbook资源没有关闭");
				}
			}
		}
	}
	
	private List<AnnualMeetingGameQuestion> analyzingExctData(Workbook workbook) throws Exception{
		return null;
	}

	/**
	 * 获得所有年会问题数据
	 */
	
	/*
	 * @Override
	 * public AnnualMeetingGameQuestionVo getAllGameQuestion() {
		try {
			List<AnnualMeetingGameQuestion> selectAll = annualMeetingGameQuestionMapper.selectAll();
			
			Integer allConut = annualMeetingGameQuestionMapper.getAllConut();
			
			return new AnnualMeetingGameQuestionVo(selectAll, AnnualMeetingGameQuestionVo.STATE_NUM_SUCCESS, "success", allConut);
		} catch (Exception e) {
			logger.error("数据查询异常");
			return new AnnualMeetingGameQuestionVo(null, AnnualMeetingGameQuestionVo.STATE_NUM_EXCEPTION, "数据查询异常" , 0);
		}
		
	}*/

}
