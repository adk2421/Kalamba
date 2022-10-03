package com.kalamba.util;

public class SummonerUtil {
    final static String API_KEY = "RGAPI-e172e71c-db53-4eba-9377-1585e850ac17";

    // LOL API URL 만들기
    public String makeURL(String region, String summonerName, String addURL) {
        String URL = "";

        switch (region) {
            case "KR":
                URL += "https://kr.api.riotgames.com/lol";
                URL += addURL + summonerName + "?";
                break;

            case "ASIA":
                URL += "https://asia.api.riotgames.com/lol";
                break;
        }

        URL += "api_key=" + API_KEY;

        return URL;
    }

    public String timeFommater(Long second) {
        String time = "";

        if (second >= 3600) {
            time += second / 3600 + ":";
        }

        time += (second / 60 < 10 ? "0" + second / 60 : second / 60) + ":";
        time += second % 60 < 10 ? "0" + second % 60 : second % 60;
        
        return time;
    }
}
