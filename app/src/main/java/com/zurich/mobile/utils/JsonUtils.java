package com.zurich.mobile.utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class JsonUtils {
    public static boolean isEmptyJson(String json){
        if(json == null){
            return true;
        }
        json = json.trim();
        return "".equals(json) || "null".equalsIgnoreCase(json) || "{}".equalsIgnoreCase(json) || "[]".equals(json);
    }

    public static <Bean> ArrayList<Bean> parseJsonArray(String jsonArrayString, BeanParser<Bean> beanParser) throws JSONException {
        if(isEmptyJson(jsonArrayString)){
            return null;
        }

        JSONArray jsonArray = new JSONArray(jsonArrayString);
        if(jsonArray.length() == 0){
            return null;
        }

        ArrayList<Bean> commentList = new ArrayList<Bean>(jsonArray.length());
        String json;
        for (int i = 0; i < jsonArray.length(); i++) {
            json = jsonArray.optString(i);
            if (!JsonUtils.isEmptyJson(json)) {
                Bean bean = beanParser.parseBean(json);
                if(bean != null){
                    commentList.add(bean);
                }
            }
        }
        return commentList;
    }

    public interface BeanParser<T>{
        T parseBean(String json) throws JSONException;
    }
}