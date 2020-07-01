package com.example.expensestracker.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.example.expensestracker.R;
import com.example.expensestracker.dialogBox.ThemeDialog;

public class SettingsActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        this.mViewHolder.chooseTheme = findViewById(R.id.area_theme);
        this.mViewHolder.txtThemeOption = findViewById(R.id.txt_theme_option);

        this.mViewHolder.chooseCurrency = findViewById(R.id.area_currency);
        this.mViewHolder.txtCurrencyOption = findViewById(R.id.txt_currency_option);

        this.mViewHolder.chooseTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opens dialog box to choose a theme
                DialogFragment newFragment = new ThemeDialog();
                newFragment.show(getSupportFragmentManager(), "theme");
            }
        });
        this.mViewHolder.chooseCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChooseCurrencyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set current options to View

        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        String cur = prefs.getString("currency", getResources().getString(R.string.currency));

        switch (cur) {
            case "$": cur = "Dollar ($)"; break;
            case "€": cur = "Euro (€)"; break;
            case "R$": cur = "Real (R$)"; break;
        }

        this.mViewHolder.txtCurrencyOption.setText(cur);

        int idTheme = prefs.getInt("theme", 0);
        String[] themestext = getResources().getStringArray(R.array.arr_theme_options);
        this.mViewHolder.txtThemeOption.setText(themestext[idTheme]);
    }

    /**
     * Checks the chosen theme.
     *
     * @return Nothing.
     */
    public void checkTheme() {
        switch (getSharedPreferences("prefs", Context.MODE_PRIVATE).getInt("theme", 0)) {
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    private static class ViewHolder {
        LinearLayout chooseTheme;
        TextView txtThemeOption;
        LinearLayout chooseCurrency;
        TextView txtCurrencyOption;
    }
}
