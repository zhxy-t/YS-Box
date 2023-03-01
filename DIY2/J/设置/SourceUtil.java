package com.github.tvbox.osc.util;

import com.github.tvbox.osc.bean.ApiModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.request.GetRequest;
import com.orhanobut.hawk.Hawk;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class SourceUtil {
    private static List<ApiModel> history = null;
    private static Map<String, String> historyMap = new HashMap();
    private static final String requestAccept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";
    private static final String userAgent = "okhttp/3.15";

    public static void addSource(String str) {
        for (ApiModel apiModel : (ApiModel[]) new Gson().fromJson(((JsonObject) new Gson().fromJson(str, JsonObject.class)).get("urls"), ApiModel[].class)) {
            if (indexOf(apiModel.getUrl()).intValue() == -1) {
                history.add(apiModel);
                historyMap.put(apiModel.getUrl(), apiModel.getName());
                Hawk.put("api_history", history);
            }
        }
        if (getCurrentApi().getUrl().isEmpty()) {
            setCurrentApi((ApiModel) history.get(0));
        }
    }
/*
    public static void loadStoreHouse(String str, Callback<Map<String, String>> callback) {
        Iterator it = ((JsonObject) new Gson().fromJson(str, JsonObject.class)).getAsJsonArray("storeHouse").iterator();
        while (it.hasNext()) {
            HttpGet httpGet = new HttpGet(url);
            JsonObject jsonObject = (JsonObject) ((JsonElement) it.next());
            httpGet(jsonObject.get("sourceUrl").getAsString().trim(), new 4(jsonObject, callback));
        }
    }
*/
    public static List<ApiModel> putHistory(List<ApiModel> list) {
        historyMap = new HashMap();
        history = list;
        Hawk.put("api_history", list);
        for (ApiModel apiModel : history) {
            historyMap.put(apiModel.getUrl(), apiModel.getName());
        }
        return history;
    }

    public static ApiModel setCurrentApi(String str) {
        String apiName = getApiName(str);
        Hawk.put("api_name", apiName);
        Hawk.put("api_url", str);
        ApiModel apiModel = new ApiModel();
        apiModel.setUrl(str);
        apiModel.setName(apiName);
        return apiModel;
    }

    public static List<ApiModel> addHistory(ApiModel apiModel) {
        if (indexOf(apiModel.getUrl()).intValue() == -1) {
            history.add(0, apiModel);
            historyMap.put(apiModel.getUrl(), apiModel.getName());
            Hawk.put("api_history", history);
        }
        return history;
    }

   // public static void httpGet(String str, Callback<String> callback) {
       // ((GetRequest) ((GetRequest) ((GetRequest) JsonObject.get(str).headers("User-Agent", userAgent)).headers("Accept", requestAccept)).cacheMode(CacheMode.NO_CACHE)).execute(new 1(callback));
   // }

    public static Integer indexOf(String str) {
        for (int i = 0; i < history.size(); i++) {
            if (StringUtils.equals(((ApiModel) history.get(i)).getUrl(), str)) {
                return Integer.valueOf(i);
            }
        }
        return Integer.valueOf(-1);
    }

    public static void replaceAllSource(String str) {
        List asList = Arrays.asList((ApiModel[]) new Gson().fromJson(((JsonObject) new Gson().fromJson(str, JsonObject.class)).get("urls"), ApiModel[].class));
        history = asList;
        putHistory(asList);
        setCurrentApi((ApiModel) history.get(0));
    }

    public static List<String> getHistoryApiUrls() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < history.size(); i++) {
            arrayList.add(((ApiModel) history.get(i)).getUrl());
        }
        return arrayList;
    }

    public static List<ApiModel> removeHistory(String str) {
        history.remove(indexOf(str).intValue());
        putHistory(history);
        return history;
    }

    public static List<ApiModel> clearHistory() {
        history.clear();
        historyMap.clear();
        return history;
    }

    public static ApiModel getCurrentApi() {
        ApiModel apiModel = new ApiModel();
        String str = "";
        apiModel.setUrl((String) Hawk.get("api_url", str));
        apiModel.setName((String) Hawk.get("api_name", str));
        return apiModel;
    }

    public static void init() {
        List historyFromDB = getHistoryFromDB();
        history = historyFromDB;
        if (historyFromDB.size() > 0) {
            putHistory(history);
        }
    }

    public static void clearCurrentApi() {
        String str = "";
        Hawk.put("api_name", str);
        Hawk.put("api_url", str);
    }

    public static String getApiName(String str) {
        String str2 = (String) historyMap.get(str);
        return StringUtils.isBlank(str2) ? str : str2;
    }

    public static ApiModel setCurrentApi(ApiModel apiModel) {
        Hawk.put("api_name", apiModel.getName());
        Hawk.put("api_url", apiModel.getUrl());
        return apiModel;
    }

   // public static void addSource(String str, Callback<String> callback) {
     //   httpGet(str, new 2(callback));
    //}

    public static List<ApiModel> getHistory() {
        return history;
    }

    public static List<ApiModel> getHistoryFromDB() {
        return getHistoryFromDB(new ArrayList());
    }

    public static List<ApiModel> getHistoryFromDB(List<ApiModel> list) {
        return (List) Hawk.get("api_history", list);
    }

    //public static void replaceAllSource(String str, Callback<String> callback) {
       // httpGet(str, new 3(callback));
   // }
}
