package com.example.expensestracker.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensestracker.R;
import com.example.expensestracker.data.Database;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTransactionActivity extends AppCompatActivity {

    Database mDatabase = new Database(this);
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.mViewHolder.editTransTitle = findViewById(R.id.edit_transaction_title);
        this.mViewHolder.editTransAmount = findViewById(R.id.edit_transaction_amount);
        this.mViewHolder.txtCurrency = findViewById(R.id.txt_transaction_currency);
        this.mViewHolder.spinTransType = findViewById(R.id.spin_transaction_type);
        this.mViewHolder.editTransDate = findViewById(R.id.edit_transaction_date);
        this.mViewHolder.editTransTime = findViewById(R.id.edit_transaction_time);
        this.mViewHolder.btnTransConfirm = findViewById(R.id.btn_transaction_confirm);

        // Format Edit area to money format ($X.yy)
        this.mViewHolder.editTransAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals(current)){
                    // Remove listener to avoid infinite loop
                    mViewHolder.editTransAmount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    // Sets formatter
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

                    // Remove Currency symbol
                    symbols.setCurrencySymbol("");
                    formatter.setDecimalFormatSymbols(symbols);

                    // Formats string
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = formatter.format((parsed/100));

                    // Set text to line
                    current = formatted;
                    mViewHolder.editTransAmount.setText(formatted);
                    mViewHolder.editTransAmount.setSelection(formatted.length());

                    // Put listener back
                    mViewHolder.editTransAmount.addTextChangedListener(this);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        this.mViewHolder.editTransDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // Date picker
                new DatePickerDialog(NewTransactionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mViewHolder.editTransDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            }
                        }, year, month, day).show();
            }
        });

        this.mViewHolder.editTransTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR);
                int minute = cldr.get(Calendar.MINUTE);

                new TimePickerDialog(NewTransactionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                        String time = null;
                        try {
                            Date d = timeformat.parse(selectedHour+":"+selectedMinute);
                            time = timeformat.format(d);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mViewHolder.editTransTime.setText(time);
                    }
                }, hour, minute, true).show();
            }
        });

        this.mViewHolder.btnTransConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get inputs
                String title = mViewHolder.editTransTitle.getText().toString();
                String amount = mViewHolder.editTransAmount.getText().toString();
                String type = String.valueOf(mViewHolder.spinTransType.getSelectedItemId());
                String date = mViewHolder.editTransDate.getText().toString();
                String time = mViewHolder.editTransTime.getText().toString();

                if (title.matches("")) {
                    Toast.makeText(NewTransactionActivity.this, R.string.fill_title, Toast.LENGTH_SHORT).show();
                    return;
                } else if (amount.matches("") || amount.matches("0.00")) {
                    Toast.makeText(NewTransactionActivity.this, R.string.fill_amount, Toast.LENGTH_SHORT).show();
                    return;
                }

                Calendar c = Calendar.getInstance();
                if (date.equals("")) {
                    date = c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
                }
                if (time.equals("")) {
                    time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                }
                SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm d/M/yyyy");
                SimpleDateFormat target = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
                try {
                    Date d = originalFormat.parse(time+" "+date);
                    time = target.format(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Log new transaction
                mDatabase.addEntry(title, amount, type, time);

                // Remove any unnecessary char
                float add = Float.parseFloat(amount.replace(",",""));

                // Checks if it's expense or income
                if (Integer.parseInt(type) == 0) {
                    add *= -1;
                }

                // Updates budget
                mDatabase.increaseBudget(add);

                // Exit transactions activity
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        this.mViewHolder.txtCurrency.setText(prefs.getString("currency", getResources().getString(R.string.currency)));
    }

    private static class ViewHolder {
        EditText editTransTitle;
        EditText editTransAmount;
        TextView txtCurrency;
        Spinner spinTransType;
        EditText editTransDate;
        EditText editTransTime;
        Button btnTransConfirm;
    }
}
