package com.example.appointment.Api;

import static okhttp3.internal.Internal.instance;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiClient {
    private static RequestQueue requestQueue;
    private static ApiClient instance;
    private static Context ctx;
    public static void init(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            throw new IllegalStateException("ApiClient not initialized. Call ApiClient.init(context) in Application class or Activity.");
        }
        return requestQueue;
    }
    private ApiClient(Context context) {
        ctx = context.getApplicationContext();
        requestQueue = Volley.newRequestQueue(ctx);
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

}
