package com.example.expensestracker.dialogBox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.expensestracker.R;
import com.example.expensestracker.data.Database;
import com.example.expensestracker.views.MainActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class editBudgetDialog extends DialogFragment {
    Database mDatabase;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDatabase = new Database(getActivity());

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View mView = inflater.inflate(R.layout.dialog_add_budget, null);
        final EditText amount = mView.findViewById(R.id.edit_budget_to_add);
        String tt;
        try {
            tt = String.valueOf(mDatabase.getTotalBudget());
        } catch (Exception e) {
            tt = "0.00";
        }
        amount.setText(tt);

        final String finalTt = tt;
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            private String current = finalTt;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    amount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
                    symbols.setCurrencySymbol("");
                    formatter.setDecimalFormatSymbols(symbols);

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = formatter.format((parsed/100));

                    current = formatted;
                    amount.setText(formatted);
                    amount.setSelection(formatted.length());

                    amount.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        builder.setView(mView);

        builder.setTitle(R.string.dialog_edit_budget_title)
               .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       String txt = amount.getText().toString();
                       float i = Float.parseFloat(txt.replace(",",""));
                       Log.d("EDIT", String.valueOf(i));
                       mDatabase.editBudget(i);

                       ((MainActivity)getActivity()).updateBudgetDisplay();
                   }
               })

               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        //cancel
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
