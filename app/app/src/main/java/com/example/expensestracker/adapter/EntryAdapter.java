package com.example.expensestracker.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.expensestracker.R;
import com.example.expensestracker.data.Database;
import com.example.expensestracker.dialogBox.DeleteDialog;
import com.example.expensestracker.dialogBox.editBudgetDialog;
import com.example.expensestracker.views.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EntryAdapter extends ArrayAdapter<EntryDetails> {

    Database mDatabase = new Database(getContext());
    private Context mContext;

    private static class ViewHolder {
        TextView txtName;
        TextView txtCurrency;
        TextView txtAmount;
        TextView txtType;
        TextView txtTime;
        Button btnDelete;
    }

    public EntryAdapter(Context context, ArrayList<EntryDetails> details) {
        super(context, 0, details);

        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EntryDetails details = getItem(position);

        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_entry_transaction, parent, false);

            mViewHolder.txtName = convertView.findViewById(R.id.txt_entry_name);
            mViewHolder.txtCurrency = convertView.findViewById(R.id.txt_entry_currencysymbol);
            mViewHolder.txtAmount = convertView.findViewById(R.id.txt_entry_amount);
            mViewHolder.txtType = convertView.findViewById(R.id.txt_entry_type);
            mViewHolder.txtTime = convertView.findViewById(R.id.txt_entry_time);
            mViewHolder.btnDelete = convertView.findViewById(R.id.btn_entry_delete);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        SharedPreferences prefs = mContext.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        mViewHolder.txtCurrency.setText(prefs.getString("currency", mContext.getResources().getString(R.string.currency)));

        mViewHolder.txtName.setText(details.name);
        mViewHolder.txtAmount.setText(details.amount);

        String txtCurr = (String) mViewHolder.txtCurrency.getText();
        if (Integer.parseInt(details.type) == 0) {
            if (txtCurr.indexOf("-") == -1) { mViewHolder.txtCurrency.setText("-"+txtCurr); }
            mViewHolder.txtType.setText("Expense");
        } else {
            mViewHolder.txtCurrency.setText(txtCurr.replace("-",""));
            mViewHolder.txtType.setText("Income");
        }

        SimpleDateFormat original = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat target = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String time = null;
        try {
            Date d = original.parse(details.time);
            time = target.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mViewHolder.txtTime.setText(time);

        String[][] entries = mDatabase.getEntries();

        mViewHolder.btnDelete.setTag(entries[position][4]);
        mViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(String.valueOf(v.getTag()));
                Log.d("POSITION", String.valueOf(position));

                DialogFragment dialog = new DeleteDialog();

                Bundle args = new Bundle();
                args.putInt("position", position);
                dialog.setArguments(args);

                dialog.show(((MainActivity)mContext).getSupportFragmentManager(), "add budget");

                ((MainActivity)mContext).updateBudgetDisplay();
            }
        });
        mViewHolder.btnDelete.setId(position);

        return convertView;
    }
}
