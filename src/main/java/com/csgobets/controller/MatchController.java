package com.csgobets.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.csgobets.vo.Match;

@Controller
public class MatchController {
	
	@RequestMapping(value = "/match", method = RequestMethod.GET)
	@ResponseBody
    public List<Match> getMatches() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Match[]> result = restTemplate.exchange(
				"http://csgolounge.com/api/matches", HttpMethod.GET, null, Match[].class);
		Match[] matches = result.getBody(); 
		List<Match> futureMatches = new ArrayList<>();
		for (int i = matches.length - 1; i >= 0; i--) {
			Match match = matches[i];
			Date currentDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String matchDateString = match.getWhen();
			Date matchDate = null;
			try {
				matchDate = formatter.parse(matchDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			matchDate = DateUtils.addHours(matchDate, 3);
			if (matchDate.after(currentDate)) {
				futureMatches.add(match);
			}
		}
		return futureMatches;
    }

}
