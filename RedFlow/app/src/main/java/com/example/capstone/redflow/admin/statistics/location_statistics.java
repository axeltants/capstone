package com.example.capstone.redflow.admin.statistics;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

import com.example.capstone.redflow.R;
import com.example.capstone.redflow.notimportant.DemoBase;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Map;

public class location_statistics extends DemoBase implements
        OnChartValueSelectedListener {

    private PieChart mChart;

    private Typeface tf;

    private Firebase mRootRef;
    private Query query;
    private ChildEventListener listener;

    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

    private int AGUSAN;
    private int AKLAN;
    private int ANTIQUE;
    private int BATANGAS;
    private int BENGUET;
    private int BOHOL;
    private int BUKIDNON;
    private int BULACAN;
    private int BAGUIO;
    private int CALOOCAN;
    private int CAMARINES;
    private int CAPIZ;
    private int CATANDUANES;
    private int CAVITE;
    private int COTABATO;
    private int CAGAYAN;
    private int CEBU;
    private int DAVAO;
    private int GENERAL_SANTOS;
    private int GINGOOG;
    private int GUIMARAS;
    private int ILIGAN;
    private int ILOCOS;
    private int ISABELA;
    private int ILOILO;
    private int LA_UNION;
    private int LAGUNA;
    private int LEYTE;
    private int MASBATE;
    private int MANDALUYONG;
    private int MANILA;
    private int NEGROS;
    private int NUEVA_ECIJA;
    private int NUEVA_VIZCAYA;
    private int OLONGAPO;
    private int ORMOC;
    private int PALAWAN;
    private int PAMPANGA;
    private int PANGASINAN;
    private int PASAY;
    private int QUEZON;
    private int QUIRINO;
    private int RIZAL;
    private int SAN_PABLO;
    private int SULU;
    private int SURIGAO;
    private int TANGUB;
    private int TARLAC;
    private int VALENZUELA;
    private int ZAMBALES;
    private int ZAMBOANGA;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.location_statistics);

        mRootRef = new Firebase("https://redflow-22917.firebaseio.com/");

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(4, 100);

        //mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);
        mChart.animateXY(1400, 1400);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IDataSet<?> set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHole: {
                if (mChart.isDrawHoleEnabled())
                    mChart.setDrawHoleEnabled(false);
                else
                    mChart.setDrawHoleEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionDrawCenter: {
                if (mChart.isDrawCenterTextEnabled())
                    mChart.setDrawCenterText(false);
                else
                    mChart.setDrawCenterText(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleXVals: {

                mChart.setDrawEntryLabels(!mChart.isDrawEntryLabelsEnabled());
                mChart.invalidate();
                break;
            }
            case R.id.actionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
                mChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.actionTogglePercent:
                mChart.setUsePercentValues(!mChart.isUsePercentValuesEnabled());
                mChart.invalidate();
                break;
            case R.id.animateX: {
                mChart.animateX(1400);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(1400);
                break;
            }
            case R.id.animateXY: {
                mChart.animateXY(1400, 1400);
                break;
            }
        }
        return true;
    }*/



    private void setData(int count, float range) {

        float mult = range;

        AGUSAN = 0;
        AKLAN = 0;
        ANTIQUE = 0;
        BATANGAS = 0;
        BENGUET = 0;
        BOHOL = 0;
        BUKIDNON = 0;
        BULACAN = 0;
        BAGUIO = 0;
        CALOOCAN = 0;
        CAMARINES = 0;
        CAPIZ = 0;
        CATANDUANES = 0;
        CAVITE = 0;
        COTABATO = 0;
        CAGAYAN = 0;
        CEBU = 0;
        DAVAO = 0;
        GENERAL_SANTOS = 0;
        GINGOOG = 0;
        GUIMARAS = 0;
        ILIGAN = 0;
        ILOCOS = 0;
        ISABELA = 0;
        ILOILO = 0;
        LA_UNION = 0;
        LAGUNA = 0;
        LEYTE = 0;
        MASBATE = 0;
        MANDALUYONG = 0;
        MANILA = 0;
        NEGROS = 0;
        NUEVA_ECIJA = 0;
        NUEVA_VIZCAYA = 0;
        OLONGAPO = 0;
        ORMOC = 0;
        PALAWAN = 0;
        PAMPANGA = 0;
        PANGASINAN = 0;
        PASAY = 0;
        QUEZON = 0;
        QUIRINO = 0;
        RIZAL = 0;
        SAN_PABLO = 0;
        SULU = 0;
        SURIGAO = 0;
        TANGUB = 0;
        TARLAC = 0;
        VALENZUELA = 0;
        ZAMBALES = 0;
        ZAMBOANGA = 0;

        query = mRootRef.child("User");

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                switch(map.get("province").toUpperCase()) {

                    case "AGUSAN":  AGUSAN++;
                                    break;

                    case "AKLAN":   AKLAN++;
                                    break;

                    case "ANTIQUE": ANTIQUE++;
                                    break;

                    case "BATANGAS":    BATANGAS++;
                                        break;

                    case "BENGUET": BENGUET++;
                                    break;

                    case "BOHOL":   BOHOL++;
                                    break;

                    case "BUKIDNON":    BUKIDNON++;
                                        break;

                    case "BULACAN": BULACAN++;
                                    break;

                    case "BAGUIO":  BAGUIO++;
                                    break;

                    case "CALOOCAN":    CALOOCAN++;
                                        break;

                    case "CAMARINES":   CAMARINES++;
                                        break;

                    case "CAPIZ":   CAPIZ++;
                                    break;

                    case "CATANDUANES": CATANDUANES++;
                                        break;

                    case "CAVITE":  CAVITE++;
                                    break;

                    case "COTABATO":    COTABATO++;
                                        break;

                    case "CAGAYAN":     CAGAYAN++;
                                        break;

                    case "DAVAO":   DAVAO++;
                                    break;

                    case "GENERAL_SANTOS":  GENERAL_SANTOS++;
                                            break;

                    case "GINGOOG": GINGOOG++;
                                    break;

                    case "GUIMARAS":    GUIMARAS++;
                                        break;

                    case "ILIGAN":  ILIGAN++;
                                    break;

                    case "ILOCOS":  ILOCOS++;
                                    break;

                    case "ISABELA": ISABELA++;
                                    break;

                    case "ILOILO":  ILOILO++;
                                    break;

                    case "LA_UNION":    LA_UNION++;
                                        break;

                    case "LAGUNA":  LAGUNA++;
                                    break;

                    case "LEYTE":   LEYTE++;
                                    break;

                    case "MASBATE": MASBATE++;
                                    break;

                    case "MANDALUYONG":     MANDALUYONG++;
                                            break;

                    case "MANILA":  MANILA++;
                                    break;

                    case "NEGROS":  NEGROS++;
                                    break;

                    case "NUEVA_ECIJA": NUEVA_ECIJA++;
                                        break;

                    case "NUEVA_VIZCAYA":   NUEVA_VIZCAYA++;
                                            break;

                    case "OLONGAPO":    OLONGAPO++;
                                        break;

                    case "ORMOC":   ORMOC++;
                                    break;

                    case "PALAWAN": PALAWAN++;
                                    break;

                    case "PAMPANGA":    PAMPANGA++;
                                        break;

                    case "PANGASINAN":  PANGASINAN++;
                                        break;

                    case "PASAY":   PASAY++;
                                    break;

                    case "QUEZON":  QUEZON++;
                                    break;

                    case "QUIRINO":     QUIRINO++;
                                        break;

                    case "RIZAL":   RIZAL++;
                                    break;

                    case "SAN_PABLO":   SAN_PABLO++;
                                        break;

                    case "SULU":    SULU++;
                                    break;

                    case "SURIGAO": SURIGAO++;
                                    break;

                    case "TANGUB":  TANGUB++;
                                    break;

                    case "TARLAC":  TARLAC++;
                                    break;

                    case "VALENZUELA":  VALENZUELA++;
                                        break;

                    case "ZAMBALES":    ZAMBALES++;
                                        break;

                    case "ZAMBOANGA":   ZAMBOANGA++;
                                        break;

                    default: CEBU++;
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(AGUSAN > 0) {
                    entries.add(new PieEntry(AGUSAN, "AGUSAN"));
                }
                if(AKLAN > 0) {
                    entries.add(new PieEntry(AKLAN, "AKLAN"));
                }
                if(ANTIQUE > 0) {
                    entries.add(new PieEntry(ANTIQUE, "ANTIQUE"));
                }
                if(BATANGAS > 0) {
                    entries.add(new PieEntry(BATANGAS, "BATANGAS"));
                }
                if(BENGUET > 0) {
                    entries.add(new PieEntry(BENGUET, "BENGUET"));
                }
                if(BOHOL > 0) {
                    entries.add(new PieEntry(BOHOL, "BOHOL"));
                }
                if(BUKIDNON > 0) {
                    entries.add(new PieEntry(BUKIDNON, "BUKIDNON"));
                }
                if(BULACAN > 0) {
                    entries.add(new PieEntry(BULACAN, "BULACAN"));
                }
                if(BAGUIO > 0) {
                    entries.add(new PieEntry(BAGUIO, "BAGUIO"));
                }
                if(CALOOCAN > 0) {
                    entries.add(new PieEntry(CALOOCAN, "CALOOCAN"));
                }
                if(CAMARINES > 0) {
                    entries.add(new PieEntry(CAMARINES, "CAMARINES"));
                }
                if(CAPIZ > 0) {
                    entries.add(new PieEntry(CAPIZ, "CAPIZ"));
                }
                if(CATANDUANES > 0) {
                    entries.add(new PieEntry(CATANDUANES, "CATANDUANES"));
                }
                if(CAVITE > 0) {
                    entries.add(new PieEntry(CAVITE, "CAVITE"));
                }
                if(COTABATO > 0) {
                    entries.add(new PieEntry(COTABATO, "COTABATO"));
                }
                if(CAGAYAN > 0) {
                    entries.add(new PieEntry(CAGAYAN, "CAGAYAN"));
                }
                if(CEBU > 0) {
                    entries.add(new PieEntry(CEBU, "Cebu"));
                }
                if(DAVAO > 0) {
                    entries.add(new PieEntry(DAVAO, "DAVAO"));
                }
                if(GENERAL_SANTOS > 0) {
                    entries.add(new PieEntry(GENERAL_SANTOS, "GENERAL SANTOS"));
                }
                if(GINGOOG > 0) {
                    entries.add(new PieEntry(GINGOOG, "GINGOOG"));
                }
                if(GUIMARAS > 0) {
                    entries.add(new PieEntry(GUIMARAS, "GUIMARAS"));
                }
                if(ILIGAN > 0) {
                    entries.add(new PieEntry(ILIGAN, "ILIGAN"));
                }
                if(ILOCOS > 0) {
                    entries.add(new PieEntry(ILOCOS, "ILOCOS"));
                }
                if(ISABELA > 0) {
                    entries.add(new PieEntry(ISABELA, "ISABELA"));
                }
                if(ILOILO > 0) {
                    entries.add(new PieEntry(ILOILO, "ILOILO"));
                }
                if(LA_UNION > 0) {
                    entries.add(new PieEntry(LA_UNION, "LA UNION"));
                }
                if(LAGUNA > 0) {
                    entries.add(new PieEntry(LAGUNA, "LAGUNA"));
                }
                if(LEYTE > 0) {
                    entries.add(new PieEntry(LEYTE, "LEYTE"));
                }
                if(MASBATE > 0) {
                    entries.add(new PieEntry(MASBATE, "MASBATE"));
                }
                if(MANDALUYONG > 0) {
                    entries.add(new PieEntry(MANDALUYONG, "MANDALUYONG"));
                }
                if(MANILA > 0) {
                    entries.add(new PieEntry(MANILA, "MANILA"));
                }
                if(NEGROS > 0) {
                    entries.add(new PieEntry(NEGROS, "NEGROS"));
                }
                if(NUEVA_ECIJA > 0) {
                    entries.add(new PieEntry(NUEVA_ECIJA, "NUEVA ECIJA"));
                }
                if(NUEVA_VIZCAYA > 0) {
                    entries.add(new PieEntry(NUEVA_VIZCAYA, "NUEVA VIZCAYA"));
                }
                if(OLONGAPO > 0) {
                    entries.add(new PieEntry(OLONGAPO, "OLONGAPO"));
                }
                if(ORMOC > 0) {
                    entries.add(new PieEntry(ORMOC, "ORMOC"));
                }
                if(PALAWAN > 0) {
                    entries.add(new PieEntry(PALAWAN, "PALAWAN"));
                }
                if(PAMPANGA > 0) {
                    entries.add(new PieEntry(PAMPANGA, "PAMPANGA"));
                }
                if(PANGASINAN > 0) {
                    entries.add(new PieEntry(PANGASINAN, "PANGASINAN"));
                }
                if(PASAY > 0) {
                    entries.add(new PieEntry(PASAY, "PASAY"));
                }
                if(QUEZON > 0) {
                    entries.add(new PieEntry(QUEZON, "QUEZON"));
                }
                if(QUIRINO > 0) {
                    entries.add(new PieEntry(QUIRINO, "QUIRINO"));
                }
                if(RIZAL > 0) {
                    entries.add(new PieEntry(RIZAL, "RIZAL"));
                }
                if(SAN_PABLO > 0) {
                    entries.add(new PieEntry(SAN_PABLO, "SAN_PABLO"));
                }
                if(SULU > 0) {
                    entries.add(new PieEntry(SULU, "SULU"));
                }
                if(SURIGAO > 0) {
                    entries.add(new PieEntry(SURIGAO, "SURIGAO"));
                }
                if(TANGUB > 0) {
                    entries.add(new PieEntry(TANGUB, "TANGUB"));
                }
                if(TARLAC > 0) {
                    entries.add(new PieEntry(TARLAC, "TARLAC"));
                }
                if(VALENZUELA > 0) {
                    entries.add(new PieEntry(VALENZUELA, "VALENZUELA"));
                }
                if(ZAMBALES > 0) {
                    entries.add(new PieEntry(ZAMBALES, "ZAMBALES"));
                }
                if(ZAMBOANGA > 0) {
                    entries.add(new PieEntry(ZAMBOANGA, "ZAMBOANGA"));
                }

                PieDataSet dataSet = new PieDataSet(entries, "Location");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);

                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (int c : ColorTemplate.MATERIAL_COLORS)
                    colors.add(c);


                colors.add(ColorTemplate.getHoloBlue());

                dataSet.setColors(colors);
                //dataSet.setSelectionShift(0f);

                dataSet.setValueLinePart1OffsetPercentage(80.f);
                dataSet.setValueLinePart1Length(0.2f);
                dataSet.setValueLinePart2Length(0.4f);
                //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.BLACK);
                data.setValueTypeface(tf);
                mChart.setData(data);

                // undo all highlights
                mChart.highlightValues(null);

                mChart.invalidate();

                query.removeEventListener(listener);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        query.addChildEventListener(listener);
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        /*for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) (Math.random() * mult) + mult / 5, mParties[i % mParties.length]));
        }*/


    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("RedFlow\nData Analytics");
        s.setSpan(new RelativeSizeSpan(1.8f), 0, 8, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 8, s.length() - 4, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length() - 4, 0);
        s.setSpan(new RelativeSizeSpan(.65f), 8, s.length() - 14, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length(), s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
