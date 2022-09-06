package com.kalamba.api;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public interface API {

    public JSONObject callAPI(String requestURL) throws ParseException;

}