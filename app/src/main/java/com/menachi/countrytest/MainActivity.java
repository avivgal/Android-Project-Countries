package com.menachi.countrytest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private List<Country> countres;
    private Button order_by_name;
    private Button order_by_area;
    private Boolean sortName;
    private Boolean sortArea;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sortName = false;
        sortArea = false;

        order_by_name = findViewById(R.id.order_by_name);
        order_by_area = findViewById(R.id.order_by_area);

        loadingDialog();
        getJson();



        order_by_name.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(!sortName){
                    clearTable();
                    sortByName();
                    addHeaders();
                    buildTable();
                    sortName=true;
                }else {
                    clearTable();
                    sortByNameReverse();
                    addHeaders();
                    buildTable();
                    sortName=false;
                }
            }
        });
        order_by_area.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(!sortArea){
                    clearTable();
                    sortByArea();
                    addHeaders();
                    buildTable();
                    sortArea=true;
                }else {
                    clearTable();
                    sortByAreaReverse();
                    addHeaders();
                    buildTable();
                    sortArea=false;
                }
            }
        });
    }

    public void sortByName(){
        if(countres == null){
            Log.i("TAG","country is null");
        }else {
            Log.i("TAG","size " + countres.size());
        }
        Collections.sort(countres, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                Log.i("TAG","o1: "+ o1.getName() + " o2: "+o2.getName());
                if(o1 != null && o2 != null) {
                    if (o1.getName() == null) {
                        o1.setName(null);
                    }
                    if (o2.getName() == null) {
                        o2.setName(null);
                    }
                    return o1.getName().compareTo(o2.getName());
                }
                else {
                    return 0;
                }
            }
        });

    }
    public void sortByNameReverse(){
        if(countres == null){
            Log.i("TAG","country is null");
        }else {
            Log.i("TAG","size " + countres.size());
        }
        Collections.sort(countres, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                Log.i("TAG","o1: "+ o1.getName() + " o2: "+o2.getName());
                if(o1 != null && o2 != null) {
                    if (o1.getName() == null) {
                        o1.setName(null);
                    }
                    if (o2.getName() == null) {
                        o2.setName(null);
                    }
                    return o2.getName().compareTo(o1.getName());
                }
                else {
                    return 0;
                }
            }
        });

    }

    public void sortByArea(){
        if(countres == null){
            Log.i("TAG","country is null");
        }else {
            Log.i("TAG","size " + countres.size());
        }
        Collections.sort(countres, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                Log.i("TAG","o1: "+ o1.getArea() + " o2: "+o2.getArea());
                if(o1 != null && o2 != null) {
                    if (o1.getArea() == null) {
                        o1.setArea(0.0);
                    }
                    if (o2.getArea() == null) {
                        o2.setArea(0.0);
                    }
                        return (int) (o1.getArea() - o2.getArea());
                }
                else {
                    return 0;
                }
            }
        });
    }
    public void sortByAreaReverse(){
        if(countres == null){
            Log.i("TAG","country is null");
        }else {
            Log.i("TAG","size " + countres.size());
        }
        Collections.sort(countres, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                Log.i("TAG","o1: "+ o1.getArea() + " o2: "+o2.getArea());
                if(o1 != null && o2 != null) {
                    if (o1.getArea() == null) {
                        o1.setArea(0.0);
                    }
                    if (o2.getArea() == null) {
                        o2.setArea(0.0);
                    }
                    return (int) (o2.getArea() - o1.getArea());
                }
                else {
                    return 0;
                }
            }
        });
    }

    public void loadingDialog (){
        LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        },5000);
    }

    private LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(3, 3, 3, 3);
        return params;
    }
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }
    public TextView getTextView(int id, String title, int color, int typeface, int bgColor){
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(10, 10, 10, 10);
        tv.setWidth(160);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        return tv;
    }

    public void clearTable(){
        TableLayout tableL= findViewById(R.id.table);
        int count = tableL.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = tableL.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }

    public void addHeaders(){
        TableLayout tableL= findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0,"Name", Color.WHITE, Typeface.BOLD, Color.RED));
        tr.addView(getTextView(1,"Native Name", Color.WHITE, Typeface.BOLD, Color.RED));
        tr.addView(getTextView(2,"Area", Color.WHITE, Typeface.BOLD, Color.RED));
        tableL.addView(tr,getTblLayoutParams());
    }

    public void addData(String myResponse) {
        this.countres = convertFromJson(myResponse);
        buildTable();
    }

    public void buildTable(){
        if(countres != null){
            int numOfCountry = countres.size();
            TableLayout tableL= findViewById(R.id.table);
            for (int i = 0; i < numOfCountry; i++) {
                TableRow tr = new TableRow(this);
                Country currentCountry = countres.get(i);

                tr.setLayoutParams(getLayoutParams());
                tr.addView(getTextView(i+1, currentCountry.getName(), Color.WHITE, Typeface.NORMAL,Color.BLUE));
                tr.addView(getTextView( i+numOfCountry , currentCountry.getNativeName(), Color.WHITE, Typeface.NORMAL, Color.BLUE));
                tr.addView(getTextView( i+numOfCountry+1 , currentCountry.getArea()+"", Color.WHITE, Typeface.NORMAL, Color.BLUE));
                tableL.addView(tr, getTblLayoutParams());

                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                        String countryName = currentCountry.getName();
                        String countryNativeName = currentCountry.getNativeName();
                        String[] countryBorders = currentCountry.getBorders();
                        intent.putExtra("countryName", countryName);
                        intent.putExtra("countryNativeName", countryNativeName);
                        intent.putExtra("countryBorders", countryBorders);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void getJson() {
        addHeaders();
        OkHttpClient client = new OkHttpClient();
        String url = "https://restcountries.eu/rest/v2/all";
        Request request = new Request.Builder().url(url).build();

         client.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 e.printStackTrace();
                 Log.i("TAG",e.toString());
             }

             @Override
             public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<Country> countries = null;
                            Log.i("TAG",myResponse);
                            countries = convertFromJson(myResponse);
                            addData(myResponse);

                        }
                    });
                }
             }
         });

    }

    private List<Country> convertFromJson(String json){
            Gson gson = new Gson();
            List<Country> countres = null;
            Country[] arrCountry = gson.fromJson(json, Country[].class);

            if(arrCountry != null){
                countres = Arrays.asList(arrCountry);
            }
            return countres;
        }

    public static class Country {
            private String name = null;
            private String[] topLevelDomain = null;
            private String alpha2Code = null;
            private String alpha3Code = null;
            private String[] callingCodes = null;
            private String capital = null;
            private String[] altSpellings = null;
            private String region = null;
            private String subregion = null;
            private Integer population = null;
            private String[] latlng = null;
            private String demonym = null;
            private Double area = null;
            private Double gini = null;
            private String[] timezones = null;
            private String[] borders = null;
            private String nativeName = null;
            private String numericCode = null;
            private Currencie[] currencies = null;
            private Languages[] languages = null;
            private Translations transients = null;
            private String flag = null;
            private RegionalBlocs[] regionalBlocs = null;
            private String cioc = null;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String[] getTopLevelDomain() {
                return topLevelDomain;
            }

            public void setTopLevelDomain(String[] topLevelDomain) {
                this.topLevelDomain = topLevelDomain;
            }

            public String getAlpha2Code() {
                return alpha2Code;
            }

            public void setAlpha2Code(String alpha2Code) {
                this.alpha2Code = alpha2Code;
            }

            public String getAlpha3Code() {
                return alpha3Code;
            }

            public void setAlpha3Code(String alpha3Code) {
                this.alpha3Code = alpha3Code;
            }

            public String[] getCallingCodes() {
                return callingCodes;
            }

            public void setCallingCodes(String[] callingCodes) {
                this.callingCodes = callingCodes;
            }

            public String getCapital() {
                return capital;
            }

            public void setCapital(String capital) {
                this.capital = capital;
            }

            public String[] getAltSpellings() {
                return altSpellings;
            }

            public void setAltSpellings(String[] altSpellings) {
                this.altSpellings = altSpellings;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getSubregion() {
                return subregion;
            }

            public void setSubregion(String subregion) {
                this.subregion = subregion;
            }

            public String getDemonym() {
                return demonym;
            }

            public void setDemonym(String demonym) {
                this.demonym = demonym;
            }

            public String[] getBorders() {
                return borders;
            }

            public void setBorders(String[] borders) {
                this.borders = borders;
            }

            public String getNativeName() {
                return nativeName;
            }

            public void setNativeName(String nativeName) {
                this.nativeName = nativeName;
            }

            public String getNumericCode() {
                return numericCode;
            }

            public void setNumericCode(String numericCode) {
                this.numericCode = numericCode;
            }

            public Currencie[] getCurrencies() {
                return currencies;
            }

            public void setCurrencies(Currencie[] currencies) {
                this.currencies = currencies;
            }

            public Languages[] getLanguages() {
                return languages;
            }

            public void setLanguages(Languages[] languages) {
                this.languages = languages;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }

            public String getCioc() {
                return cioc;
            }

            public void setCioc(String cioc) {
                this.cioc = cioc;
            }

            public RegionalBlocs[] getRegionalBlocs() {
                return regionalBlocs;
            }

            public void setRegionalBlocs(RegionalBlocs[] regionalBlocs) {
                this.regionalBlocs = regionalBlocs;
            }

            public String[] getTimezones() {
                return timezones;
            }

            public void setTimezones(String[] timezones) {
                this.timezones = timezones;
            }

            public Translations getTransients() {
                return transients;
            }

            public void setTransients(Translations transients) {
                this.transients = transients;
            }

            public String[] getLatlng() {
                return latlng;
            }

            public void setLatlng(String[] latlng) {
                this.latlng = latlng;
            }

            public Integer getPopulation() {
                return population;
            }

            public void setPopulation(Integer population) {
                this.population = population;
            }

            public Double getArea() {
                return area;
            }

            public void setArea(Double area) {
                this.area = area;
            }

            public Double getGini() {
                return gini;
            }

            public void setGini(Double gini) {
                this.gini = gini;
            }
        }
    class Currencie {
            private String code = null;
            private String name = null;
            private String symbol = null;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }
        }
    class Languages {
            private String iso639_1 = null;
            private String iso639_2 = null;
            private String name = null;
            private String nativeName = null;

            public String getIso639_1() {
                return iso639_1;
            }

            public void setIso639_1(String iso639_1) {
                this.iso639_1 = iso639_1;
            }

            public String getIso639_2() {
                return iso639_2;
            }

            public void setIso639_2(String iso639_2) {
                this.iso639_2 = iso639_2;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNativeName() {
                return nativeName;
            }

            public void setNativeName(String nativeName) {
                this.nativeName = nativeName;
            }
        }
    class Translations {
            private String de = null;
            private String es = null;
            private String fr = null;
            private String ja = null;
            private String it = null;
            private String br = null;
            private String pt = null;
            private String nl = null;
            private String hr = null;
            private String fa = null;

            public String getDe() {
                return de;
            }

            public void setDe(String de) {
                this.de = de;
            }

            public String getEs() {
                return es;
            }

            public void setEs(String es) {
                this.es = es;
            }

            public String getFr() {
                return fr;
            }

            public void setFr(String fr) {
                this.fr = fr;
            }

            public String getJa() {
                return ja;
            }

            public void setJa(String ja) {
                this.ja = ja;
            }

            public String getIt() {
                return it;
            }

            public void setIt(String it) {
                this.it = it;
            }

            public String getBr() {
                return br;
            }

            public void setBr(String br) {
                this.br = br;
            }

            public String getPt() {
                return pt;
            }

            public void setPt(String pt) {
                this.pt = pt;
            }

            public String getNl() {
                return nl;
            }

            public void setNl(String nl) {
                this.nl = nl;
            }

            public String getHr() {
                return hr;
            }

            public void setHr(String hr) {
                this.hr = hr;
            }

            public String getFa() {
                return fa;
            }

            public void setFa(String fa) {
                this.fa = fa;
            }
        }
    class RegionalBlocs {
            private String acronym = null;
            private String name = null;
            private String[] otherAcronyms = null;
            private String[] otherNames = null;

            public String getAcronym() {
                return acronym;
            }

            public void setAcronym(String acronym) {
                this.acronym = acronym;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String[] getOtherAcronyms() {
                return otherAcronyms;
            }

            public void setOtherAcronyms(String[] otherAcronyms) {
                this.otherAcronyms = otherAcronyms;
            }

            public String[] getOtherNames() {
                return otherNames;
            }

            public void setOtherNames(String[] otherNames) {
                this.otherNames = otherNames;
            }
        }
}
