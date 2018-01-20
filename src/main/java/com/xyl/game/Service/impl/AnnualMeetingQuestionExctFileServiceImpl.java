package com.xyl.game.Service.impl;

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

@Service
public class AnnualMeetingQuestionExctFileServiceImpl implements AnnualMeetingQuestionExctFileSerivce{

	private static final Logger logger = LoggerFactory.getLogger(AnnualMeetingQuestionExctFileServiceImpl.class);
	
	@Override
	public Integer savaDataForExct(InputStream exctFileStream) throws Exception {
		//创建对Excel工作簿文件的引用
		Workbook workbook = null;
		try {
			try{
				workbook = new HSSFWorkbook(exctFileStream);
			}catch(Exception e){
				//上传的文件可能是offic2007以上的版本,记录日志
				logger.info("可能上传offic2007以上的版本");
				workbook = new XSSFWorkbook();
			}
			List<AnnualMeetingGameQuestion> analyzingExctData = analyzingExctData(workbook);
			
		} catch (Exception e) {
			logger.error("文件加载异常",e.toString());
			return 0;
		} finally {
			if(workbook != null){
				workbook.close();
			}
		}
		return 1;
	}
	
	private List<AnnualMeetingGameQuestion> analyzingExctData(Workbook workbook) throws Exception{
		
		return null;
	}

}
