package com.jeffreyfhow.fakestagram.network.requester;

/**
 * Interface for network requester.
 * Normally there would be one version of the NetworkRequester and an interface wouldn't be needed.
 * But for self-study purposes, this project has three different NetworkRequesters to compare
 * to each other. One that uses OkHttp3, one that uses RxJava and one that uses Rx Java and Java 8.
 */
public interface INetworkRequester {
    void sendGetPostsRequest(String id_token);
    void sendLikeRequest(String id_token, String id);
    void sendUnlikeRequest(String id_token, String id);
    void logOut();
}
