package com.example.appointment.Api;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SessionCookieRequest extends JsonObjectRequest {
    public static String sessionCookie;

    public SessionCookieRequest(int method, String url, JSONObject jsonRequest,
                                Response.Listener<JSONObject> listener,
                                Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        if (sessionCookie != null) {
            headers.put("Cookie", sessionCookie);
        }
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Map<String, String> headers = response.headers;
        if (headers.containsKey("Set-Cookie")) {
            String cookie = headers.get("Set-Cookie");
            if (cookie != null && cookie.contains("JSESSIONID")) {
                sessionCookie = cookie.split(";")[0];
            }
        }
        return super.parseNetworkResponse(response);
    }
}
