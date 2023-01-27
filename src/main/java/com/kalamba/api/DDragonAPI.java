package com.kalamba.api;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.kalamba.util.SummonerUtil;

public class DDragonAPI {

    API API = new API();
    SummonerUtil summonerUtil = new SummonerUtil();

    /**
     * 📢[ Data Dragon 현재 버전 ]
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
     * 📢[ 챔피언 정보 리스트 ]
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getChampInfoList(String champDataVer) throws ParseException {
        String url = summonerUtil.makeURL("DDRAGON", "", champDataVer + "/data/ko_KR/champion.json");
        
        Map<String, Object> result = (Map<String, Object>) API.callAPI(url, JSONObject.class);
        
        Map<String, Object> champInfoList = (Map<String, Object>) result.get("data");

        return champInfoList;
    }

    /**
     * 📢[ 챔피언 정보 ]
     * @param championName
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public Object getChampInfo(String championName, String champDataVer) throws ParseException {
        String url = "https://ddragon.leagueoflegends.com/cdn/" + champDataVer + "/data/ko_KR/champion/"+ championName + ".json";

        Map<String, Object> result = (Map<String, Object>) API.callAPI(url, JSONObject.class);

        result = (Map<String, Object>) result.get("data");

        result = (Map<String, Object>) result.get(championName);

        return result.get("name").toString();
    }
}
