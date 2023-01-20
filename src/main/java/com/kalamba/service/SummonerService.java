package com.kalamba.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.kalamba.api.API;
import com.kalamba.api.ChampionAPI;
import com.kalamba.api.SummonerAPI;
import com.kalamba.util.SummonerUtil;

import io.github.cdimascio.dotenv.Dotenv;
import netscape.javascript.JSObject;

public class SummonerService {
    // .env 로드
    Dotenv dotenv = Dotenv.load();
    // API Key
    final String API_KEY = dotenv.get("API_AUTH_KEY");

    API API = new API();
    ChampionAPI championAPI = new ChampionAPI();
    SummonerAPI summonerAPI = new SummonerAPI();
    SummonerUtil summonerUtil = new SummonerUtil();

    /**
     * 📢[ API 'SUMMONER-V4' ]
     * @param summoner
     * @return 
     */
    public Map<String, Object> summonerV4(String summoner) {

        return summonerAPI.getSummonerInfo(summoner);
    }

    /**
     * 📢[ API 'MATCH-V5' ]
     * @param userPID
     * @param matchCount
     * @return 
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> matchV5(String userPID, int matchCount) throws ParseException {
        // 플레이한 게임 리스트 가져오기
        ArrayList<String> matchIdList = summonerAPI.getMatchList(userPID, matchCount);
        
        // 대상 소환사 전적 정보를 저장할 배열
        ArrayList<Map<String, Object>> playerInfoList = new ArrayList<Map<String, Object>>();

        for (String matchId : matchIdList) {
            // 플레이한 게임 정보 가져오기
            Map<String, Object> matchInfo = summonerAPI.getMatchInfo(matchId);

            // 전적 상세 정보
            Map<String, Object> matchInfoDetail = (Map<String, Object>) matchInfo.get("info");
            ArrayList<Map<String, Object>> participants = (ArrayList<Map<String, Object>>) matchInfoDetail.get("participants");

            // 대상 소환사 전적 정보
            Map<String, Object> playerInfo = summonerUtil.selectPlayerInfo(participants, userPID);
            Map<String, Object> prtPlayerInfo = new HashMap<String, Object>();

            prtPlayerInfo.put("matchInfo", matchInfo);

            // #Common
            prtPlayerInfo.put("summonerName", playerInfo.get("summonerName"));
            prtPlayerInfo.put("win", playerInfo.get("win"));
            prtPlayerInfo.put("gameDuration", summonerUtil.timeFommater(Integer.parseInt(matchInfoDetail.get("gameDuration").toString())));
            prtPlayerInfo.put("gameMode", matchInfoDetail.get("gameMode"));
            prtPlayerInfo.put("gameStartTimestamp", summonerUtil.timeStampFommater(matchInfoDetail.get("gameStartTimestamp")));

            // #KDA
            Map<String, Object> challenges = (Map<String, Object>) playerInfo.get("challenges");
            prtPlayerInfo.put("kda", String.format("%.1f", Double.parseDouble(String.valueOf(challenges.get("kda")))));
            prtPlayerInfo.put("kills", playerInfo.get("kills"));
            prtPlayerInfo.put("deaths", playerInfo.get("deaths"));
            prtPlayerInfo.put("assists", playerInfo.get("assists"));

            prtPlayerInfo.put("teamDamagePercentage", challenges.get("teamDamagePercentage"));

            // #Champion
            String championName = String.valueOf(playerInfo.get("championName"));
            prtPlayerInfo.put("championName", championAPI.getChampInfo(championName));
            prtPlayerInfo.put("championImage", "http://ddragon.leagueoflegends.com/cdn/12.18.1/img/champion/" + championName + ".png"); // 챔피언 이미지
            
            playerInfoList.add(prtPlayerInfo);
        }

        return playerInfoList;
    }

    /**
     * 📢[ 소환사 최근 경기 전적 ]
     * @param userPID
     * @return
     */
    public Map<String, Object> recentRecord(ArrayList<Map<String, Object>> playerInfoList) {
        Map<String, Object> recentRecord = new HashMap<String, Object>();
        int wins = 0, losses = 0;

        for (Map<String, Object> infoItem : playerInfoList) {
            if (infoItem.get("win").equals(true))
                wins++;
            else
                losses++;
        }

        recentRecord.put("wins", wins);
        recentRecord.put("losses", losses);

        return recentRecord;
    }
}