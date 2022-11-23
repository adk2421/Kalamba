package com.kalamba.util;

import io.github.cdimascio.dotenv.Dotenv;

public class SummonerUtil {
    // .env 로드
    Dotenv dotenv = Dotenv.load();
    // API Key
    final String API_KEY = dotenv.get("API_AUTH_KEY");

    // LOL API URL 만들기
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

    public String timeFommater(int second) {
        String time = "";

        if (second >= 3600) {
            time += second / 3600 + ":";
        }

        time += (second / 60 < 10 ? "0" + second / 60 : second / 60) + ":";
        time += second % 60 < 10 ? "0" + second % 60 : second % 60;
        
        return time;
    }
}
