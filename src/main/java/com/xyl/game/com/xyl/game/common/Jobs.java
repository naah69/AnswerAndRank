package com.xyl.game.com.xyl.game.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xyl.game.contrller.AnnualMeetingQuestionFileLoadContraller;

/**
 * Jobs
 * 定时输出excel 任务
 * @author Naah
 * @date 2018-01-21
 */
@Component
public class Jobs {
    public final static long FIRST_DELAY =  60 * 1000;
    public final static long ONE_MINUTE =  60 * 1000;
    
    public final static String USERDATA ="userData";
    
    private static final Logger logger = LoggerFactory.getLogger(Jobs.class);

    @Scheduled(initialDelay = FIRST_DELAY,fixedDelay=ONE_MINUTE)
    public void fixedDelayJob(){
    	Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			//生成一各sheet
			workbook.write(new FileOutputStream(new File("")));
			Sheet createSheet = workbook.createSheet(USERDATA);
			
			CellStyle createCellStyle = workbook.createCellStyle();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(workbook != null){
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }


}
