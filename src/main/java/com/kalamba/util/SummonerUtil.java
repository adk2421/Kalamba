package com.kalamba.util;

import java.util.ArrayList;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;

public class SummonerUtil {
    // .env 로드
    Dotenv dotenv = Dotenv.load();
    // API Key
    final String API_KEY = dotenv.get("API_AUTH_KEY");

    /**
     * 📢[ API URL 생성 ]
     * @param region
     * @param summonerName
     * @param addURL
     * @return
     */
    public String makeURL(String region, String summonerName, String addURL) {
        String URL = "";
        String ASIA_BASEURL = dotenv.get("API_AUTH_ASIA_BASEURL");
        String KR_BASEURL = dotenv.get("API_AUTH_KR_BASEURL");

        switch (region) {
            case "KR":
                URL += KR_BASEURL;
                URL += addURL + summonerName + "?";
                break;

            case "ASIA":
                URL += ASIA_BASEURL;
                break;
        }

        URL += "api_key=" + API_KEY;

        return URL;
    }

    /**
     * 📢[ 초 단위로 받은 시간 데이터 변환 ]
     * @param second
     * @return
     */
    public String timeFommater(int second) {
        String time = "";

        if (second >= 3600) {
            time += second / 3600 + ":";
        }

        time += (second / 60 < 10 ? "0" + second / 60 : second / 60) + ":";
        time += second % 60 < 10 ? "0" + second % 60 : second % 60;
        
        return time;
    }

    /**
     * 📢[ 게임 전적에서 대상 데이터 추출 ]
     * @param participants
     * @param userPID
     * @return
     */
    public Map<String, Object> selectPlayerInfo(ArrayList<Map<String, Object>> participants, String userPID) {
        for (Map<String, Object> participant : participants) {
            if (userPID.equals(participant.get("puuid")))
                return participant;
        }
        return null;
    }
}
