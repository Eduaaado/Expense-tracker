package com.example.expensestracker.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.expensestracker.R;

public class NewTransactionActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_transaction);

        this.mViewHolder.editTransTitle = findViewById(R.id.edit_transaction_title);
        this.mViewHolder.editTransAmount = findViewById(R.id.edit_transaction_amount);
        this.mViewHolder.spinTransType = findViewById(R.id.spin_transaction_type);
        this.mViewHolder.btnTransConfirm = findViewById(R.id.btn_transaction_confirm);

        this.mViewHolder.btnTransConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create Entry

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
