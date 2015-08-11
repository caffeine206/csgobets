package com.csgobets.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csgobets.service.MatchService;
import com.csgobets.vo.Match;
import com.csgobets.vo.Recommendation;

@Controller
public class MatchController {
	
	@Resource(name = "matchService")
	MatchService matchService;
	
	@RequestMapping(value = "/match", method = RequestMethod.GET)
	@ResponseBody
    public List<Match> getMatches() {
		return matchService.getMatchesFromCsgl();
    }
	
	@RequestMapping(value = "/match2", method = RequestMethod.GET)
	@ResponseBody
    public List<Match> getEgbMatches() {
		return matchService.getMatchesFromEgb();
    }
	
	@RequestMapping(value = "/teams", method = RequestMethod.GET)
	@ResponseBody
    public Set<String> getTeams() {
		return matchService.getTeams();
    }
	
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	@ResponseBody
    public Map<String, String> readFile() {
		return matchService.getTeamMappings();
    }
	
	@RequestMapping(value = "/bet", method = RequestMethod.GET)
	@ResponseBody
    public List<Recommendation> recommendBet() {
		return matchService.recommendBet();
    }
	

}
