package com.example.expensestracker.dialogBox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.expensestracker.R;
import com.example.expensestracker.data.Database;
import com.example.expensestracker.views.MainActivity;

public class addBudgetDialog extends DialogFragment {
    Database mDatabase;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View mView = inflater.inflate(R.layout.dialog_add_budget, null);
        final EditText amount = mView.findViewById(R.id.edit_budget_to_add);
        builder.setView(mView);

        mDatabase = new Database(getActivity());

        builder.setTitle(R.string.dialog_add_budget_title)
               .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       String txt = amount.getText().toString();
                       int i = Integer.parseInt(txt);
                       mDatabase.increaseBudget(i);

                   }
               })

               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {

                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
