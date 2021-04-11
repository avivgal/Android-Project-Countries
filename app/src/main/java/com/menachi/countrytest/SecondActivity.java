package com.menachi.countrytest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity{
    private MainActivity.Country country = new MainActivity.Country();
    TextView countryName;
    TextView countryNativeName;
    TextView borders;
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        printData();

    }

    public void printData(){

        Intent myIntent = getIntent();
        String chosenCountryName = myIntent.getStringExtra("countryName");
        String chosenCountryNativeName = myIntent.getStringExtra("countryNativeName");
        String[] chosenCountryBorders = myIntent.getStringArrayExtra("countryBorders");
        StringBuilder chosenCountryBordersString = new StringBuilder();

        for (String border : chosenCountryBorders) {
            chosenCountryBordersString.append(border).append("','");
        }
        System.out.println("TCL chosenCountryName " + chosenCountryName);
        System.out.println("TCL chosenCountryNativeName " + chosenCountryNativeName);
        System.out.println("TCL chosenCountryBorders " + chosenCountryBorders);
        System.out.println("TCL chosenCountryBordersString " + chosenCountryBordersString);

        countryName = findViewById(R.id.countryName);
        countryNativeName = findViewById(R.id.nativeName);
        borders = findViewById(R.id.borders);

        countryNativeName.setText("Native Name: "+ chosenCountryNativeName);
        countryName.setText("Country Name: "+ chosenCountryName);
        borders.setText("Borders :" + chosenCountryBordersString);
    }
}