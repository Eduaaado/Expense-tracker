package com.example.expensestracker.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.expensestracker.R;
import com.example.expensestracker.data.Database;

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

        this.mViewHolder.btnTransConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mViewHolder.editTransTitle.getText().toString();
                String amount = mViewHolder.editTransAmount.getText().toString();
                String type = String.valueOf(mViewHolder.spinTransType.getSelectedItemId());

                mDatabase.addEntry(title, amount, type);

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
