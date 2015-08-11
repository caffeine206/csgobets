package com.csgobets.vo;

import java.util.Date;

public class Match {
	private Date date;
	private String teamA;
	private String teamB;
	private double teamAodds;
	private double teamBodds;
	private int format;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTeamA() {
		return teamA;
	}
	public void setTeamA(String teamA) {
		this.teamA = teamA;
	}
	public String getTeamB() {
		return teamB;
	}
	public void setTeamB(String teamB) {
		this.teamB = teamB;
	}
	public double getTeamAodds() {
		return teamAodds;
	}
	public void setTeamAodds(double teamAodds) {
		this.teamAodds = teamAodds;
	}
	public double getTeamBodds() {
		return teamBodds;
	}
	public void setTeamBodds(double teamBodds) {
		this.teamBodds = teamBodds;
	}
	public int getFormat() {
		return format;
	}
	public void setFormat(int format) {
		this.format = format;
	}
	
	public void switchTeams() {
		String temp = teamA;
		teamA = teamB;
		teamB = temp;
		double tempOdds = teamAodds;
		teamAodds = teamBodds;
		teamBodds = tempOdds;
	}
}
