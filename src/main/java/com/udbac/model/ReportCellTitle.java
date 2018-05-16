package com.udbac.model;

/**
 * 报告单元格属性
 */
public class ReportCellTitle {

	private String title;
	private int row;
	private int column;
	private int[] range;

	public String getTitle() {
		return title;
	}

	public ReportCellTitle(String title, int row, int column, int[] range) {
		super();
		this.title = title;
		this.row = row;
		this.column = column;
		this.range = range;
	}



	public void setTitle(String title) {
		this.title = title;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int[] getRange() {
		return range;
	}

	public void setRange(int[] range) {
		this.range = range;
	}
}
