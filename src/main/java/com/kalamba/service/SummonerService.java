package com.kalamba.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kalamba.api.API;
import com.kalamba.api.DDragonAPI;
import com.kalamba.api.SummonerAPI;
import com.kalamba.entity.UserEntity;
import com.kalamba.util.SummonerUtil;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class SummonerService {
    // .env 로드
    Dotenv dotenv = Dotenv.load();
    // API Key
    final String API_KEY = dotenv.get("API_AUTH_KEY");

    API API = new API();
    SummonerAPI summonerAPI = new SummonerAPI();
    SummonerUtil summonerUtil = new SummonerUtil();
    DDragonAPI dDragonAPI = new DDragonAPI();
    
    /**
     * 📢[ API 'SUMMONER-V4' ]
     * @param summoner
     * @return 
     */
    public UserEntity summonerV4(String summoner) {

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
        String champDataVer = dDragonAPI.getDataVer("champion");

        Map<String, Object> champInfoList = dDragonAPI.getChampInfoList(champDataVer);

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
            String championName = (String) playerInfo.get("championName");
            championName = StringUtils.capitalize(championName);
            if (championName.equals("FiddleSticks"))
                championName = "Fiddlesticks";
            Map<String, Object> champInfo = (Map<String, Object>) champInfoList.get(championName);

            Map<String, Object> prtPlayerInfo = new HashMap<String, Object>();
            JSONObject prtInfoDetail = new JSONObject();

            String gameMode = (String) matchInfoDetail.get("gameMode");
            if (gameMode.equals("ARAM"))
                gameMode = "칼바람 나락";
            else if (gameMode.equals("CLASSIC"))
                gameMode = "소환사의 협곡";
            else if (gameMode.equals("URF"))
                gameMode = "우르프";

            /* Card */
            // #Common
            prtPlayerInfo.put("summonerName", playerInfo.get("summonerName"));
            prtPlayerInfo.put("win", playerInfo.get("win"));
            prtPlayerInfo.put("gameMode", matchInfoDetail.get("gameMode"));
            prtPlayerInfo.put("gameStartTimestamp", summonerUtil.timeStampFommater(matchInfoDetail.get("gameStartTimestamp")));
            prtPlayerInfo.put("gameMode", gameMode);

            // #KDA
            Map<String, Object> challenges = (Map<String, Object>) playerInfo.get("challenges");
            prtPlayerInfo.put("kda", summonerUtil.getKDA(playerInfo));
            prtPlayerInfo.put("kills", playerInfo.get("kills"));
            prtPlayerInfo.put("deaths", playerInfo.get("deaths"));
            prtPlayerInfo.put("assists", playerInfo.get("assists"));

            System.out.println(championName);
            System.out.println(champInfo.get("name"));

            // #Champion
            prtPlayerInfo.put("championName", champInfo.get("name"));
            prtPlayerInfo.put("championImage", "http://ddragon.leagueoflegends.com/cdn/" + champDataVer + "/img/champion/" + championName + ".png"); // 챔피언 이미지

            if (!gameMode.equals("우르프")) {
                prtPlayerInfo.put("teamDamagePercentage", challenges.get("teamDamagePercentage"));

                // 다시하기 예외처리
                boolean earlySurrender = summonerUtil.getEarlySurrender(participants);
                if (earlySurrender) {
                    prtInfoDetail.put("disabledDetail", earlySurrender);
                    prtPlayerInfo.put("win", "remake");
                } else {
                    /* Detail */
                    prtInfoDetail.put("championImage", prtPlayerInfo.get("championImage")); // 챔피언 이미지
                    prtInfoDetail.put("champExperience", playerInfo.get("champExperience")); // 챔피언 숙련도
                    prtInfoDetail.put("gameDuration", summonerUtil.timeFommater(matchInfoDetail.get("gameDuration"))); // 플레이 타임
                    prtInfoDetail.put("killParticipation", summonerUtil.dpFommater(challenges.get("killParticipation"))); // 킬 관여율
                    prtInfoDetail.put("totalDamageDealtToChampions", summonerUtil.getRank("totalDamageDealtToChampions",participants, playerInfo.get("totalDamageDealtToChampions"))); // 챔피언에게 가한 피해량
                }
            }
            prtPlayerInfo.put("prtInfoDetail", prtInfoDetail);
            
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