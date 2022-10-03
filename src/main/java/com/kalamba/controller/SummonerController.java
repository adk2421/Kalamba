package com.kalamba.controller;

import com.kalamba.api.API;
import com.kalamba.api.ChampionInfoAPI;
import com.kalamba.util.JsonToMap;
import com.kalamba.util.SummonerUtil;

import java.nio.charset.spi.CharsetProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SummonerController {
    final static String API_KEY = "RGAPI-e172e71c-db53-4eba-9377-1585e850ac17";

    API API = new API();
    SummonerUtil summonerUtil = new SummonerUtil();
    JsonToMap jsonToMap = new JsonToMap();
    ChampionInfoAPI championInfoAPI = new ChampionInfoAPI();
    
    // 소환사 검색
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
                "puuid": "7LcGDG-Ai_YFYbk-hU8XUFSlFDK6KBCV_IbCCqRrvEjDzODKTGu2hSqY8M8V1zZryWwx39VuYBDRtw", // 경기 정보 가져올 때 사용하는 소환사 ID
                "name": "권오빈님",
                "profileIconId": 3379,
                "revisionDate": 1662052969000,
                "summonerLevel": 208
            }
        */
        String SummonerName = summonerName.replaceAll(" ", "%20"); // 소환사 이름 검색 시, 공백 변환
        String summonerInfoURL = summonerUtil.makeURL("KR", SummonerName, "/summoner/v4/summoners/by-name/"); // API URL 설정
        
        JSONObject summonerInfo = API.callObjAPI(summonerInfoURL); // API 호출

        Map<String, Object> summoner = new HashMap<String, Object>();
        summoner = jsonToMap.JsonObjectToMap(summonerInfo); // 소환사 정보 Map에 저장

        model.addAllAttributes(summoner); // 소환사 정보 Model에 저장

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
        int matchCount = 10; // 가져올 최근 경기 갯수
        String userMatchURL = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/"+ summoner.get("puuid") + "/ids?start=0&count=" + matchCount + "&api_key=" + API_KEY; // API URL 설정
        
        JSONArray summonerMatch = API.callArrAPI(userMatchURL); // API 호출

        ArrayList<String> matchId = new ArrayList<String>();
        
        for (int i=0; i < summonerMatch.size(); i++) { // JSONArray to ArrayList
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
        //for (int i=0; i < matchId.size(); i++) { 
            // 게임 참가 플레이어 정보
            String matchInfoURL = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId.get(0) + "?api_key=" + API_KEY; // API URL 설정
            
            JSONObject matchInfo = API.callObjAPI(matchInfoURL); // API 호출

            JSONObject matchInfoDetail = (JSONObject) matchInfo.get("info");
            
            model.addAttribute("gameDuration", summonerUtil.timeFommater((Long) matchInfoDetail.get("gameDuration")));
            model.addAttribute("gameMode", matchInfoDetail.get("gameMode"));

            JSONArray summonerMatchInfo = (JSONArray) matchInfoDetail.get("participants");

            JSONObject jsonObject1 = (JSONObject) summonerMatchInfo.get(6);

            model.addAttribute("summonerName", jsonObject1.get("summonerName"));
            model.addAttribute("deaths", jsonObject1.get("deaths"));

            String championName = jsonObject1.get("championName").toString();

            JSONObject jsonObject2 = (JSONObject) jsonObject1.get("challenges");

            model.addAttribute("kda", jsonObject2.get("kda"));
            model.addAttribute("teamDamagePercentage", jsonObject2.get("teamDamagePercentage"));
        //}

        model.addAttribute("championName", championInfoAPI.getChampInfo(championName));
        model.addAttribute("championImage", "http://ddragon.leagueoflegends.com/cdn/12.18.1/img/champion/" + championName + ".png"); // 챔피언 이미지

        return "summonerInfo";
    }
}
