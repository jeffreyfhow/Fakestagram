package com.jeffreyfhow.fakestagram.network.helper;

import android.util.Log;

import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Used to create/manage/access/remove 1 or more OkHttpClients
 * Each OkHttpClient has a String key to identify it
 * OkHttpClients are built off of each other (or the default OkHttpClient) via newBuilder()
 */
public class OkHttpClientManager {

    private volatile static OkHttpClientManager instance;
    private static HashMap<String, OkHttpClient> okHttpClients;

    public static String BASE_ID = "BASE_ID";

    private OkHttpClientManager(){
        okHttpClients = new HashMap<>();
        okHttpClients.put(BASE_ID, new OkHttpClient());
    }

    /**
     * Gets the Singleton instance of the OkHttpClientManager
     * @return
     */
    public static OkHttpClientManager getInstance(){
        if(instance == null){
            synchronized (OkHttpClientManager.class){
                if(instance == null) {
                    instance = new OkHttpClientManager();
                }
            }
        }
        return instance;
    }

    /**
     *
     * @param key: the String key used to access this OkHttpClient
     * @param interceptors: Any interceptors to add to the OkHttpClient
     * @return
     *      if OkHttpClient with 'key' already exists -> null
     *      otherwise -> newly create clone OkHttpClient
     */
    public OkHttpClient createOkHttpClient(String key, List<Interceptor> interceptors){
        return cloneOkHttpClient(key, BASE_ID, interceptors);
    }

    /**
     *
     * @param key: the String key used to access this OkHttpClient
     * @param originalKey: the String key of the OkHttpClient to clone
     * @param interceptors: Any interceptors to add to the clone OkHttpClient
     * @return
     *      if OkHttpClient with 'key' already exists -> null
     *      if no OkHttpClient with 'originalKey' exists to clone -> null
     *      otherwise -> newly create clone OkHttpClient
     */
    public OkHttpClient cloneOkHttpClient(String key, String originalKey, List<Interceptor> interceptors){
        if(okHttpClients.containsKey(key)){
            Log.d(this.getClass().getSimpleName(), "OkHttpClient with id " + key + " already exists");
            return null;
        }

        if(!okHttpClients.containsKey(originalKey)){
            Log.d(this.getClass().getSimpleName(), "OkHttpClient with id " + originalKey + " DNE");
            return null;
        }

        OkHttpClient.Builder okHttpBuilder = okHttpClients.get(originalKey).newBuilder();

        for (Interceptor i : interceptors) {
            okHttpBuilder.addInterceptor(i);
        }

        OkHttpClient okHttpClient = okHttpBuilder.build();

        okHttpClients.put(key, okHttpClient);

        return okHttpClient;
    }

    /**
     * Retrieves the OkHttpClient by name
     * @param key: the String key used to identify this OkHttpClient
     * @return
     *      if no OkHttpClient with 'key' exists -> null
     *      otherwise -> OkHttpClient found
     */
    public OkHttpClient getOkHttpClient(String key){
        if(okHttpClients.containsKey(key)){
            return okHttpClients.get(key);
        }
        return null;
    }

    /**
     * Removes the OkHttpClient with id 'key'
     * @param key: the String key used to identify this OkHttpClient
     */
    public void removeOkHttpClient(String key){
        okHttpClients.remove(key);
    }

    /**
     * Clears all OkHttpClients
     */
    public void clear(){
        okHttpClients.clear();
    }
}
