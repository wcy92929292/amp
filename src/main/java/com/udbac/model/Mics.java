package com.udbac.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Mic集合类
 */
public class Mics {
		
		//活动编号，投放日，分组ID 共同组合判断合并点位
		//活动编号
		private String activityCode = "";
		//分组ID
		private int groupId;
		//投放日
		private Date putDate = null;

		// 开始写入短代码的行
		int startWriteRow = 0;
		//点位集合
		List<MonthReportModel> micList = new LinkedList<>();

		public Mics(String activityCode, int groupId, Date putDate) {
			super();
			this.activityCode = activityCode;
			this.groupId = groupId;
			this.putDate = putDate;
		}

		public String getActivityCode() {
			return activityCode;
		}

		public int getGroupId() {
			return groupId;
		}

		public Date getPutDate() {
			return putDate;
		}

		public int getStartWriteRow() {
			return startWriteRow;
		}

		public void setStartWriteRow(int startWriteRow) {
			this.startWriteRow = startWriteRow;
		}

		public List<MonthReportModel> getMicList() {
			return micList;
		}
}
