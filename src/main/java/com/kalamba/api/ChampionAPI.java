package com.kalamba.api;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class ChampionAPI {

    API API = new API();

    /**
     * 📢[ Data Dragon 최신 버전 ]
     * @param type // "item", "rune", "mastery", "summoner", "champion", "profileicon", "map", "language", "sticker"
     * @return 
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public String getDataVer(String type) throws ParseException {
        String url = "https://ddragon.leagueoflegends.com/realms/kr.json"; // LOL 기본 Data Version API URL

        Map<String, Object> result = (Map<String, Object>) API.callAPI(url, JSONObject.class);
        
        Map<String, Object> dataVersion = (Map<String, Object>) result.get("n");

        return dataVersion.get(type).toString();
    }

    /**
     * 📢[ 챔피언 정보 ]
     * @param championName
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public Object getChampInfo(String championName) throws ParseException {
        String dataVersion = getDataVer("champion");
        String url = "https://ddragon.leagueoflegends.com/cdn/" + dataVersion + "/data/ko_KR/champion/"+ championName + ".json"; // 챔피언 정보 API URL

        Map<String, Object> result = (Map<String, Object>) API.callAPI(url, JSONObject.class);

        result = (Map<String, Object>) result.get("data");

        result = (Map<String, Object>) result.get(championName);

        return result.get("name").toString();
    }
}
