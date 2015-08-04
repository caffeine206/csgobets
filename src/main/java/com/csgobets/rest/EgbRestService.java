package com.csgobets.rest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.csgobets.vo.CsglMatch;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Component(value = "egbRestService")
public class EgbRestService {
	
	private static final String EGB_URL = "http://egb.com/ajax.php?key=modules_tables_update_UpdateTableBets&act=UpdateTableBets&ajax=update&fg=0&ind=tables&st=1438391464&type=modules&ut=2015-08-02%20+%2012:08:28";
	
	public List<CsglMatch> findMatches() {
		Date date = new Date();
		Date cetDate = DateUtils.addHours(date, 8);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd + HH:mm:ss");
		String cetTime = sdf.format(cetDate);

		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
		
		HtmlPage page = null;
		String pageText = "";
		try {
			page = webClient.getPage(EGB_URL + cetTime);
			pageText = page.asText();
			webClient.close();
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		if (StringUtils.isEmpty(pageText)) {
			return null;
		}
		List<CsglMatch> result = new ArrayList<>();
		final JSONObject obj = new JSONObject(pageText);
		final JSONArray bets = obj.getJSONArray("bets");
		for (int i = 0; i < bets.length(); i++) {
			final JSONObject bet = bets.getJSONObject(i);
			if (bet.getString("game").equals("Counter-Strike")) {
				CsglMatch match = new CsglMatch();
				System.out.print(bet.getJSONObject("gamer_1").getString("nick"));
				System.out.println(" VS " + bet.getJSONObject("gamer_2").getString("nick"));
				match.setA(bet.getJSONObject("gamer_1").getString("nick"));
				match.setB(bet.getJSONObject("gamer_2").getString("nick"));
				Date matchDate = new Date(bet.getLong("date") * 1000);
				matchDate = DateUtils.addHours(matchDate, -8);
				System.out.println("DATE: " + matchDate.toString());
				match.setWhen(matchDate.toString());
			}
		}
		return result;
	}
}
