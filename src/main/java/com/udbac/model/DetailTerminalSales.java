package com.udbac.model;

import java.util.Date;

/***
 * 
 * @author ningyexin
 *  在售商品销售明细
 *
 */
public class DetailTerminalSales {
	
	/**
	 *   create_date timestamp without time zone, --统计时间
  goods_code character varying(20), -- 商品编码
  goods_name character varying(20),-- 商品名称
  product_name character varying(20),--产品名称
  goods_type character varying(20),--商品类目
  goods_owner character varying(20),--商户
  sales_total character varying(20),--商品销售总数
  sales_user_total character varying(20),--商品销售用户总数
  sales_user_avg character varying(20),--人均销售商品总数
  sales_amount character varying(20), 商品销售总金额
	 */
	
	private String goods_code;
	private Date create_date;
	private String goods_name;
	private String product_name;
	private String goods_type;
	private String goods_owner;
	private String sales_total;
	private String sales_user_total;
	private String sales_user_avg;
	private String sales_amount;
	
	
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getGoods_type() {
		return goods_type;
	}
	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}
	public String getGoods_owner() {
		return goods_owner;
	}
	public void setGoods_owner(String goods_owner) {
		this.goods_owner = goods_owner;
	}
	public String getSales_total() {
		return sales_total;
	}
	public void setSales_total(String sales_total) {
		this.sales_total = sales_total;
	}
	public String getSales_user_total() {
		return sales_user_total;
	}
	public void setSales_user_total(String sales_user_total) {
		this.sales_user_total = sales_user_total;
	}
	public String getSales_user_avg() {
		return sales_user_avg;
	}
	public void setSales_user_avg(String sales_user_avg) {
		this.sales_user_avg = sales_user_avg;
	}
	public String getSales_amount() {
		return sales_amount;
	}
	public void setSales_amount(String sales_amount) {
		this.sales_amount = sales_amount;
	}
	
	

}
