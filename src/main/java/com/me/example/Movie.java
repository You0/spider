package com.me.example;

import java.util.Date;

public class Movie {
	private String title = "";
	private String type = "";
	private int rating = 0;
	private String actor = "";
	private String cover = "";
	private String imgs = "";
	private String video = "";
	private String fh = "";
	private Date date = new Date();
	private String company = "";
	private String series = "";
	private String time = "";
	private String director = "";

	public void setDirector(String director) {
		this.director = director;
	}

	public String getDirector() {
		return director;
	}



	@Override
	public String toString() {
		return "Movie [title=" + title + ", type=" + type + ", rating=" + rating + ", actor=" + actor + ", cover="
				+ cover + ", imgs=" + imgs + ", video=" + video + ", fh=" + fh + ", date=" + date + ", company="
				+ company + ", series=" + series + ", time=" + time + ", director=" + director + "]";
	}

	public String getTitle() {
		return title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getFh() {
		return fh;
	}

	public void setFh(String fh) {
		this.fh = fh;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
