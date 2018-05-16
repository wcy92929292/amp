package com.udbac.model;

import java.util.LinkedList;
import java.util.List;

import com.udbac.model.Mics;

/**
 * 相同媒体中的活动集合
 */
public class Activitys {
		//所有点位的活动名
		private String activityName = "";
		//点位集合
		private List<Mics> micsList = new LinkedList<>();
		
		public Activitys(String activityName){
			this.activityName = activityName;
		}

		public String getActivityName() {
			return activityName;
		}

		public List<Mics> getMicsList() {
			return micsList;
		}
		
}
