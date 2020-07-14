package com.example.expensestracker.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensestracker.R;

public class ChooseCurrencyActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_currency);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.mViewHolder.txtDollar = findViewById(R.id.currency_option_dollar);
        this.mViewHolder.txtEuro = findViewById(R.id.currency_option_euro);
        this.mViewHolder.txtReal = findViewById(R.id.currency_option_real);

        this.mViewHolder.txtDollar.setOnClickListener(this);
        this.mViewHolder.txtEuro.setOnClickListener(this);
        this.mViewHolder.txtReal.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currency_option_dollar:
                ChangeCurrency("$");
                break;
            case R.id.currency_option_euro:
                ChangeCurrency("€");
                break;
            case R.id.currency_option_real:
                ChangeCurrency("R$");
                break;
        }
    }

    private void ChangeCurrency(String currency) {
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("currency", currency);
        editor.apply();

        finish();
    }

    private static class ViewHolder {
        TextView txtDollar;
        TextView txtEuro;
        TextView txtReal;
    }
}
