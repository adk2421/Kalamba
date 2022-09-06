package com.kalamba.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToMap {
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> JsonObjectToMap(JSONObject jsonObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        try { 
            map = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class);

        } catch (JsonParseException e) { 
            e.printStackTrace();
        } catch (JsonMappingException e) { 
            e.printStackTrace();
        } catch (IOException e) { 
            e.printStackTrace();
        }

        return map;
    }
}
