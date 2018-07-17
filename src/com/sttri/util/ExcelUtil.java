package com.sttri.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sttri.pojo.TblDev;

import jxl.Cell;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


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
			int rows=sheet.getRows();//行数
			int cols=sheet.getColumns();//列数
			for(int i=1;i<rows;i++) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				for (int j = 0; j < cols; j++) {
					Cell cell=sheet.getCell(j,i);
					String celli=cell.getContents().trim();
					map.put("cell"+(i-1), celli);
				}
				list.add(map);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	/**
     * 导出Excel
     * @param list：结果集合
     * @param filePath：指定的路径名
     * @param out：输出流对象 通过response.getOutputStream()传入
     * @param mapFields：导出字段 key:对应实体类字段    value：对应导出表中显示的中文名
     * @param sheetName：工作表名称
     */
	@SuppressWarnings("rawtypes")
	public static void createExcel(List list,String filePath,OutputStream out,Map<String, String> mapFields,String sheetName){
        sheetName = sheetName!=null && !sheetName.equals("")?sheetName:"sheet1";
        WritableWorkbook wook = null;//可写的工作薄对象
        Object objClass = null;
        try {
            if(filePath!=null && !filePath.equals("")){
                wook = Workbook.createWorkbook(new File(filePath));//指定导出的目录和文件名 如：D:\\test.xls
            }else{
                wook = Workbook.createWorkbook(out);//jsp页面导出用
            }
            //设置头部字体格式
            WritableFont font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD,
                    false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
            //应用字体
            WritableCellFormat wcfh = new WritableCellFormat(font);
            //设置其他样式
            wcfh.setAlignment(Alignment.CENTRE);//水平对齐
            wcfh.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
            wcfh.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
            wcfh.setBackground(Colour.YELLOW);//背景色
            wcfh.setWrap(false);//自动换行
                                                                            
            //设置内容日期格式
            DateFormat df = new DateFormat("yyyy-MM-dd HH:mm:ss");
            //应用日期格式
            WritableCellFormat wcfc = new WritableCellFormat(df);
                                                                            
            wcfc.setAlignment(Alignment.CENTRE);
            wcfc.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
            wcfc.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
            wcfc.setWrap(true);//不自动换行
                                                                            
            //创建工作表
            WritableSheet sheet = wook.createSheet(sheetName, 0);
            SheetSettings setting = sheet.getSettings();
            setting.setVerticalFreeze(1);//冻结窗口头部
                                                                            
            int columnIndex = 0;  //列索引
            List<String> methodNameList = new ArrayList<String>();
            if(mapFields!=null){
                String key  = "";
                Map<String,Method> getMap = null;
                Method method = null;
               /* CellView cv = new CellView();  
                cv.setAutosize(true);//自动宽度  
                cv.setSize(20); //最小宽度 
*/                //开始导出表格头部
                for (Iterator<String> i = mapFields.keySet().iterator();i.hasNext();) {
                    key = i.next();
                    //根据内容自动设置列宽
                    sheet.setColumnView(columnIndex,20);
                    // 应用wcfh样式创建单元格
                    sheet.addCell(new Label(columnIndex, 0, mapFields.get(key), wcfh));
                    //记录字段的顺序，以便于导出的内容与字段不出现偏移
                    methodNameList.add(key);
                    columnIndex++;
                }
                if(list!=null && list.size()>0){
                    //导出表格内容
                    for (int i = 0,len = list.size(); i < len; i++) {
                        objClass = list.get(i);
                        getMap = getAllMethod(objClass);//获得对象所有的get方法
                        //按保存的字段顺序导出内容
                        for (int j = 0; j < methodNameList.size(); j++) {
                            //根据key获取对应方法
                            method = getMap.get("GET"+methodNameList.get(j).toString().toUpperCase());
                            if(method!=null){
                                //从对应的get方法得到返回值
                                String value = method.invoke(objClass, null).toString();
                                //应用wcfc样式创建单元格
                                sheet.addCell(new Label(j, i+1, value, wcfc));
                              }else{
                                   //如果没有对应的get方法，则默认将内容设为""
                                   sheet.addCell(new Label(j, i+1, "", wcfc));
                              }
                    
                        }
                    }
                }
                wook.write();
                System.out.println("导出Excel成功！");
            }else{
                throw new Exception("传入参数不合法");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(wook!=null){
                    wook.close();
                }
                if(out!=null){
                    out.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
	
	/**
     * 获取类的所有get方法
     * @param cls
     * @return
     */
    public static HashMap<String,Method> getAllMethod(Object obj) throws Exception{
        HashMap<String,Method> map = new HashMap<String,Method>();
        Method[] methods = obj.getClass().getMethods();//得到所有方法
        String methodName = "";
        for (int i = 0; i < methods.length; i++) {
            methodName = methods[i].getName().toUpperCase();//方法名
            if(methodName.startsWith("GET")){
                map.put(methodName, methods[i]);//添加get方法至map中
            }
        }
        return map;
    }
	
	/**
     * 根据指定路径导出Excel
     * @param list
     * @param filePath
     * @param mapFields
     * @param sheetName
     */
	@SuppressWarnings("rawtypes")
	public static void ImportExcel(List list,String filePath,Map<String, String> mapFields,String sheetName){
        createExcel(list,filePath,null,mapFields,sheetName);
    }
                                                                    
    /**
     * 从Jsp页面导出Excel
     * @param list
     * @param filePath
     * @param out
     * @param mapFields
     * @param sheetName
     */
	@SuppressWarnings("rawtypes")
	public static void ImportExcel(List list,OutputStream out,Map<String, String> mapFields,String sheetName){
        createExcel(list,null,out,mapFields,sheetName);
    }
	
	public static void main(String[] args) {
		System.out.println(readExcel("C:/Users/thj/Desktop/dev.xls").get(0));
	}
}
