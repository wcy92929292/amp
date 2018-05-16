package com.udbac.entity;

import java.util.Date;

import com.udbac.model.UserBean;

/****
 * Todo信息
 * 
 * @author lily
 *
 */
public class TodoInfoBean extends TbAmpBasicActivityInfo {

	private Integer todoCode;// Todo编号
	private String todoContent;// Todo内容
	private String todoType;// Todo类型（0固定活动任务，1点位任务，2普通备注,3问题备注）
	private String todoState;// Todo状态
	private String addTaskUid; // 任务填写人
	private String receptionTaskUid;// 任务接收人
	private String mailState;// 是否创建邮件
	private String imgPath;// 图片路径
	private String stateUpdate;// Todo状态更改日期
	private Integer ofState;// 所属当前活动状态或当前点位状态
	private String ofTaskType;// 所属任务类别
	private Integer nowState;// 当前状态
	private Integer lastState;// 上一状态
	private Integer nextState;// 下一状态
	private Integer roleId;// 角色编号
	private String mic;// 点位短代码

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getTodoCode() {
		return todoCode;
	}

	public void setTodoCode(Integer todoCode) {
		this.todoCode = todoCode;
	}

	public String getTodoContent() {
		return todoContent;
	}

	public void setTodoContent(String todoContent) {
		this.todoContent = todoContent;
	}

	public String getTodoType() {
		return todoType;
	}

	public void setTodoType(String todoType) {
		this.todoType = todoType;
	}

	public String getTodoState() {
		return todoState;
	}

	public void setTodoState(String todoState) {
		this.todoState = todoState;
	}

	public String getAddTaskUid() {
		return addTaskUid;
	}

	public void setAddTaskUid(String addTaskUid) {
		this.addTaskUid = addTaskUid;
	}

	public String getReceptionTaskUid() {
		return receptionTaskUid;
	}

	public void setReceptionTaskUid(String receptionTaskUid) {
		this.receptionTaskUid = receptionTaskUid;
	}

	public String getMailState() {
		return mailState;
	}

	public void setMailState(String mailState) {
		this.mailState = mailState;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getStateUpdate() {
		return stateUpdate;
	}

	public void setStateUpdate(String stateUpdate) {
		this.stateUpdate = stateUpdate;
	}

	public Integer getOfState() {
		return ofState;
	}

	public void setOfState(Integer ofState) {
		this.ofState = ofState;
	}

	public String getOfTaskType() {
		return ofTaskType;
	}

	public void setOfTaskType(String ofTaskType) {
		this.ofTaskType = ofTaskType;
	}

	public Integer getNowState() {
		return nowState;
	}

	public void setNowState(Integer nowState) {
		this.nowState = nowState;
	}

	public Integer getNextState() {
		return nextState;
	}

	public void setNextState(Integer nextState) {
		this.nextState = nextState;
	}

	public Integer getLastState() {
		return lastState;
	}

	public void setLastState(Integer lastState) {
		this.lastState = lastState;
	}

	@Override
	public String toString() {
		return "TodoInfoBean [todoCode=" + todoCode + ", todoContent=" + todoContent + ", todoType=" + todoType
				+ ", todoState=" + todoState + ", addTaskUid=" + addTaskUid + ", receptionTaskUid=" + receptionTaskUid
				+ ", mailState=" + mailState + ", imgPath=" + imgPath + ", stateUpdate=" + stateUpdate + ", ofState="
				+ ofState + ", ofTaskType=" + ofTaskType + ", nowState=" + nowState + ", lastState=" + lastState
				+ ", nextState=" + nextState + ", roleId=" + roleId + ", mic=" + mic + "]";
	}

}
