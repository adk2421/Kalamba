package com.kalamba.kalamba.Controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class SummonerController {
    final static String API_KEY = "RGAPI-2993ce6f-d23f-4e5c-99c2-9d880be51431";
    
    @PostMapping("/searchSummoner")
    public String searchSummoner(@RequestParam String summonerName) throws ParseException {
        System.out.println("id : " + summonerName);
        JSONObject result = null;
        summonerName = summonerName.replaceAll(" ", "%20");
        String userInfoURL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+ summonerName + "?api_key=" + API_KEY;

        result = callAPI(summonerName, userInfoURL);

        return result.toJSONString();
    }

    private JSONObject callAPI(String summonerName, String requestURL) throws ParseException {
        
        /* REST API 호출하기 */
        URL url = null;
        HttpURLConnection con= null;
		JSONObject result = null;
        StringBuilder sb = new StringBuilder();

        try {
            // URL 객체 생성
            url = new URL(requestURL);
            // URL을 참조하는 객체를 URLConnection 객체로 변환
            con = (HttpURLConnection) url.openConnection();

            // 커넥션 request 방식 "GET"으로 설정
            con.setRequestMethod("GET");

            // 커넥션 request 값 설정(key,value) 
			con.setRequestProperty("Content-type", "application/json");

            // 받아온 JSON 데이터 출력 가능 상태로 변경 (default : false)
			con.setDoOutput(true);

            // 데이터 입력 스트림에 담기
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            while(br.ready()) {
				sb.append(br.readLine());
			}

            con.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* JSON 데이터 파싱하기 */

        // JSONParser에 입력 스트림에 담은 JSON데이터(sb.toString())를 넣어 파싱한 다음 JSONObject로 반환한다.
		result = (JSONObject) new JSONParser().parse(sb.toString());

        // REST API 호출 상태 출력하기
		StringBuilder out = new StringBuilder();
		out.append(result.get("status") +" : " + result.get("status_message") +"\n");

        // JSON데이터에서 "id"라는 JSONObject를 가져온다.
		System.out.println("getId : " + result.get("id"));
        System.out.println("getId : " + result.get("puuid"));

        // String userMatchURL = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/"+ userInfoJson.get("puuid") + "/ids?start=0&count=20&api_key=" + API_KEY;

        return result;
    }
    
}
