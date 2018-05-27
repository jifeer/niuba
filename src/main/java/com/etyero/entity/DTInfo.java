package com.etyero.entity;

/**
 * DT分红时线实体类
 * 
 * @author lijialong
 */
public class DTInfo {
	private Integer id;
	private String dt_today;
	private String dt_yerstoday;
	private String dt_beforeYerstoday;
	private String dt_nowTime;

	public String getDt_nowTime() {
		return dt_nowTime;
	}

	public void setDt_nowTime(String dt_nowTime) {
		this.dt_nowTime = dt_nowTime;
	}

	private String create_time;
	private String update_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDt_today() {
		return dt_today;
	}

	public void setDt_today(String dt_today) {
		this.dt_today = dt_today;
	}

	public String getDt_yerstoday() {
		return dt_yerstoday;
	}

	public void setDt_yerstoday(String dt_yerstoday) {
		this.dt_yerstoday = dt_yerstoday;
	}

	public String getDt_beforeYerstoday() {
		return dt_beforeYerstoday;
	}

	public void setDt_beforeYerstoday(String dt_beforeYerstoday) {
		this.dt_beforeYerstoday = dt_beforeYerstoday;
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
