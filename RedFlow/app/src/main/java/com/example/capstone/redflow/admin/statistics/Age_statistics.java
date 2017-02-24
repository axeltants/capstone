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
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class Age_statistics extends DemoBase implements
        OnChartValueSelectedListener {

    private PieChart mChart;

    private Typeface tf;

    private Firebase mRootRef;
    private Query query;
    private ChildEventListener listener;

    private int genin;  //16-21
    private int chuunin;    //22-27
    private int jounin; //28-33
    private int anbu;   //34-39
    private int chubu;  //40-45
    private int kage;   //46-51
    private int sclass; //52-60
    private int root;  //60+

    private TextView Vgenin;
    private TextView Vchuunin;
    private TextView Vjounin;
    private TextView Vanbu;
    private TextView Vchubu;
    private TextView Vkage;
    private TextView Vsclass;
    private TextView Vroot;

    private LinearLayout Lgenin;
    private LinearLayout Lchuunin;
    private LinearLayout Ljounin;
    private LinearLayout Lanbu;
    private LinearLayout Lchubu;
    private LinearLayout Lkage;
    private LinearLayout Lsclass;
    private LinearLayout Lroot;

    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.age_statistics);

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

        Vgenin = (TextView) findViewById(R.id.genin);
        Vchuunin = (TextView) findViewById(R.id.chuunin);
        Vjounin = (TextView) findViewById(R.id.jounin);
        Vanbu = (TextView) findViewById(R.id.anbu);
        Vchubu = (TextView) findViewById(R.id.chubu);
        Vkage = (TextView) findViewById(R.id.kage);
        Vsclass = (TextView) findViewById(R.id.sclass);
        Vroot = (TextView) findViewById(R.id.root);

        Lgenin = (LinearLayout) findViewById(R.id.gnin);
        Lchuunin = (LinearLayout) findViewById(R.id.chnin);
        Ljounin = (LinearLayout) findViewById(R.id.jnin);
        Lanbu = (LinearLayout) findViewById(R.id.nbu);
        Lchubu = (LinearLayout) findViewById(R.id.chbu);
        Lkage = (LinearLayout) findViewById(R.id.kge);
        Lsclass = (LinearLayout) findViewById(R.id.sclss);
        Lroot = (LinearLayout) findViewById(R.id.rt);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }

    @Override
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

        genin = 0;  //16-21
        chuunin = 0;    //22-27
        jounin = 0; //28-33
        anbu = 0;   //34-39
        chubu = 0;  //40-45
        kage = 0;   //46-51
        sclass = 0; //52-60
        root = 0;  //60+

        query = mRootRef.child("User");

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Integer> map = dataSnapshot.getValue(Map.class);

                switch(map.get("birthyear")) {
                    case 2000:
                    case 1999:
                    case 1998:
                    case 1997:
                    case 1996:
                    case 1995:  genin++;
                                break;
                    case 1994:
                    case 1993:
                    case 1992:
                    case 1991:
                    case 1990:
                    case 1989:  chuunin++;
                                break;
                    case 1988:
                    case 1987:
                    case 1986:
                    case 1985:
                    case 1984:
                    case 1983:  jounin++;
                                break;
                    case 1982:
                    case 1981:
                    case 1980:
                    case 1979:
                    case 1978:
                    case 1977:  anbu++;
                                break;
                    case 1976:
                    case 1975:
                    case 1974:
                    case 1973:
                    case 1972:
                    case 1971:  chubu++;
                                break;
                    case 1970:
                    case 1969:
                    case 1968:
                    case 1967:
                    case 1966:
                    case 1965:  kage++;
                                break;
                    case 1964:
                    case 1963:
                    case 1962:
                    case 1961:
                    case 1960:
                    case 1959:
                    case 1958:
                    case 1957:
                    case 1956:  sclass++;
                                break;

                    default:    root++;
                }

                Vgenin.setText(String.valueOf(genin));
                Vchuunin.setText(String.valueOf(chuunin));
                Vjounin.setText(String.valueOf(jounin));
                Vanbu.setText(String.valueOf(anbu));
                Vchubu.setText(String.valueOf(chubu));
                Vkage.setText(String.valueOf(kage));
                Vsclass.setText(String.valueOf(sclass));
                Vroot.setText(String.valueOf(root));
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
                if(genin > 0) {
                    entries.add(new PieEntry(genin, "Ages 16-21"));
                }
                if(chuunin > 0) {
                    entries.add(new PieEntry(chuunin, "Ages 22-27"));
                }
                if(jounin > 0) {
                    entries.add(new PieEntry(jounin, "Ages 28-33"));
                }
                if(anbu > 0) {
                    entries.add(new PieEntry(anbu, "Ages 34-39"));
                }
                if(chubu > 0) {
                    entries.add(new PieEntry(chubu, "Ages 40-45"));
                }
                if(kage > 0) {
                    entries.add(new PieEntry(kage, "Ages 46-51"));
                }
                if(sclass > 0) {
                    entries.add(new PieEntry(sclass, "Ages 52-60"));
                }
                if(root > 0) {
                    entries.add(new PieEntry(root, "Ages 60+"));
                }

                if(genin == 0){
                    Lgenin.setVisibility(View.GONE);
                }
                if(chuunin == 0){
                    Lchuunin.setVisibility(View.GONE);
                }
                if(jounin == 0){
                    Ljounin.setVisibility(View.GONE);
                }
                if(anbu == 0){
                    Lanbu.setVisibility(View.GONE);
                }
                if(chubu == 0){
                    Lchubu.setVisibility(View.GONE);
                }
                if(kage == 0){
                    Lkage.setVisibility(View.GONE);
                }
                if(sclass == 0){
                    Lsclass.setVisibility(View.GONE);
                }
                if(root == 0){
                    Lroot.setVisibility(View.GONE);
                }

                PieDataSet dataSet = new PieDataSet(entries, "Age");
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
