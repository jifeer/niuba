package com.etyero.entity;

/**
 * Cht分红实体类
 * 
 * @author lijialong
 */
public class Cht {
	private Integer id;
	private String yesterday_info;
	private String dividend_histroy_BTC;
	private String dividend_histroy_ETH;
	private String dividend_histroy_count;
	private String dividend_histroy_date;
	private String update_time;

	public String getIs_update() {
		return update_time;
	}

	public void setIs_update(String update_time) {
		this.update_time = update_time;
	}

	private String create_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getYesterday_info() {
		return yesterday_info;
	}

	public void setYesterday_info(String yesterday_info) {
		this.yesterday_info = yesterday_info;
	}

	public String getDividend_histroy_BTC() {
		return dividend_histroy_BTC;
	}

	public void setDividend_histroy_BTC(String dividend_histroy_BTC) {
		this.dividend_histroy_BTC = dividend_histroy_BTC;
	}

	public String getDividend_histroy_ETH() {
		return dividend_histroy_ETH;
	}

	public void setDividend_histroy_ETH(String dividend_histroy_ETH) {
		this.dividend_histroy_ETH = dividend_histroy_ETH;
	}

	public String getDividend_histroy_count() {
		return dividend_histroy_count;
	}

	public void setDividend_histroy_count(String dividend_histroy_count) {
		this.dividend_histroy_count = dividend_histroy_count;
	}

	public String getDividend_histroy_date() {
		return dividend_histroy_date;
	}

	public void setDividend_histroy_date(String dividend_histroy_date) {
		this.dividend_histroy_date = dividend_histroy_date;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
