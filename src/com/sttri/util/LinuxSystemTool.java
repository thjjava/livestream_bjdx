package com.sttri.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 
 * 
 * 取得linux系统下的cpu、内存信息
 * 
 */
public final class LinuxSystemTool {
	/**
	 * 获取内存信息 
	 * @return map;
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Map<String, String> getMemInfo() throws IOException, InterruptedException {
		Map<String, String> map = new HashMap<String, String>();
		File file = new File("/proc/meminfo");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String str = null;
		StringTokenizer token = null;
		while ((str = br.readLine()) != null) {
			token = new StringTokenizer(str);
			if (!token.hasMoreTokens())
				continue;

			str = token.nextToken();
			if (!token.hasMoreTokens())
				continue;

			if (str.equalsIgnoreCase("MemTotal:"))
				map.put("MemTotal", token.nextToken());
			else if (str.equalsIgnoreCase("MemFree:"))
				map.put("MemFree", token.nextToken());
			else if (str.equalsIgnoreCase("Cached:"))
				map.put("Cached", token.nextToken());
			else if (str.equalsIgnoreCase("SwapTotal:"))
				map.put("SwapTotal", token.nextToken());
			else if (str.equalsIgnoreCase("SwapFree:"))
				map.put("SwapFree", token.nextToken());
		} 
		br.close();
		return map;
	}

	/**
	 * get memory by used info
	 * 获取CPU使用率
	 * @return float efficiency
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("resource")
	public static float getCpuInfo() throws IOException, InterruptedException {
		File file = new File("/proc/stat");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		StringTokenizer token = new StringTokenizer(br.readLine());
		token.nextToken();
//		int user1 = Integer.parseInt(token.nextToken());
//		int nice1 = Integer.parseInt(token.nextToken());
//		int sys1 = Integer.parseInt(token.nextToken());
//		int idle1 = Integer.parseInt(token.nextToken());
		Float user1 = Float.parseFloat(token.nextToken());
		Float nice1 = Float.parseFloat(token.nextToken());
		Float sys1 = Float.parseFloat(token.nextToken());
		Float idle1 = Float.parseFloat(token.nextToken());

		Thread.sleep(1000);

		br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		token = new StringTokenizer(br.readLine());
		token.nextToken();
//		int user2 = Integer.parseInt(token.nextToken());
//		int nice2 = Integer.parseInt(token.nextToken());
//		int sys2 = Integer.parseInt(token.nextToken());
//		int idle2 = Integer.parseInt(token.nextToken());
		Float user2 = Float.parseFloat(token.nextToken());
		Float nice2 = Float.parseFloat(token.nextToken());
		Float sys2 = Float.parseFloat(token.nextToken());
		Float idle2 = Float.parseFloat(token.nextToken());
        br.close();
        float cpuUsage = 0;
        if ((user2 + nice2 + sys2 + idle2) > (user1 + nice1 + sys1 + idle1)) {
        	
        	 float result = ((user2 + sys2 + nice2) - (user1 + sys1 + nice1))
     				/ ((user2 + nice2 + sys2 + idle2) - (user1 + nice1 + sys1 + idle1));
        	 System.out.println("**result**:"+result);
             BigDecimal decimal = new BigDecimal(result);
             cpuUsage = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); //四舍五入，保留2位小数 
		}
        return cpuUsage;
	}
}
