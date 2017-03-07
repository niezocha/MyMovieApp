package com.example.zofia.mymovieapp;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import nucleus.presenter.Presenter;

public class ListingPresenter extends Presenter<ListingActivity>{


    public void getDataAsync(String title){

        new Thread(){
            @Override
            public void run(){

                try {
                    String result = getData(title);

                    SearchResult searchResult = new Gson().fromJson(result, SearchResult.class);

                    getView().setDataOnUiThread(searchResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private String getData(String title) throws IOException{

        String stringUrl = "http://www.omdbapi.com/?s="+title;
        URL url = new URL(stringUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();

        return convertStreamToString(inputStream);
    }

    private String convertStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

}
