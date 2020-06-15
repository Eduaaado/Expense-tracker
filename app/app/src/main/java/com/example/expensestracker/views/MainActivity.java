package com.example.expensestracker.views;

import android.content.Intent;
import android.os.Bundle;

import com.example.expensestracker.R;
import com.example.expensestracker.dialogBox.addBudgetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mViewHolder.txtBudgetInt = findViewById(R.id.txt_budget_integers);
        this.mViewHolder.txtBudgetDec = findViewById(R.id.txt_budget_decimal);
        this.mViewHolder.btnBudgetEdit = findViewById(R.id.btn_budget_edit);
        this.mViewHolder.btnBudgetAdd = findViewById(R.id.btn_budget_add);
        this.mViewHolder.lstTransactions = findViewById(R.id.list_transactions);

        this.mViewHolder.fab = findViewById(R.id.fab);
        this.mViewHolder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTransactionActivity.class);
                startActivity(intent);
            }
        });

        this.mViewHolder.btnBudgetAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new addBudgetDialog();
                newFragment.show(getSupportFragmentManager(), "add budget");
            }
        });

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
