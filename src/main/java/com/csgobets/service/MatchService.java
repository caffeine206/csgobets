package com.csgobets.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.csgobets.rest.CsglRestService;
import com.csgobets.rest.EgbRestService;
import com.csgobets.vo.CsglMatch;
import com.csgobets.vo.Match;

@Component(value = "matchService")
public class MatchService {
	
	@Resource(name = "csglRestService")
	CsglRestService csglRestService;
	
	@Resource(name = "egbRestService")
	EgbRestService egbRestService;
	
	public List<Match> getMatchesFromCsgl() {
		return csglRestService.findMatches();
	}
	
	public List<CsglMatch> getMatchesFromEgb() {
		return egbRestService.findMatches();
	}
}
