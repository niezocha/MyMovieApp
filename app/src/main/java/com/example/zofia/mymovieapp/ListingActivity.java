package com.example.zofia.mymovieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity <ListingPresenter>{

    private static final String SEARCH_TITLE = "search_title";
    private MovieListAdapter adapter;
    private ViewFlipper viewFlipper;
    private ImageView noInternetImage;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        String title = getIntent().getStringExtra(SEARCH_TITLE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new MovieListAdapter();
        recyclerView.setAdapter(adapter);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        noInternetImage = (ImageView) findViewById(R.id.no_internet);

        getPresenter().getDataAsync(title);
    }

    public static Intent createIntent(Context context, String title){
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        return intent;
    }

    public void setDataOnUiThread(SearchResult result, boolean noInternet) {
        runOnUiThread(() -> {
                if(noInternet){
                    viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
                }else{
                    viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
                    adapter.setItems(result.getItems());
                }
        });
    }
}
