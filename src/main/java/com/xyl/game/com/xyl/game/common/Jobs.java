package com.xyl.game.com.xyl.game.common;

import com.xyl.game.po.User;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

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

    @Scheduled(fixedDelay=ONE_MINUTE)
    public void fixedDelayJob(){
    	Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			//生成一各sheet

			Sheet createSheet = workbook.createSheet(USERDATA);

			//生成表格标题
			Row createRow = createSheet.createRow(0);
			Class clazz = User.class;

			Field[] declaredFields = clazz.getDeclaredFields();

			for (int i = 0; i < declaredFields.length; i++) {
				Cell createCell = createRow.createCell(i);
				createCell.setCellValue(declaredFields[i].getName());
			}
			//迭代插入所有user数据
			Map<String, User> usersMap = HeapVariable.usersMap;
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
			workbook.write(new FileOutputStream(new File("E:\\")));
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
    }


}
