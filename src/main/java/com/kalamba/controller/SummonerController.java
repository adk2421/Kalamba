package com.kalamba.controller;

import com.kalamba.api.SummonerInfoAPI;
import com.kalamba.util.JsonToMap;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
// import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kalamba.util.JsonToMap;

@Controller
public class SummonerController {
    final static String API_KEY = "RGAPI-4aeb9ce3-7680-456d-9590-405ec2b449ee";
    
    @PostMapping(value="/searchSummoner")
    public String selectUserInfo(@RequestParam String summonerName, Model model) throws ParseException {
        /*  
        ### 소환사 기본 정보 ###
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
        String userInfoURL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+ SummonerName + "?api_key=" + API_KEY;
        JSONObject result = null;

        SummonerInfoAPI summonerInfoAPI = new SummonerInfoAPI();
        result = summonerInfoAPI.callAPI(userInfoURL);

        JsonToMap jsonToMap = new JsonToMap();

        Map<String, Object> map = new HashMap<String, Object>();
        map = jsonToMap.JsonObjectToMap(result);

        model.addAllAttributes(map);

        /* 소환사 매치 정보
        String userMatchURL = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/"+ map.get("puuid") + "/ids?start=0&count=20&api_key=" + API_KEY;
        result = summonerInfoAPI.callAPI(userMatchURL);

        // JSONObject to Map
        try { 
            map = new ObjectMapper().readValue(result.toJSONString(), Map.class);
 
        } catch (JsonParseException e) { 
            e.printStackTrace();
        } catch (JsonMappingException e) { 
            e.printStackTrace();
        } catch (IOException e) { 
            e.printStackTrace();
        }

        System.out.println("map : " + map);
        */

        return "summonerInfo";
    }



    
    
}
