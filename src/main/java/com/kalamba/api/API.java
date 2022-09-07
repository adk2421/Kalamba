package com.kalamba.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public interface API {

    public JSONObject callObjAPI(String requestURL) throws ParseException;

    public JSONArray callArrAPI(String requestURL) throws ParseException;

}