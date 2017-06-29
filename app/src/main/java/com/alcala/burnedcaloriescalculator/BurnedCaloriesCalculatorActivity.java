package com.alcala.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.DecimalFormat;

public class BurnedCaloriesCalculatorActivity extends AppCompatActivity {

    private EditText mWeightET;
    private TextView mMilesRanTV;
    private TextView mCaloriesBurnedTV;
    private TextView mBmiTV;
    private EditText mNameET;
    private SeekBar mMilesSeekBar;
    private Spinner mFootSpinner;
    private Spinner mInchesSpinner;

    private String weightString = "";
    private float feet = 0;
    private float inches = 0;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        mWeightET = (EditText) findViewById(R.id.weightET);
        mMilesRanTV = (TextView) findViewById(R.id.milesRanTV);
        mCaloriesBurnedTV = (TextView) findViewById(R.id.caloriesBurnedTV);
        mBmiTV = (TextView) findViewById(R.id.bmiTV);
        mNameET = (EditText) findViewById(R.id.nameET);
        mMilesSeekBar = (SeekBar) findViewById(R.id.milesSeekBar);
        mFootSpinner = (Spinner) findViewById(R.id.footSpinner);
        mInchesSpinner = (Spinner) findViewById(R.id.inchesSpinner);

        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource(this, R.array.feetArray,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> inchesAdapter = ArrayAdapter.createFromResource(this, R.array.inchesArray,
                android.R.layout.simple_spinner_item);
        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFootSpinner.setAdapter(feetAdapter);
        mInchesSpinner.setAdapter(inchesAdapter);

        mNameET.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                }
                return false;
            }
        });

        mWeightET.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    calculateAndDisplay();
                }
                return false;
            }
        });

        mInchesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //inches = mInchesSpinner.getSelectedItemPosition()+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mFootSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              // feet = mFootSpinner.getSelectedItemPosition()+1;
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        mMilesSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMilesRanTV.setText(progress + "mi");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        savedValues = getSharedPreferences("SavedValues",MODE_PRIVATE);
    }

    private void calculateAndDisplay() {
        weightString = mWeightET.getText().toString();
        float weightValue;
        if(weightString.equals("")) {
            weightValue = 0;
        }
        else
            weightValue = Float.parseFloat(weightString);

        feet = mFootSpinner.getSelectedItemPosition()+1;
        inches = mInchesSpinner.getSelectedItemPosition()+1;

        int milesRan = mMilesSeekBar.getProgress();
        float caloriesBurned = (float) (0.75*weightValue*milesRan);
        float bmi = ((weightValue*703)/((12*feet+inches)*(12*feet+inches)));

        DecimalFormat df = new DecimalFormat("###,###.##");
        mCaloriesBurnedTV.setText(df.format(caloriesBurned)+" cal");
        mBmiTV.setText(df.format(bmi));
    }



    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("weightString", weightString);

        editor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        weightString = savedValues.getString("weightString", "");
        mWeightET.setText(weightString);
        calculateAndDisplay();
    }
}
