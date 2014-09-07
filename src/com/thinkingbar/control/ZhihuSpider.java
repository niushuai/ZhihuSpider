package com.thinkingbar.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thinkingbar.model.FileOperationEntity;
import com.thinkingbar.model.ZhihuEntity;

public class ZhihuSpider {

	/**
	 * 从指定 URL 得到网页的源码
	 */
	public static String getSourceCode(String url) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			connection.connect();

			//使用 UTF-8 进行页面编码，防止中文乱码
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			System.err.println("HTTP Get Error...");
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e2) {
				System.err.println("close BufferedReader failed...");
				e2.printStackTrace();
			}
		}
		return result.toString();
	}

	/**
	 * 从知乎编辑推荐页面抓取问题和对应答案
	 */
	public static ArrayList<ZhihuEntity> GetRecommendQuestions(String content) {
		ArrayList<ZhihuEntity> results = new ArrayList<ZhihuEntity>();
		
		//匹配问题链接
		Pattern urlPattern = Pattern.compile("<h2>.+?question_link.+?href=\"(.+?)\".+?</h2>");
		Matcher matcher = urlPattern.matcher(content);
		
		while (matcher.find()) {
			ZhihuEntity zhihuTemp = new ZhihuEntity(matcher.group(1));
			results.add(zhihuTemp);
		}
		return results;
	}
	
	public static void main(String[] args) {
		
		String url = "http://www.zhihu.com/explore/recommendations";
		String content = ZhihuSpider.getSourceCode(url);

		ArrayList<ZhihuEntity> myZhihu = ZhihuSpider.GetRecommendQuestions(content);

		for (ZhihuEntity zhihu : myZhihu) {
			FileOperationEntity.writeIntoFile(zhihu.writeString(), "/Users/niushuai/work/Zhihu.txt", true);
		}
	}
}