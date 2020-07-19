package com.example.practicawebservice;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Andres Rodriguez
 */
public class VolleySingleton {
    private static VolleySingleton instanceVolley;
    private RequestQueue request;
    private static Context context;

    private VolleySingleton(Context context) {
        this.context = context;
        request = getRequestQueue();
    }


    public static synchronized VolleySingleton getInstanceVolley(Context context){
        if (instanceVolley == null) {
            instanceVolley = new VolleySingleton(context);
        }
        return instanceVolley;
    }

    public RequestQueue getRequestQueue() {
        if (request == null) {
            request = Volley.newRequestQueue(context.getApplicationContext());
        }

        return request;
    }
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}