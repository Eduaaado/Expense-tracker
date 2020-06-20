package com.example.expensestracker.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.expensestracker.R;
import com.example.expensestracker.data.Database;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NewTransactionActivity extends AppCompatActivity {

    Database mDatabase = new Database(this);
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_transaction);

        this.mViewHolder.editTransTitle = findViewById(R.id.edit_transaction_title);
        this.mViewHolder.editTransAmount = findViewById(R.id.edit_transaction_amount);
        this.mViewHolder.spinTransType = findViewById(R.id.spin_transaction_type);
        this.mViewHolder.btnTransConfirm = findViewById(R.id.btn_transaction_confirm);

        this.mViewHolder.editTransAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals(current)){
                    mViewHolder.editTransAmount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
                    symbols.setCurrencySymbol("");
                    formatter.setDecimalFormatSymbols(symbols);

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = formatter.format((parsed/100));

                    current = formatted;
                    mViewHolder.editTransAmount.setText(formatted);
                    mViewHolder.editTransAmount.setSelection(formatted.length());

                    mViewHolder.editTransAmount.addTextChangedListener(this);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        this.mViewHolder.btnTransConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mViewHolder.editTransTitle.getText().toString();
                String amount = mViewHolder.editTransAmount.getText().toString();
                String type = String.valueOf(mViewHolder.spinTransType.getSelectedItemId());

                mDatabase.addEntry(title, amount, type);

                float add = Float.parseFloat(amount.replace(",",""));
                if (Integer.parseInt(type) == 0) {
                    add *= -1;
                }
                mDatabase.increaseBudget(add);

                finish();
            }
        });

    }

    private static class ViewHolder {
        EditText editTransTitle;
        EditText editTransAmount;
        Spinner spinTransType;
        Button  btnTransConfirm;
    }
}
