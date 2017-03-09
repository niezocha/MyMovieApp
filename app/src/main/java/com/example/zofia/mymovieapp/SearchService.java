package com.example.zofia.mymovieapp;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchService {

    @GET("/")
    Observable<SearchResult> search(@Query("s") String title);
}
