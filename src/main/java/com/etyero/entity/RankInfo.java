package com.etyero.entity;

/**
 * 交易排行榜实体类
 * 
 * @author lijialong
 */
public class RankInfo {
	private Integer id;
	private String rank_info;
	private String create_time;
	private String update_time;

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRank_info() {
		return rank_info;
	}

	public void setRank_info(String rank_info) {
		this.rank_info = rank_info;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
