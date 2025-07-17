package com.example.appointment.Api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UserApi {

    private static final String BASE_URL = "http://192.168.1.90:8080/api/user";

    public static void login(Context context, String email, String password,
                             Response.Listener<org.json.JSONObject> successListener,
                             Response.ErrorListener errorListener) {

        JSONObject loginData = new JSONObject();
        try {
            loginData.put("email", email);
            loginData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SessionCookieRequest request = new SessionCookieRequest(
                SessionCookieRequest.Method.POST,
                BASE_URL + "/login",
                loginData,
                successListener,
                errorListener
        );

        ApiClient.getRequestQueue().add(request);
    }

    public static void logout(Context context,
                              Response.Listener<JSONObject> listener,
                              Response.ErrorListener errorListener) {
        String url = BASE_URL + "/logout";

        SessionCookieRequest request = new SessionCookieRequest(
                SessionCookieRequest.Method.POST,
                url,
                null,   // Không cần body cho logout
                listener,
                errorListener
        );

        ApiClient.getRequestQueue().add(request);
    }
    public static void register(Context context, JSONObject registerData,
                                Response.Listener<JSONObject> successListener,
                                Response.ErrorListener errorListener) {

        String url = BASE_URL + "/register";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                registerData,
                successListener,
                errorListener
        );

        // Cách gọi đúng:
        ApiClient.getInstance(context).addToRequestQueue(request);
    }
    public static void changePassword(Context context,
                                      JSONObject data,
                                      Response.Listener<JSONObject> successListener,
                                      Response.ErrorListener errorListener) {

        String url = BASE_URL + "/change-password";

        SessionCookieRequest request = new SessionCookieRequest(
                Request.Method.PUT,
                url,
                data,
                successListener,
                errorListener
        );

        ApiClient.getInstance(context).addToRequestQueue(request);
    }
    public static void getDoctors(Context context,
                                  Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errorListener) {
        String url = BASE_URL + "/doctors";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                errorListener);

        ApiClient.getInstance(context).addToRequestQueue(request);
    }
    public static void submitAppointment(Context context,
                                         JSONObject data,
                                         Response.Listener<JSONObject> listener,
                                         Response.ErrorListener errorListener) {
        String url = BASE_URL + "/appointment";

        SessionCookieRequest request = new SessionCookieRequest(
                Request.Method.POST,
                url,
                data,
                listener,
                errorListener);

        ApiClient.getInstance(context).addToRequestQueue(request);
    }
    public static void getAppointments(Context context,
                                       Response.Listener<JSONObject> listener,
                                       Response.ErrorListener errorListener) {
        String url = BASE_URL + "/appointments";


        SessionCookieRequest request = new SessionCookieRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                errorListener
        );

        ApiClient.getInstance(context).addToRequestQueue(request);
    }


    public static void deleteAppointment(Context context,
                                         int id,
                                         Response.Listener<JSONObject> listener,
                                         Response.ErrorListener errorListener) {
        String url = BASE_URL + "/appointments/" + id;

        SessionCookieRequest request = new SessionCookieRequest(
                Request.Method.DELETE,
                url,
                null,
                listener,
                errorListener
        );

        ApiClient.getRequestQueue().add(request);
    }
    public static void getAppointmentById(Context context, int id,
                                          Response.Listener<JSONObject> listener,
                                          Response.ErrorListener errorListener) {
        String url = BASE_URL + "/appointments/" + id;

        SessionCookieRequest request = new SessionCookieRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                errorListener
        );

        ApiClient.getInstance(context).addToRequestQueue(request);
    }
    public static void updateAppointment(Context context, int id, JSONObject data,
                                         Response.Listener<JSONObject> listener,
                                         Response.ErrorListener errorListener) {

        String url = BASE_URL + "/appointments/" + id;

        SessionCookieRequest request = new SessionCookieRequest(
                Request.Method.PUT,
                url,
                data,
                listener,
                errorListener
        );

        ApiClient.getInstance(context).addToRequestQueue(request);
    }

}
