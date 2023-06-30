package com.kalamba.api;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.kalamba.entity.UserEntity;
import com.kalamba.util.SummonerUtil;

public class SummonerAPI {

    API API = new API();
    SummonerUtil summonerUtil = new SummonerUtil();

    /**
     * 📢[ API 'SUMMONER-V4': 소환사 기본 정보 ]
     * @param summoner
     * @return 
     */
    public UserEntity getSummonerInfo(String summoner) {
        String addURL = "/summoner/v4/summoners/by-name/";
        String url = summonerUtil.makeURL("KR", summoner, addURL);
        UserEntity userEntity = (UserEntity) API.callAPI(url, UserEntity.class);
        
        return userEntity;
    }

    /**
     * 📢[ API 'MATCH-V5': 플레이 게임 리스트 ]
     * @param userPID
     * @param matchCount
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getMatchList(String userPID, int matchCount) {
        String addURL = "/match/v5/matches/by-puuid/"+ userPID + "/ids?start=0&count=" + matchCount + "&";
        String url = summonerUtil.makeURL("ASIA", "", addURL);
                
        return (ArrayList<String>) API.callAPI(url, JSONArray.class);
    }

    /**
     * 📢[ API 'MATCH-V5': 플레이 게임 정보 ]
     * @param matchId
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMatchInfo(String matchId) {
        String addURL = "/match/v5/matches/" + matchId + "?";
        String url = summonerUtil.makeURL("ASIA", "", addURL);
                
        return (Map<String, Object>) API.callAPI(url, JSONObject.class);
    }
}
