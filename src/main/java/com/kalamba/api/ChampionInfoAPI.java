package com.kalamba.api;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class ChampionInfoAPI {

    API API = new API();

    // LOL Data Version
    @SuppressWarnings("unchecked")
    public String getDataVer(String type) throws ParseException {
        String url = "https://ddragon.leagueoflegends.com/realms/kr.json"; // LOL 기본 Data Version API URL

        Map<String, Object> result = (Map<String, Object>) API.callAPI(url, JSONObject.class);

        Map<String, Object> dataVersion = (Map<String, Object>) result.get("n");

        return dataVersion.get(type).toString();
    }
    
    @SuppressWarnings("unchecked")
    public String getChampInfo(String championName) throws ParseException {
        String dataVersion = getDataVer("champion");
        String url = "https://ddragon.leagueoflegends.com/cdn/" + dataVersion + "/data/ko_KR/champion/"+ championName + ".json"; // 챔피언 정보 API URL

        Map<String, Object> result = (Map<String, Object>) API.callAPI(url, JSONObject.class);

        result = (Map<String, Object>) result.get("data");

        result = (Map<String, Object>) result.get(championName);

        return result.get("name").toString();
    }        
}
