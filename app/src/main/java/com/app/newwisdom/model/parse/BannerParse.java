package com.app.newwisdom.model.parse;

import com.app.newwisdom.model.entity.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ninedau_zheng on 2017/8/2.
 */

public class BannerParse {

    private Banner parseJson(JSONObject obj) throws JSONException {
        Banner banner = new Banner();
        banner.created_at = obj.getString("created_at");
        banner.link = obj.getString("link");
        banner.pic = obj.getString("pic");
        banner.title = obj.getString("title");
        banner.updated_at = obj.getString("updated_at");
        banner.id = obj.getInt("id");
        banner.status = obj.getInt("status");
        banner.type = obj.getInt("type");
        return banner;
    }

    public List<Banner> parseBannerList(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        JSONArray arr = obj.getJSONArray("banners");
        List<Banner> bannerList = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            bannerList.add(parseJson(arr.getJSONObject(i)));
        }
        return bannerList;
    }

    public Banner parseJson(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        return parseJson(obj);
    }
}
