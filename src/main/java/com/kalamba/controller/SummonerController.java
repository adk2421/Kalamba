package com.kalamba.controller;

import com.kalamba.api.API;
import com.kalamba.api.ChampionInfoAPI;
import com.kalamba.service.SummonerService;
import com.kalamba.util.SummonerUtil;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SummonerController {
    // .env 로드
    Dotenv dotenv = Dotenv.load();
    // API Key
    final String API_KEY = dotenv.get("API_AUTH_KEY");

    API API = new API();
    SummonerService summonerService = new SummonerService();
    SummonerUtil summonerUtil = new SummonerUtil();
    ChampionInfoAPI championInfoAPI = new ChampionInfoAPI();
    
    /**
     * 📢[ 소환사 검색 ]
     * @param summonerName
     * @param model
     * @return
     * @throws ParseException
     */
    @PostMapping(value="/searchSummoner")
    public String selectUserInfo(@RequestParam String summonerName, Model model) throws ParseException {
        int matchCount = 5; // 가져올 최근 경기 갯수

        // 소환사 이름 검색 시, 공백 변환
        String summoner = summonerName.replace(" ", "");
        // API [SUMMONER-V4]
        Map<String, Object> summonerInfo = summonerService.summonerV4(summoner);
        
        // 소환사 정보 Model에 저장
        model.addAllAttributes(summonerInfo);
        model.addAttribute("profileIcon", "http://ddragon.leagueoflegends.com/cdn/13.1.1/img/profileicon/" + summonerInfo.get("profileIconId") + ".png");

        // 소환사 puuid 설정
        String userPID = String.valueOf(summonerInfo.get("puuid"));

        ArrayList<Map<String, Object>> playerInfoList = summonerService.matchV5(userPID, matchCount);

        // 소환사 최근 전적
        Map<String, Object> recentRecord = summonerService.recentRecord(playerInfoList);
        model.addAttribute("recentRecord", recentRecord);

        model.addAttribute("playerInfoList", playerInfoList);

        return "summonerInfo";
    }
}