package com.xyl.game.utils;

import com.xyl.game.po.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	public static void savaUserData(String path) {
		savaUserData(new File(path));
	}

	public static void savaUserDataForRoot(String path) {
		savaUserData(new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()+"/"+path));
	}

	public static void printdata(OutputStream outputStream){

	}

	public static void savaUserData(File file) {
		int count = 0;
		Map<String, User> usersMap = HeapVariable.usersMap;
		Collection<User> values = usersMap.values();
		Workbook workbook = null;

		// 文件没有改变
		if (HeapVariable.MD5DataChange.equals(MD5Util.md5(values.toString()))) {
			return;
		} else {
			String md5 = MD5Util.md5(values.toString());
			HeapVariable.MD5DataChange = md5;
			logger.info("数据修改，执行写入操作！");
		}
		try {
			workbook = new XSSFWorkbook();

			if (!file.exists()) {
				workbook.createSheet();
			} else {
				workbook = new XSSFWorkbook(new FileInputStream(file));
			}
			// 读写原来的数据
			Sheet sheetAt = workbook.getSheetAt(0);
			count = sheetAt.getLastRowNum() + 1;
			// 解析数据
			analyticData(count, sheetAt, values);

			workbook.write(new FileOutputStream(file));
			logger.info("写入操作完成");
		} catch (Exception e) {
			logger.error(Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把数据写到新增数据写到excel表中
	 *
	 * @param count
	 * @return
	 * @throws Exception
	 */
	private static void analyticData(int count, Sheet createSheet, Collection<User> values) throws Exception {
		// 记录时间
		Row time = createSheet.createRow(count + 1);
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String format = dateFormat.format(date);
		CellRangeAddress cra = new CellRangeAddress(count + 1, count + 1, 0, 7);
		Cell createCell2 = time.createCell(0);
		createCell2.setCellValue(format);
		createSheet.addMergedRegion(cra);

		// 生成表格标题
		Row createRow = createSheet.createRow(count + 2);
		Class clazz = User.class;

		Field[] declaredFields = clazz.getDeclaredFields();

		for (int i = 0; i < declaredFields.length; i++) {
			Cell createCell = createRow.createCell(i);
			createCell.setCellValue(declaredFields[i].getName());
		}

		// 迭代插入所有user数据
		writeDataExecl(createSheet, createRow, clazz, declaredFields, count + 3, values);
	}

	/**
	 * 遍历数据
	 *
	 * @param createSheet
	 * @param createRow
	 * @param clazz
	 * @param declaredFields
	 * @param countNum
	 * @param values
	 * @throws NoSuchMethodException
	 */
	private static void writeDataExecl(Sheet createSheet, Row createRow, Class clazz, Field[] declaredFields,
			int countNum, Collection<User> values) throws Exception {
		System.out.println(values);
		Iterator<User> iterator = values.iterator();
		int count = countNum;
		while (iterator.hasNext()) {
			User user = iterator.next();
			short lastCellNum = createRow.getLastCellNum();
			Row createRow2 = createSheet.createRow(count++);
			// 遍历插入标题对应的数据
			for (int i = 0; i < lastCellNum; i++) {
				Cell cell = createRow.getCell(i);
				for (int j = 0; j < declaredFields.length; j++) {
					if (declaredFields[j].getName().equals(cell.toString())) {
						Cell createCell = createRow2.createCell(i);
						// 获得user对象中的值
						Method method = clazz.getMethod("get" + StringUtil.initialsUpper(declaredFields[j].getName()));
						Object invoke = method.invoke(user);
						if (invoke != null) {
							createCell.setCellValue(invoke.toString());
						}
						break;
					}
				}
			}
		}
	}


}
