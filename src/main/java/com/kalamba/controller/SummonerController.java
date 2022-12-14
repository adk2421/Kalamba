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
    // .env λ‘λ
    Dotenv dotenv = Dotenv.load();
    // API Key
    final String API_KEY = dotenv.get("API_AUTH_KEY");

    API API = new API();
    SummonerService summonerService = new SummonerService();
    SummonerUtil summonerUtil = new SummonerUtil();
    ChampionInfoAPI championInfoAPI = new ChampionInfoAPI();
    
    /**
     * π’[ μνμ¬ κ²μ ]
     * @param summonerName
     * @param model
     * @return
     * @throws ParseException
     */
    @PostMapping(value="/searchSummoner")
    public String selectUserInfo(@RequestParam String summonerName, Model model) throws ParseException {
        
        // μνμ¬ μ΄λ¦ κ²μ μ, κ³΅λ°± λ³ν
        String summoner = summonerName.replace(" ", "");
        // API [SUMMONER-V4]
        Map<String, Object> summonerInfo = summonerService.summonerV4(summoner);
        
        // μνμ¬ μ λ³΄ Modelμ μ μ₯
        model.addAllAttributes(summonerInfo);

        // μνμ¬ puuid μ€μ 
        String userPID = String.valueOf(summonerInfo.get("puuid"));

        ArrayList<Map<String, Object>> playerInfoList = summonerService.matchV5(userPID);

        model.addAttribute("playerInfoList", playerInfoList);

        return "summonerInfo";
    }
}