package com.sttri.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sttri.pojo.TblDev;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


/**
 *读取Excel数据并保存到List中
 * @version 0.1
 */
public class ExcelUtil {
	/**
	 * 获取Excel表中设备集合
	 * @param path
	 * @return
	 */
	public static List<TblDev> getByExcel(String path){
		List<TblDev> list = new ArrayList<TblDev>();
		InputStream stream;
		try {
			stream = new FileInputStream(new File(path));
			Workbook wb=Workbook.getWorkbook(stream);
			Sheet sheet=wb.getSheet(0);
			int count=sheet.getRows();
			for(int i=1;i<count;i++) {
				TblDev dev = new TblDev();
				Cell cell=sheet.getCell(0,i);
				String devName=cell.getContents().trim();
				dev.setDevName(devName);
				cell= sheet.getCell(1,i);
				String devNo=cell.getContents().trim();
				dev.setDevNo(devNo);
				if("".equals(devNo)){
					break;
				}
				cell=sheet.getCell(2,i);
				String devKey=cell.getContents().trim();
				dev.setDevKey(devKey);
				list.add(dev);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	 
	public static List<Map<Object, Object>> readExcel(String path){
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		InputStream stream;
		try {
			stream = new FileInputStream(new File(path));
			Workbook wb=Workbook.getWorkbook(stream);
			Sheet sheet=wb.getSheet(0);
			int rows=sheet.getRows();
			int cols=sheet.getColumns();
			for(int i=1;i<rows;i++) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				for (int j = 0; j < cols; j++) {
					Cell cell=sheet.getCell(j,i);
					String cellj=cell.getContents().trim();
					map.put("cell"+j, cellj);
				}
				list.add(map);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		System.out.println(readExcel("C:/Users/thj/Desktop/dev.xls").get(0));
	}
}
