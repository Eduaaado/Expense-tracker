package com.example.expensestracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.expensestracker.R;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter<EntryDetails> {
    public EntryAdapter(Context context, ArrayList<EntryDetails> details) {
        super(context, 0, details);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EntryDetails details = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_entry_transaction, parent, false);
        }

        TextView txtName = convertView.findViewById(R.id.txt_entry_name);
        TextView txtAmount = convertView.findViewById(R.id.txt_entry_amount);
        TextView txtType = convertView.findViewById(R.id.txt_entry_type);
        TextView txtTime = convertView.findViewById(R.id.txt_entry_time);

        txtName.setText(details.name);
        txtAmount.setText(details.amount);
        txtType.setText(details.type);
        txtTime.setText(details.time);

        return convertView;
    }
}
