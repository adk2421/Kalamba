package com.kalamba.kalamba.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {
    final static String API_KEY = "RGAPI-2993ce6f-d23f-4e5c-99c2-9d880be51431";
    
    @RequestMapping(value="/searchSummoner11")
    public String searchSummoner(Model model, HttpServletRequest request) {
        String summonerName = request.getParameter("summonerName");
        summonerName = summonerName.replaceAll(" ", "%20");
        String requestURL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+ summonerName + "?api_key=" + API_KEY;

        model.addAttribute("summonerName", summonerName);
        model.addAttribute("requestURL", requestURL);

        return "mainPage";
    }
}
