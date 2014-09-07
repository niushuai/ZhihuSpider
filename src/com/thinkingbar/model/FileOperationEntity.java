package com.thinkingbar.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileOperationEntity {
	/**
	 * @param filePath
	 *            文件路径的字符串表示形式
	 * @param keyWord
	 *            查找包含某个关键字的信息：非null为带关键字查询；null为全文显示
	 * @return 当文件存在时，返回字符串；当文件不存在时，返回null
	 */
	public static String ReadFromFile(String filePath) {
		File file = new File(filePath);
		StringBuffer stringBuffer = null;
		
		if (file.exists()) {
			stringBuffer = new StringBuffer();
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;
			String temp = "";
			try {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				
				while ((temp = bufferedReader.readLine()) != null) {
					stringBuffer.append(temp + "\n");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fileReader != null)
						fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (bufferedReader != null)
						bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (stringBuffer == null) {
			return null;
		} else {
			return stringBuffer.toString();
		}

	}

	/**
	 * 将 content 写入文件。如果指定文件路径不存在，将新建文件后写入。
	 */
	public static boolean writeIntoFile(String content, String filePath, boolean isAppend) {
		boolean isSuccess = true;
		// 先过滤掉文件名
		int index = filePath.lastIndexOf("/");
		String dir = filePath.substring(0, index);
		// 创建除文件的路径
		File fileDir = new File(dir);
		fileDir.mkdirs();
		// 再创建路径下的文件
		File file = null;
		try {
			file = new File(filePath);
			file.createNewFile();
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		}
		// 写入文件
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file, isAppend);
			fileWriter.write(content);
			fileWriter.flush();
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return isSuccess;
	}

	/**
	 * 获取当前时间，可用于文件后缀。使用方法：
	 * 		String nowTime = getNowTime("yyyyMMddhhmm");
	 */
	public static String getNowTime(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date());
	}

	/**
	 * 创建文件。如果文件已存在则退出
	 */
	public static boolean createNewFile(String filePath) {
		boolean isSuccess = true;
		// 如有则将"\\"转为"/",没有则不产生任何变化
		String filePathTurn = filePath.replaceAll("\\\\", "/");
		// 先过滤掉文件名
		int index = filePathTurn.lastIndexOf("/");
		String dir = filePathTurn.substring(0, index);
		// 再创建文件夹
		File fileDir = new File(dir);
		isSuccess = fileDir.mkdirs();
		// 创建文件
		File file = new File(filePathTurn);
		try {
			isSuccess = file.createNewFile();
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		}
		return isSuccess;
	}
}