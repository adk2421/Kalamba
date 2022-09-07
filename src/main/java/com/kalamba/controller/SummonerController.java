package com.kalamba.controller;

import com.kalamba.api.SummonerInfoAPI;
import com.kalamba.util.JsonToMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
// import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kalamba.util.JsonToMap;

@Controller
public class SummonerController {
    final static String API_KEY = "RGAPI-cc1bf21e-71d8-4ccf-83c5-0694761a9d20";
    
    @PostMapping(value="/searchSummoner")
    public String selectUserInfo(@RequestParam String summonerName, Model model) throws ParseException {
        /*
        ### 소환사 기본 정보 ###
        : Return value: SummonerDTO

        ex) PATH PARAMETERS
            summonerName = "권오빈님"
                    ↓
            RESPONSE BODY {
                "id": "n7B-Fu4jybqp25kh35sI2tw8mwVdIN8oi1Ev3nSLSwkU2A",
                "accountId": "2wKtRIuAZFoWrNrQeeWHlsUReeVf0W7keAoc81pL-CjW",
                "puuid": "7LcGDG-Ai_YFYbk-hU8XUFSlFDK6KBCV_IbCCqRrvEjDzODKTGu2hSqY8M8V1zZryWwx39VuYBDRtw",
                "name": "권오빈님",
                "profileIconId": 3379,
                "revisionDate": 1662052969000,
                "summonerLevel": 208
            }
        */
        String SummonerName = summonerName.replaceAll(" ", "%20");
        String summonerInfoURL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+ SummonerName + "?api_key=" + API_KEY;
        JSONObject summonerInfo = null;

        SummonerInfoAPI summonerInfoAPI = new SummonerInfoAPI();
        summonerInfo = summonerInfoAPI.callObjAPI(summonerInfoURL);

        JsonToMap jsonToMap = new JsonToMap();

        Map<String, Object> summoner = new HashMap<String, Object>();
        summoner = jsonToMap.JsonObjectToMap(summonerInfo);

        model.addAllAttributes(summoner);

        /* 
        ### 소환사 전적 ###
        : Return value: List[string]

        ex) PATH PARAMETERS
            puuid = "7LcGDG-Ai_YFYbk-hU8XUFSlFDK6KBCV_IbCCqRrvEjDzODKTGu2hSqY8M8V1zZryWwx39VuYBDRtw"
                    ↓
            RESPONSE BODY [
                "KR_6105283224",
                "KR_6105280673",
                "KR_6105159499",
                "KR_6105137923",
                "KR_6105146026",
                ...
                ...
            ]
        */
        int matchCount = 10;
        String userMatchURL = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/"+ summoner.get("puuid") + "/ids?start=0&count=" + matchCount + "&api_key=" + API_KEY;
        JSONArray summonerMatch = null;

        summonerMatch = summonerInfoAPI.callArrAPI(userMatchURL);

        ArrayList<String> matchId = new ArrayList<String>();
        
        for (int i = 0; i < summonerMatch.size(); i++) {
            matchId.add(summonerMatch.get(i).toString());

            System.out.println(summonerMatch.get(i));
        }

        /* 
        ### 소환사 전적 정보 ###
        : Return value: MatchDto

        ex) PATH PARAMETERS
            matchId = "KR_6105283224"
                    ↓
            RESPONSE BODY {
                "metadata": {
                    "dataVersion": "2",
                    "matchId": "KR_6105283224",
                    "participants": [
                        "mYCzpzKLbtksSc0qtWeOLSzbhFSMNQqsk4oupHmTO2U_olHbL5MO7YePFTSeYStyt3LpdYo81wVlcA",
                        ...
                    ]
                "info": {
                    ...
                }
            }
        */
        String matchInfoURL = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId.get(0) + "?api_key=" + API_KEY;
        JSONObject matchInfo = null;

        matchInfo = summonerInfoAPI.callObjAPI(matchInfoURL);

        JSONObject matchInfoDetail = (JSONObject) matchInfo.get("info");
        
        System.out.println("gameDuration : " + matchInfoDetail.get("gameDuration"));
        System.out.println("gameMode : " + matchInfoDetail.get("gameMode"));

        JSONArray summonerMatchInfo = (JSONArray )matchInfoDetail.get("participants");

        JSONObject jsonObject1 = (JSONObject) summonerMatchInfo.get(6);

        System.out.println("summonerName : " + jsonObject1.get("summonerName"));
        System.out.println("deaths : " + jsonObject1.get("deaths"));
        System.out.println("championName : " + jsonObject1.get("championName"));

        JSONObject jsonObject2 = (JSONObject) jsonObject1.get("challenges");

        System.out.println("kda : " + jsonObject2.get("kda"));
        System.out.println("teamDamagePercentage : " + jsonObject2.get("teamDamagePercentage"));

        return "summonerInfo";
    }
}
