package com.thinkingbar.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thinkingbar.control.ZhihuSpider;

public class ZhihuEntity {
	public String question;
	public String questionDescription;
	public static final String zhiHuProblemURLPrefix = "http://www.zhihu.com/question/";
	public String zhihuUrl;
	public ArrayList<String> answers;

	public ZhihuEntity(String url) {
		question = "";
		questionDescription = "";
		zhihuUrl = "";
		answers = new ArrayList<String>();

		//获取问题的真实 URL
		getRealUrl(url);
		
		System.out.println("Spider: " + zhihuUrl + " ...");
		String content = ZhihuSpider.getSourceCode(zhihuUrl);
		
		Pattern pattern;
		Matcher matcher;
		
		// 匹配问题
		pattern = Pattern.compile("zh-question-title.+?<h2.+?>(.+?)</h2>");
		matcher = pattern.matcher(content);
		if (matcher.find()) {
			question = matcher.group(1);
		}
		// 匹配问题描述
		pattern = Pattern.compile("zh-question-detail.+?<div.+?>(.*?)</div>");
		matcher = pattern.matcher(content);
		if (matcher.find()) {
			questionDescription = matcher.group(1);
		}
		// 匹配问题答案
		pattern = Pattern.compile("/answer/content.+?<div.+?>(.*?)</div>");
		matcher = pattern.matcher(content);
		while (matcher.find()) {
			answers.add(matcher.group(1));
		}
	}

	/**
	 * 获取问题的真实 URL:
	 * 		推荐问题格式为	http://www.zhihu.com/question/123456/answer/123
	 * 		转化成        	http://www.zhihu.com/question/123456
	 */
	void getRealUrl(String url) {
		
		Pattern pattern = Pattern.compile("question/(.*?)/");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			zhihuUrl = zhiHuProblemURLPrefix + matcher.group(1);
		}
	}

	public String writeString() {
		StringBuilder result = new StringBuilder();
		result.append("问题：").append(question).append("\r\n");
		result.append("描述：").append(questionDescription).append("\r\n");
		result.append("链接：").append(zhihuUrl).append("\r\n\r\n");
		for (int i = 0; i < answers.size(); i++) {
			result.append("回答").append(i).append("：").append(answers.get(i)).append("\r\n\r\n\r\n");
		}
		result.append("\r\n\r\n\r\n\r\n\r\n\r\n");
		
		// 替换 THML 标签
		result = new StringBuilder(result.toString().replaceAll("<br>", "\r\n"));
		result = new StringBuilder(result.toString().replaceAll("<.*?>", ""));
		return result.toString();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("问题：").append(question).append("\n");
		result.append("描述：").append(questionDescription).append("\n");
		result.append("链接：").append(zhihuUrl).append("\n");
		result.append("回答：").append(answers.size() ).append("\n");
		return result.toString();
	}
}
