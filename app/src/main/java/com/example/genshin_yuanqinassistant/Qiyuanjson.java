package com.example.genshin_yuanqinassistant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class Qiyuanjson {
    private String uid;
    private String gacha_type;
    private String item_id;
    private String count;
    private String time;
    private String name;
    private String lang;
    private String item_type;
    private String rank_type;
    private String id;

    private  JSONObject jsonObject;

    public Qiyuanjson() {
    }

    public Qiyuanjson(String uid, String gacha_type, String item_id, String count, String time, String name, String lang, String item_type, String rank_type, String id) {
        this.uid = uid;
        this.gacha_type = gacha_type;
        this.item_id = item_id;
        this.count = count;
        this.time = time;
        this.name = name;
        this.lang = lang;
        this.item_type = item_type;
        this.rank_type = rank_type;
        this.id = id;
    }

    public Qiyuanjson(JSONObject jsonObject) {
        this.jsonObject=jsonObject;
    }

    public Qiyuanjson(List<JSONObject> list_changzhu) {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGacha_type() {
        return gacha_type;
    }

    public void setGacha_type(String gacha_type) {
        this.gacha_type = gacha_type;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getRank_type() {
        return rank_type;
    }

    public void setRank_type(String rank_type) {
        this.rank_type = rank_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("uid", uid);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject1.put("gacha_type", gacha_type);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject1.put("item_id", item_id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject1.put("count", count);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject1.put("time", time);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject1.put("name", name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject1.put("lang", lang);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject1.put("item_type", item_type);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject1.put("rank_type", rank_type);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject1.put("id", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return String.valueOf(jsonObject1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Qiyuanjson)) return false;
        Qiyuanjson that = (Qiyuanjson) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
