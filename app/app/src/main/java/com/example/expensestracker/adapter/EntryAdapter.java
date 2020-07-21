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
import java.util.Calendar;
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
        TextView txtDate;
        Button btnDelete;
    }

    public EntryAdapter(Context context, ArrayList<EntryDetails> details) {
        super(context, 0, details);

        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EntryDetails details = getItem(position);

        SimpleDateFormat original = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat target = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dayform = new SimpleDateFormat("dd");
        SimpleDateFormat daymonthform = new SimpleDateFormat("dd/MM");
        SimpleDateFormat yearform = new SimpleDateFormat("yyyy");
        String time = null;
        int day = -1;
        String daymonth = null;
        int year = -1;

        try {
            Date d = original.parse(details.time);
            time = target.format(d);
            day = Integer.parseInt(dayform.format(d));
            daymonth = daymonthform.format(d);
            year = Integer.parseInt(yearform.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = mContext.getSharedPreferences("prefs", Context.MODE_PRIVATE);

        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            if (day == prefs.getInt("lastday", day-1) || day == prefs.getInt("lastyear", year)) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_entry_transaction, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.date_entry_transaction, parent, false);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("lastday", day);
                editor.putInt("lastyear", year);
                editor.apply();

                String txt = daymonth;
                Calendar cal = Calendar.getInstance();
                if (cal.get(Calendar.YEAR) == year) {
                    int todaydate = cal.get(Calendar.DAY_OF_MONTH);
                    if (day == todaydate) {
                        txt = this.mContext.getResources().getString(R.string.today);
                    } else if (day == todaydate - 1) {
                        txt = this.mContext.getResources().getString(R.string.yesterday);
                    }
                } else {
                    txt = daymonth+"/"+year;
                }

                mViewHolder.txtDate = convertView.findViewById(R.id.txt_entry_date);
                mViewHolder.txtDate.setText(txt);
            }

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
