package com.xyl.game.contrller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xyl.game.Service.AnnualMeetingQuestionExctFileSerivce;
import com.xyl.game.po.User;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.StringUtil;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;

/**
 * 接收Exct文件
 * @author dazhi
 *
 */
@Controller
public class AnnualMeetingQuestionFileLoadContraller {
	
	private static final Logger logger = LoggerFactory.getLogger(AnnualMeetingQuestionFileLoadContraller.class);
	
	public final static long FIRST_DELAY =  60 * 1000;
    public final static long ONE_MINUTE =  60 * 1000;

	@Autowired 
	private AnnualMeetingQuestionExctFileSerivce exctFileSerivce;
	
	/**
	 * 接收年会游戏上传的exct表格文件
	 */
	@RequestMapping("/loadExctFile")
	@ResponseBody
	public AnnualMeetingGameQuestionVo uploadExct(@RequestParam(value="exctFile")MultipartFile multipartFile,HttpSession session){
		AnnualMeetingGameQuestionVo savaDataForExct = null;
		try {
			savaDataForExct = exctFileSerivce.savaDataForExct(multipartFile.getInputStream());
		} catch (IOException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		session.setAttribute(session.getId(), savaDataForExct);
		return savaDataForExct;
	}
	
	/**
	 * 修改缓存数据
	 * @param session
	 * @param id
	 * @param fieldValue
	 * @param fieldName
	 * @return
	 */
	@RequestMapping("/updataQuestionData")
	@ResponseBody
	public String updataQuestionData(HttpSession session,Integer id,String fieldValue,String fieldName){
		AnnualMeetingGameQuestionVo attribute = (AnnualMeetingGameQuestionVo)session.getAttribute(session.getId());
		try {
			if(exctFileSerivce.updataData(attribute, id, fieldValue, fieldName)){
				logger.info("年会问题缓存数据修改成功");
			}else{
				logger.info("年会问题缓存数据未修改");
			}
		} catch (Exception e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		return "OK";
	}
	
	@RequestMapping("/")
	@ResponseBody
	public String a(){
		File file = new File("E:\\userData.xlsx");
		if(file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			//生成一各sheet
			
			Sheet createSheet = workbook.createSheet("userData");
			//表格样式
			CellStyle createCellStyle = workbook.createCellStyle();
			
			//生成表格标题
			Row createRow = createSheet.createRow(0);
			createRow.setRowStyle(createCellStyle);
			Class clazz = User.class;
			
			Field[] declaredFields = clazz.getDeclaredFields();
			
			for (int i = 0; i < declaredFields.length; i++) {
				Cell createCell = createRow.createCell(i);
				createCell.setCellValue(declaredFields[i].getName());
			}
			//迭代插入所有user数据
			Map<String, User> usersMap = HeapVariable.usersMap;
			
			if(usersMap != null){
				Collection<User> values = usersMap.values();
				Iterator<User> iterator = values.iterator();
				int count = 1;
				while (iterator.hasNext()) {
					User user = iterator.next();
					short lastCellNum = createRow.getLastCellNum();
					Row createRow2 = createSheet.createRow(count++);
					//遍历插入标题对应的数据
					for (int i = 0; i < lastCellNum; i++) {
						Cell cell = createRow.getCell(i);
						for (int j = 0; j < declaredFields.length; j++) {
							if(declaredFields[j].getName().equals(cell.toString())){
								Cell createCell = createRow2.createCell(i);
								//获得user对象中的值
								Method method = clazz.getMethod("get"+StringUtil.initialsUpper(declaredFields[j].getName()));
								Object invoke = method.invoke(user);
								createCell.setCellValue(invoke.toString());
								break;
							}
						}
					}
				}
			}
			workbook.write(new FileOutputStream(file));
		} catch (Exception e) {
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
		return "ok";
	}
	
	
	/**
	 * 查询数据库以及存储的数据
	 */
	@RequestMapping("/loadExctFile")
	@ResponseBody
	public AnnualMeetingGameQuestionVo getAllGameQuestion(){
		return exctFileSerivce.getAllGameQuestion();
		//return null;
	}
}
