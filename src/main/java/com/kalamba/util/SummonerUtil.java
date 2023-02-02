package com.kalamba.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        String DD_BASEURL = dotenv.get("DDRAGON_AUTH_BASEURL");

        switch (region) {
            case "KR":
                URL += KR_BASEURL + addURL + summonerName + "?";
                break;

            case "ASIA":
                URL += ASIA_BASEURL + addURL;
                break;

            case "DDRAGON":
                URL += DD_BASEURL + addURL;
                break;
        }

        if (!"DDRAGON".equals(region))
            URL += "api_key=" + API_KEY;

        return URL;
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

    public String getKDA(Map<String, Object> playerInfo) {
        int KA = (int) playerInfo.get("kills") + (int) playerInfo.get("assists");
        
        return String.format("%.1f", (double) KA / (int) playerInfo.get("deaths"));
    }

    public boolean getEarlySurrender(ArrayList<Map<String, Object>> participants) {
        Object TF = true;
        for (Map<String, Object> participant : participants) {
            if (TF.equals(participant.get("teamEarlySurrendered")))
                return true;
        }
        return false;
    }

    public int getRank(String name, ArrayList<Map<String, Object>> participants, Object value) {
        int rank = 1;
        int intValue = (int) value;

        for (Map<String, Object> participant : participants) {
            if (intValue < (int) participant.get(name))
                rank++;
        }

        return rank;
    }

    /**
     * 📢[ 초 단위로 받은 시간 데이터 변환 ]
     * @param second
     * @return
     */
    public String timeFommater(Object sec) {
        String time = "";
        int second = (int) sec;

        if (second >= 3600) {
            time += second / 3600 + ":";
        }

        time += (second / 60 < 10 ? "0" + second / 60 : second / 60) + ":";
        time += second % 60 < 10 ? "0" + second % 60 : second % 60;
        
        return time;
    }

    /**
     * 📢[ 문자열로 받은 시간 데이터 변환 ]
     * @param objTime
     * @return
     */
    public String timeStampFommater(Object objTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String time = sdf.format(new Timestamp(Long.parseLong((String.valueOf(objTime)))));
        
        return time;
    }

    /**
     * 📢[ 소수점 자르기 ]
     * @param objTime
     * @return
     */
    public String dpFommater(Object objNum) {
        double num = Double.parseDouble((String.valueOf(objNum)));
        
        return (Math.round(num * 100) ) + "";
    }
}
