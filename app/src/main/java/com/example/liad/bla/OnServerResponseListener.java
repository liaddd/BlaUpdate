package com.example.liad.bla;
import org.json.JSONObject;


public interface OnServerResponseListener {


    void onSuccess(JSONObject data);

    void onFailure(JSONObject error);
}