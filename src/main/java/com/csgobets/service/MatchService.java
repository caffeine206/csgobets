package com.csgobets.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.csgobets.rest.CsglRestService;
import com.csgobets.rest.EgbRestService;
import com.csgobets.vo.Match;
import com.csgobets.vo.Recommendation;

@Component(value = "matchService")
public class MatchService {
	
	@Resource(name = "csglRestService")
	CsglRestService csglRestService;
	
	@Resource(name = "egbRestService")
	EgbRestService egbRestService;
	
	public List<Match> getMatchesFromCsgl() {
		return csglRestService.findMatches();
	}
	
	public List<Match> getMatchesFromEgb() {
		return egbRestService.findMatches();
	}

	public Set<String> getTeams() {
		return csglRestService.getTeams();
	}

	public Map<String, String> getTeamMappings() {
		BufferedReader br = null;
		Map<String, String> teamMappings = new HashMap<>();
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader("E:\\Workspace\\csgobets\\src\\main\\webapp\\resources\\mappings.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.isEmpty()) {
					String[] teams = sCurrentLine.split(",");
					teamMappings.put(teams[0].toLowerCase(), teams[1].toLowerCase());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return teamMappings;
	}

	public List<Recommendation> recommendBet() {
		List<Match> csglMatches = getMatchesFromCsgl();
		List<Match> egbMatches = getMatchesFromEgb();
		Set<String> masterTeamList = getTeams();
		Map<String, String> teamMappings = getTeamMappings();
		List<Recommendation> recommendations = new ArrayList<>();
		
		for (Match egbMatch : egbMatches) {
			boolean valid = validateTeams(egbMatch, masterTeamList, teamMappings);
			if (!valid) {
				continue;
			}
			for (Match csglMatch : csglMatches) {
				String egbTeamA = egbMatch.getTeamA();
				String egbTeamB = egbMatch.getTeamB();
				String csglTeamA = csglMatch.getTeamA();
				String csglTeamB = csglMatch.getTeamB();
				
				boolean teamAequal = egbTeamA.equalsIgnoreCase(csglTeamA);
				if (!teamAequal) {
					teamAequal = egbTeamA.equalsIgnoreCase(csglTeamB);
					if (teamAequal) {
						csglMatch.switchTeams();
						csglTeamA = csglMatch.getTeamA();
						csglTeamB = csglMatch.getTeamB();
					} else {
						continue;
					}
				}
				boolean teamBequal = egbTeamB.equalsIgnoreCase(csglTeamB);
				if (!teamBequal) {
					continue;
				}
				recommendations.add(generateRecommendation(egbMatch, csglMatch));
				break;
			}
		}
		return recommendations;
	}
	
	private Recommendation generateRecommendation(Match egbMatch, Match csglMatch) {
		Recommendation recommendation = new Recommendation();
		recommendation.setFormat(csglMatch.getFormat());
		recommendation.setTeamA(csglMatch.getTeamA());
		recommendation.setTeamB(csglMatch.getTeamB());
		recommendation.setDate(csglMatch.getDate());
		recommendation.setCsglOdds(csglMatch.getTeamAodds() + " : " + csglMatch.getTeamBodds());
		recommendation.setEgbOdds(egbMatch.getTeamAodds() + " : " + egbMatch.getTeamBodds());
		double egbOddsDifference = egbMatch.getTeamAodds() - csglMatch.getTeamAodds();
		recommendation.setEgbOddsDifference(egbOddsDifference);
		if (Math.abs(egbOddsDifference) > 5.0) {
			recommendation.setShouldBet(true);
		} else {
			recommendation.setShouldBet(false);
		}
		
		double percentage = calculateKellyCriterion(egbMatch, csglMatch);
		recommendation.setPercentageToBet(percentage);
		recommendation.setBetAmount(percentage * 100 * .5);
		return recommendation;
	}

	private double calculateKellyCriterion(Match egbMatch, Match csglMatch) {
		double egbTeamAodds = egbMatch.getTeamAodds();
		double egbTeamBodds = egbMatch.getTeamBodds();
		double csglTeamAodds = csglMatch.getTeamAodds();
		double csglTeamBodds = csglMatch.getTeamBodds();
		double difference = csglTeamAodds - egbTeamAodds;
		double percentage = 0;
		if (difference <= 0) {
			percentage = getPercentage(csglTeamBodds, csglTeamAodds, egbTeamAodds, egbTeamBodds);
		} else {
			percentage = getPercentage(csglTeamAodds, csglTeamBodds, egbTeamBodds, egbTeamAodds);
		}
		return percentage;
	}
	
	private double getPercentage(double csglTeam1, double csglTeam2, double egbTeam1, double egbTeam2) {
		double csglOdds = csglTeam1 / csglTeam2;
		double percentage = (csglOdds * egbTeam1 - egbTeam2) / csglOdds;
		return percentage;
	}

	private boolean validateTeams(Match egbMatch, Set<String> masterTeamList, Map<String, String> teamMappings) {
		boolean teamAvalid = validateTeam(true, egbMatch, masterTeamList, teamMappings);
		boolean teamBvalid = validateTeam(false, egbMatch, masterTeamList, teamMappings);
		return teamAvalid && teamBvalid;
	}
	
	private boolean validateTeam(boolean teamA, Match egbMatch, Set<String> masterTeamList, Map<String, String> teamMappings) {
		String team = "";
		if (teamA) {
			team = egbMatch.getTeamA();
		} else {
			team = egbMatch.getTeamB();
		}
		if (!masterTeamList.contains(team.toLowerCase())) {
			String mappedTeam = teamMappings.get(team.toLowerCase());
			if (mappedTeam != null) {
				if (teamA) {
					egbMatch.setTeamA(mappedTeam);
				} else {
					egbMatch.setTeamB(mappedTeam);
				}
				return true;
			} else {
				System.out.println("Team " + team + " NOT FOUND!!!  Please add mapping.");
				return false;
			}
		}
		return true;
	}
}
