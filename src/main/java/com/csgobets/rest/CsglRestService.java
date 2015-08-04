package com.csgobets.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.csgobets.vo.CsglMatch;
import com.csgobets.vo.CsglOdds;
import com.csgobets.vo.Match;

@Component(value = "csglRestService")
public class CsglRestService {
	
	private static final String MATCH_URL = "http://csgolounge.com/api/matches";
	
	private static final String ODDS_URL = "http://csgolounge.com/api/matches_stats";
	
	public List<Match> findMatches() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<CsglMatch[]> result = restTemplate.exchange(
				MATCH_URL, HttpMethod.GET, null, CsglMatch[].class);
		CsglMatch[] matches = result.getBody(); 
		Map<Integer, CsglMatch> futureMatches = getFutureMatches(matches);
		ResponseEntity<CsglOdds[]> odds = restTemplate.exchange(
				ODDS_URL, HttpMethod.GET, null, CsglOdds[].class);
		List<Match> combinedMatches = getCombinedMatches(futureMatches, odds.getBody());
		return combinedMatches;
	}
	
	private List<Match> getCombinedMatches(Map<Integer, CsglMatch> futureMatches, CsglOdds[] odds) {
		List<Match> result = new ArrayList<>();
		int numOldMatches = 0;
		for (int i = odds.length - 1; i >= 0; i--) {
			if (numOldMatches > 10) {
				break;
			}
			CsglOdds matchOdds = odds[i];
			Match match = new Match();
			CsglMatch csglMatch = futureMatches.get(matchOdds.getMatch());
			if (csglMatch != null) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					match.setDate(formatter.parse(csglMatch.getWhen()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				match.setFormat(csglMatch.getFormat());
				match.setTeamA(csglMatch.getA());
				match.setTeamB(csglMatch.getB());
				int teamAodds = matchOdds.getA();
				int teamBodds = matchOdds.getB();
				double total = teamAodds + teamBodds;
				match.setTeamAodds(teamAodds/total);
				match.setTeamBodds(teamBodds/total);
				result.add(match);
			} else {
				numOldMatches++;
			}
		}
		return result;
	}

	private Map<Integer, CsglMatch> getFutureMatches(CsglMatch[] matches) {
		Map<Integer, CsglMatch> futureMatches = new HashMap<>();
		int numOldMatches = 0;
		for (int i = matches.length - 1; i >= 0; i--) {
			if (numOldMatches > 10) {
				break;
			}
			CsglMatch match = matches[i];
			if (match.getClosed() != 1) {
				Date currentDate = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String matchDateString = match.getWhen();
				Date matchDate = null;
				try {
					matchDate = formatter.parse(matchDateString);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				matchDate = DateUtils.addHours(matchDate, -9);
				if (matchDate.after(currentDate)) {
					futureMatches.put(match.getMatch(), match);
				} else {
					numOldMatches++;
				}
			}
		}
		return futureMatches;
	}
}
