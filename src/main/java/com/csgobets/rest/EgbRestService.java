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

import com.csgobets.vo.Match;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Component(value = "egbRestService")
public class EgbRestService {

	private static final String EGB_URL = "http://egb.com/ajax.php?key=modules_tables_update_UpdateTableBets&act=UpdateTableBets&ajax=update&fg=0&ind=tables&st=1438391464&type=modules&ut=";

	public List<Match> findMatches() {
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
		List<Match> result = new ArrayList<>();
		final JSONObject obj = new JSONObject(pageText);
		final JSONArray bets = obj.getJSONArray("bets");
		Date currentDate = new Date();
		for (int i = 0; i < bets.length(); i++) {
			final JSONObject bet = bets.getJSONObject(i);
			if (bet.getString("game").equals("Counter-Strike") && bet.getInt("winner") == 0) {
				Date matchDate = new Date(bet.getLong("date") * 1000);
				if (matchDate.after(currentDate)) {
					Match match = new Match();
					match.setTeamA(bet.getJSONObject("gamer_1").getString("nick"));
					match.setTeamB(bet.getJSONObject("gamer_2").getString("nick"));

					match.setDate(matchDate);
					double teamAodds = bet.getDouble("coef_1");
					double teamBodds = bet.getDouble("coef_2");
					double total = teamAodds + teamBodds;
					match.setTeamAodds(teamBodds / total);
					match.setTeamBodds(teamAodds / total);
					result.add(match);
				}
			}
		}
		return result;
	}
}
