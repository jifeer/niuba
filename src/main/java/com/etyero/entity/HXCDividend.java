package com.etyero.entity;

/**
 * HXC分红实体类
 * 
 * @author lijialong
 */
public class HXCDividend {
	private Integer id;
	private String hxc_dividend;
	private String create_time;
	private String update_time;

	public String getIs_update() {
		return update_time;
	}

	public void setIs_update(String update_time) {
		this.update_time = update_time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHxc_dividend() {
		return hxc_dividend;
	}

	public void setHxc_dividend(String hxc_dividend) {
		this.hxc_dividend = hxc_dividend;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
