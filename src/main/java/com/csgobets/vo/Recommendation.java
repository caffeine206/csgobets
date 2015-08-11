package com.csgobets.vo;

import java.util.Date;

public class Recommendation {
	private Date date;
	private String teamA;
	private String teamB;
	private double egbOddsDifference;
	private int format;
	private double betAmount;
	private boolean shouldBet;
	private double percentageToBet;
	private String csglOdds;
	private String egbOdds;
	
	public boolean isShouldBet() {
		return shouldBet;
	}
	public void setShouldBet(boolean shouldBet) {
		this.shouldBet = shouldBet;
	}
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
	public double getEgbOddsDifference() {
		return egbOddsDifference;
	}
	public void setEgbOddsDifference(double egbOddsDifference) {
		this.egbOddsDifference = egbOddsDifference;
	}
	public int getFormat() {
		return format;
	}
	public void setFormat(int format) {
		this.format = format;
	}
	public double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(double betAmount) {
		this.betAmount = betAmount;
	}
	public double getPercentageToBet() {
		return percentageToBet;
	}
	public void setPercentageToBet(double percentageToBet) {
		this.percentageToBet = percentageToBet;
	}
	public String getCsglOdds() {
		return csglOdds;
	}
	public void setCsglOdds(String csglOdds) {
		this.csglOdds = csglOdds;
	}
	public String getEgbOdds() {
		return egbOdds;
	}
	public void setEgbOdds(String egbOdds) {
		this.egbOdds = egbOdds;
	}
	
}
