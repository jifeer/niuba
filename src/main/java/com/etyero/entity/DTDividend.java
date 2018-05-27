package com.etyero.entity;

import java.math.BigDecimal;

/**
 * DT历史分红实体类
 * 
 * @author lijialong
 */
public class DTDividend {
	private Integer id;
	private BigDecimal dt_dividend;
	private BigDecimal dt_onePeopleCost;
	private BigDecimal dt_doublePeopleCost;
	private String dt_dividend_date;
	private String create_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getDt_dividend() {
		return dt_dividend;
	}

	public void setDt_dividend(BigDecimal dt_dividend) {
		this.dt_dividend = dt_dividend;
	}

	public BigDecimal getDt_onePeopleCost() {
		return dt_onePeopleCost;
	}

	public void setDt_onePeopleCost(BigDecimal dt_onePeopleCost) {
		this.dt_onePeopleCost = dt_onePeopleCost;
	}

	public BigDecimal getDt_doublePeopleCost() {
		return dt_doublePeopleCost;
	}

	public void setDt_doublePeopleCost(BigDecimal dt_doublePeopleCost) {
		this.dt_doublePeopleCost = dt_doublePeopleCost;
	}

	public String getDt_dividend_date() {
		return dt_dividend_date;
	}

	public void setDt_dividend_date(String dt_dividend_date) {
		this.dt_dividend_date = dt_dividend_date;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
