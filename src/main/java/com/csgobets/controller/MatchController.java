package com.csgobets.controller;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.csgobets.vo.Match;

@Controller
public class MatchController {
	
	@RequestMapping("/match")
    public ResponseEntity<Match[]> getMatches() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Match[]> result = restTemplate.exchange(
				"http://csgolounge.com/api/matches", HttpMethod.GET, null, Match[].class);
        return result;
    }

}
