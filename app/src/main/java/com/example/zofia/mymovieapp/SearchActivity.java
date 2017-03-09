package com.example.zofia.mymovieapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    private Map<Integer, String> apiKeysMap = new HashMap<Integer, String>(){{
        put(R.id.radio_movie, "movie");
        put(R.id.radio_series, "series");
        put(R.id.radio_episode, "episode");
        put(R.id.radio_game, "game");
    }};

    @BindView(R.id.number_picker)
    NumberPicker numberPicker;

    @BindView(R.id.edit_text)
    TextInputEditText editText;

    @BindView(R.id.search_button)
    ImageButton searchButton;

    @BindView(R.id.year_chechbox)
    CheckBox yearCheckBox;

    @BindView(R.id.type_chechbox)
    CheckBox typeCheckBox;

    @BindView(R.id.radio_type)
    RadioGroup typeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        numberPicker.setMinValue(1950);
        numberPicker.setMaxValue(year);
        numberPicker.setValue(year);
        numberPicker.setWrapSelectorWheel(true);
    }

    @OnCheckedChanged(R.id.type_chechbox)
    void onTypeChechboxStateChanged(CompoundButton buttonView, boolean isChecked){
        typeRadioGroup.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
    }

    @OnCheckedChanged(R.id.year_chechbox)
    void onYearChechboxStateChanged(CompoundButton buttonView, boolean isChecked){
        numberPicker.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick(R.id.search_button)
    void onSearchButtonClick() {

        int checkRadioId = typeRadioGroup.getCheckedRadioButtonId();
        String type = typeCheckBox.isChecked() ? apiKeysMap.get(checkRadioId) : null;
        int year =  yearCheckBox.isChecked() ? numberPicker.getValue() : ListingActivity.NO_YEAR_SELECTED;

        startActivity(
                ListingActivity.createIntent(SearchActivity.this, editText.getText().toString(), year, type));
    }
}
