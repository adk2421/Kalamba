package com.kalamba.service;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.kalamba.api.API;
import com.kalamba.api.ChampionInfoAPI;
import com.kalamba.util.SummonerUtil;

import io.github.cdimascio.dotenv.Dotenv;

public class SummonerService {
    // .env 로드
    Dotenv dotenv = Dotenv.load();
    // API Key
    final String API_KEY = dotenv.get("API_AUTH_KEY");

    API API = new API();
    ChampionInfoAPI championInfoAPI = new ChampionInfoAPI();
    SummonerUtil summonerUtil = new SummonerUtil();

    /**
     * 📢[ API SUMMONER-V4 ]
     * @param summoner
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> summonerV4(String summoner) {
        /* 소환사 기본 정보
        : Return value: SummonerDTO

        ex) PATH PARAMETERS
            summonerName = "권오빈님"
                    ↓
            RESPONSE BODY {
                "id": "n7B-Fu4jybqp25kh35sI2tw8mwVdIN8oi1Ev3nSLSwkU2A",
                "accountId": "2wKtRIuAZFoWrNrQeeWHlsUReeVf0W7keAoc81pL-CjW",
                "puuid": "7LcGDG-Ai_YFYbk-hU8XUFSlFDK6KBCV_IbCCqRrvEjDzODKTGu2hSqY8M8V1zZryWwx39VuYBDRtw", // 경기 정보 가져올 때 사용하는 소환사 ID
                "name": "권오빈님",
                "profileIconId": 3379,
                "revisionDate": 1662052969000,
                "summonerLevel": 208
            }
        */
        String summonerInfoURL = summonerUtil.makeURL("KR", summoner, "/summoner/v4/summoners/by-name/");  
        
        return (Map<String, Object>) API.callAPI(summonerInfoURL, JSONObject.class);
    }

    /**
     * 📢[ API MATCH-V5 ]
     * @param userPID
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> matchV5(String userPID) throws ParseException {
        int matchCount = 8; // 가져올 최근 경기 갯수

        /* 소환사 전적 목록 ID */
        String userMatchURL = summonerUtil.makeURL("ASIA", "", "/match/v5/matches/by-puuid/"+ userPID + "/ids?start=0&count=" + matchCount + "&");
        ArrayList<String> matchId = (ArrayList<String>) API.callAPI(userMatchURL, JSONArray.class);
        
        // 대상 소환사 전적 정보 리스트
        ArrayList<Map<String, Object>> playerInfoList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < matchCount; i++) {

            // 소환사 전적 정보
            String matchInfoURL = summonerUtil.makeURL("ASIA", "", "/match/v5/matches/" + matchId.get(i) + "?");
            System.out.println(matchInfoURL);
            Map<String, Object> matchInfo = (Map<String, Object>) API.callAPI(matchInfoURL, JSONObject.class);

            // 전적 상세 정보
            Map<String, Object> matchInfoDetail = (Map<String, Object>) matchInfo.get("info");
            ArrayList<Map<String, Object>> participants = (ArrayList<Map<String, Object>>) matchInfoDetail.get("participants");

            // 대상 소환사 전적 정보
            Map<String, Object> playerInfo = summonerUtil.selectPlayerInfo(participants, userPID);
            playerInfo.put("gameDuration", summonerUtil.timeFommater(Integer.parseInt(matchInfoDetail.get("gameDuration").toString())));
            playerInfo.put("gameMode", matchInfoDetail.get("gameMode"));

            Map<String, Object> challenges = (Map<String, Object>) playerInfo.get("challenges");
            playerInfo.put("kda", String.format("%.1f", Double.parseDouble(challenges.get("kda").toString())));
            playerInfo.put("teamDamagePercentage", challenges.get("teamDamagePercentage"));

            String championName = String.valueOf(playerInfo.get("championName"));
            playerInfo.put("championName", championInfoAPI.getChampInfo(championName));
            playerInfo.put("championImage", "http://ddragon.leagueoflegends.com/cdn/12.18.1/img/champion/" + championName + ".png"); // 챔피언 이미지
            
            playerInfoList.add(playerInfo);
        }

        return playerInfoList;
    }
    
}