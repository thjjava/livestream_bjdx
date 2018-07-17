package com.sttri.util;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;




import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sttri.dao.CommonDao;
import com.sttri.pojo.SystemAlarm;

public class SysInit implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent event) {
		
	}

	public void contextInitialized(ServletContextEvent event) {
		String cRootPath = event.getServletContext().getRealPath("/");
		if(cRootPath!=null) {
			cRootPath = cRootPath.replaceAll("\\\\", "/");
		}else {
			cRootPath = "/";
		}
		if (!cRootPath.endsWith("/")) {
			cRootPath = cRootPath + "/";
		}
		Constant.ROOTPATH = cRootPath;
		
		
		/**
		 * 定时执行获取服务器内存和CPU使用率任务
		 * 服务启动后1分钟开始执行，每1小时执行一次
		 */
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("***进入获取服务器内存和CPU使用率线程***"+Util.dateToStr(new Date()));
				try {
					ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
					CommonDao dao = (CommonDao) ac.getBean("dao");
					Map<String, String> map = LinuxSystemTool.getMemInfo();
					String memTotal = map.getOrDefault("MemTotal", "0");
					String memFree = map.getOrDefault("MemFree", "0");
					String cached = map.getOrDefault("Cached", "0");
					int memUsed = Integer.parseInt(memTotal) - Integer.parseInt(memFree) - Integer.parseInt(cached);
					float cpuUsage = LinuxSystemTool.getCpuInfo();
					int alarmLevel = 0;
					if (cpuUsage > 0.8) {
						alarmLevel = 1;
						SystemAlarm alarm = new SystemAlarm();
						alarm.setId(Util.getUUID(6));
						alarm.setMemTotal(memTotal);
						alarm.setMemUsed(memUsed+"");
						alarm.setCpuUsage(String.valueOf(cpuUsage));
						alarm.setAlarmLevel(alarmLevel);
						alarm.setServer(null);
						alarm.setThreshold("0.80");
						alarm.setAddTime(Util.dateToStr(new Date()));
						dao.save(alarm);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 1000*60, 1000*60*60);
	}

}
