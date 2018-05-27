package com.etyero.entity;

/**
 * 乐币分红信息实体类
 * 
 * @author lijialong
 */
public class LCEXInfo {
	private Integer id;
	private String dividend_info;
	private String create_time;
	private String update_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDividend_info() {
		return dividend_info;
	}

	public void setDividend_info(String dividend_info) {
		this.dividend_info = dividend_info;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

}
