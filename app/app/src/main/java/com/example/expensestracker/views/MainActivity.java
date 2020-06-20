package com.example.expensestracker.views;

import android.content.Intent;
import android.os.Bundle;

import com.example.expensestracker.R;
import com.example.expensestracker.adapter.EntryAdapter;
import com.example.expensestracker.adapter.EntryDetails;
import com.example.expensestracker.data.Database;
import com.example.expensestracker.dialogBox.editBudgetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database mDatabase;
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = new Database(this);

        this.mViewHolder.txtBudgetInt = findViewById(R.id.txt_budget_integers);
        this.mViewHolder.txtBudgetDec = findViewById(R.id.txt_budget_decimal);
        this.mViewHolder.btnBudgetEdit = findViewById(R.id.btn_budget_edit);
        this.mViewHolder.lstTransactions = findViewById(R.id.list_transactions);

        this.mViewHolder.fab = findViewById(R.id.fab);
        this.mViewHolder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTransactionActivity.class);
                startActivity(intent);
            }
        });

        this.mViewHolder.btnBudgetEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new editBudgetDialog();
                newFragment.show(getSupportFragmentManager(), "add budget");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateBudgetDisplay();

        String[][] entries = mDatabase.getEntries();

        // Construct the data source
        ArrayList<EntryDetails> entryList = new ArrayList<EntryDetails>();

        for (int i = 0; i < entries.length; i++) {
            EntryDetails details = new EntryDetails(entries[i][0], entries[i][1], entries[i][2], entries[i][3]);
            entryList.add(details);
        }

        // Create the adapter to convert the array to views
        EntryAdapter adapter = new EntryAdapter(this, entryList);
        // Attach the adapter to a ListView
        this.mViewHolder.lstTransactions.setAdapter(adapter);
    }

    public void updateBudgetDisplay() {
        try {
            float money = mDatabase.getTotalBudget();
            String strMoney = String.valueOf(money);

            int indexOfDecimal = strMoney.indexOf(".");
            String moneyInt = strMoney.substring(0, indexOfDecimal);
            String moneyDec = strMoney.substring(indexOfDecimal);

            this.mViewHolder.txtBudgetInt.setText(moneyInt);
            if (moneyDec.length() == 2) { moneyDec = moneyDec+"0"; }
            this.mViewHolder.txtBudgetDec.setText(moneyDec);

            if (money > 0) {
                this.mViewHolder.txtBudgetInt.setTextColor(getResources().getColor(R.color.moneyPositive));
                this.mViewHolder.txtBudgetDec.setTextColor(getResources().getColor(R.color.moneyPositive));
            } else {
                this.mViewHolder.txtBudgetInt.setTextColor(getResources().getColor(R.color.moneyNegative));
                this.mViewHolder.txtBudgetDec.setTextColor(getResources().getColor(R.color.moneyNegative));
            }

        } catch (Exception e) {
            this.mViewHolder.txtBudgetInt.setText("0");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class ViewHolder {
        FloatingActionButton fab;
        TextView txtBudgetInt;
        TextView txtBudgetDec;
        Button btnBudgetEdit;
        Button btnBudgetAdd;
        ListView lstTransactions;
    }
}
