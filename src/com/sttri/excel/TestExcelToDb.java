package com.sttri.excel;

import java.io.File;
import java.sql.ResultSet;
import java.util.Date;

import com.sttri.util.Util;

import jxl.Sheet;
import jxl.Workbook;


/**
 * @author Javen
 * @Email zyw205@gmail.com
 * 
 */
public class TestExcelToDb {
    public static void main(String[] args) {
    	/*
        //得到表格中所有的数据
        List<StuEntity> listExcel=StuService.getAllByExcel("d://book.xls");
        //得到数据库表中所有的数据
        List<StuEntity> listDb=StuService.getAllByDb();
        
        DBhepler db=new DBhepler();
        
        for (StuEntity stuEntity : listExcel) {
            int id=stuEntity.getId();
            if (!StuService.isExist(id)) {
                //不存在就添加
                String sql="insert into stu (name,sex,num) values(?,?,?)";
                String[] str=new String[]{stuEntity.getName(),stuEntity.getSex(),stuEntity.getNum()+""};
                db.AddU(sql, str);
            }else {
                //存在就更新
                String sql="update stu set name=?,sex=?,num=? where id=?";
                String[] str=new String[]{stuEntity.getName(),stuEntity.getSex(),stuEntity.getNum()+"",id+""};
                db.AddU(sql, str);
            }
        }
        */
    	
    	DBhepler db=new DBhepler();
    	
    	try {
            Workbook rwb=Workbook.getWorkbook(new File("G://sz_account.xls"));
            Sheet rs=rwb.getSheet("Sheet1");//或者rwb.getSheet(0)
            int clos=rs.getColumns();//得到所有的列
            int rows=rs.getRows();//得到所有的行
            
            System.out.println(clos+" rows:"+rows);
            String devNo = "", sql = "", addTime = Util.dateToStr(new Date());
            String[] param = null;
            ResultSet rset = null;
            for (int i = 1; i < rows; i++) {
            	devNo = rs.getCell(0, i).getContents();
            	sql = "select * from tbl_dev where devNo=?";
            	param = new String[]{devNo};
            	rset = db.Search(sql, param);
            	if(rset.next())
            		continue;
            	sql = "insert into tbl_dev (id,devName,devNo,devKey,comId,onlines,isable,addTime) values(?,?,?,?,?,?,?,?)";
            	param = new String[]{Util.getUUID(6),devNo,devNo,"MTIzNDU2","1","1","0",addTime};
            	System.out.println(db.AddU(sql, param));
            	
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}