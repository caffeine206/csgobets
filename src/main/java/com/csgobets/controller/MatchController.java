package com.csgobets.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csgobets.service.MatchService;
import com.csgobets.vo.CsglMatch;
import com.csgobets.vo.Match;

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
    public List<CsglMatch> getEgbMatches() {
		return matchService.getMatchesFromEgb();
    }

}
