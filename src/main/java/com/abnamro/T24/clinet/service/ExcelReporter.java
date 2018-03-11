package com.abnamro.T24.clinet.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;

/**
 * This class is provide the export the records to excel
 * 
 * @author Umesh
 * 
 */

public class ExcelReporter {

	private static final String SHEET_NAME_PREFIX = "Sheet";
	private static final int MAX_NUMBER_OF_ROW_PER_SHEET = 60000;

	private List<String> fieldNames = null;
	private Workbook workbook = null;

	/**
	 * This method is used to setup the column name using the property of the class
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	private boolean setupFieldsForClass(Object data) throws Exception {

		fieldNames = new ArrayList<String>();
		if (data instanceof JSONObject) {
			JSONObject json = (JSONObject) data;
			Iterator<String> keys = json.keys();
			while (keys.hasNext()) {
				String obj = (String) keys.next();
				fieldNames.add(obj);

			}
		} else {
			Class<? extends Object> clas = data.getClass();
			Field[] fields = clas.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				fieldNames.add((fields[i].getName()));
			}

		}

		return true;
	}

	/**
	 * this method is used to write the records to excel and return the workbook
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public <T> Workbook writeRecordsToExcel(List<T> data) throws Exception {
		workbook = new HSSFWorkbook();

		setupFieldsForClass(data.get(0));

		int totalNumberOfRecord = data.size();

		int partitionSize = (((totalNumberOfRecord + MAX_NUMBER_OF_ROW_PER_SHEET) - 1) / MAX_NUMBER_OF_ROW_PER_SHEET);

		for (int sheetNumber = 0; sheetNumber < partitionSize; sheetNumber++) {

			Sheet sheet = getWorkbook().createSheet(SHEET_NAME_PREFIX + (sheetNumber + 1));

			// Create a row and put some cells in it. Rows are 0 based.
			int rowCount = 0;
			int columnCount = 0;
			int dataFromIndex = (MAX_NUMBER_OF_ROW_PER_SHEET * sheetNumber);
			int numberOfRecordToProcess = dataFromIndex + MAX_NUMBER_OF_ROW_PER_SHEET;
			int dataToIndex = dataFromIndex
					+ (numberOfRecordToProcess < totalNumberOfRecord ? MAX_NUMBER_OF_ROW_PER_SHEET
							: (totalNumberOfRecord - dataFromIndex));

			Row row = sheet.createRow(rowCount++);
			for (String fieldName : fieldNames) {
				Cell cel = row.createCell(columnCount++);
				cel.setCellValue(fieldName);
			}

			Class<? extends Object> classz = data.get(0).getClass();
			for (T values : data.subList(dataFromIndex, dataToIndex)) {
				row = sheet.createRow(rowCount++);
				columnCount = 0;
				for (String fieldName : fieldNames) {

					Cell cel = row.createCell(columnCount);
					JSONObject jsonObject = null;
					Object value = null;

					if (values instanceof JSONObject) {
						jsonObject = (JSONObject) values;
						value = jsonObject.get(fieldName);

					}

					else {
						String methodName = getMethodNameFromClass(fieldName, classz);
						Method method = classz.getMethod(methodName);
						value = method.invoke(values, (Object[]) null);

					}

					if (value != null) {
						if (value instanceof String) {
							cel.setCellValue((String) value);
						} else if (value instanceof Long) {
							cel.setCellValue((Long) value);
						} else if (value instanceof Integer) {
							cel.setCellValue((Integer) value);
						} else if (value instanceof Double) {
							cel.setCellValue((Double) value);
						} else if (value instanceof List<?>) {
							cel.setCellValue(getListValue(value));
						}
					}
					columnCount++;

				}
			}
		}

		return getWorkbook();
	}

	private String getListValue(Object value) {
		return ((List<?>) value).isEmpty() ? "" : value.toString();
	}

	private Workbook getWorkbook() {
		return workbook;
	}

	private String getMethodNameFromClass(String fieldName, Class<? extends Object> classz) {

		Method[] methods = classz.getDeclaredMethods();
		String methodName = null;
		for (Method method : methods) {

			if (method.getName().equalsIgnoreCase("get" + fieldName)) {
				methodName = method.getName();

			}
		}

		return methodName;

	}
}
