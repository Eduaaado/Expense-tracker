package com.example.expensestracker.dialogBox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.example.expensestracker.R;
import com.example.expensestracker.views.SettingsActivity;

public class ThemeDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View mView = inflater.inflate(R.layout.dialog_choose_theme, null);
        builder.setView(mView);

        RadioGroup rg = mView.findViewById(R.id.rg_theme);
        int rbid = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).getInt("theme", 0);
        RadioButton rb;
        switch (rbid) {
            case 0:
                rb = rg.findViewById(R.id.rb_theme_system);
                rb.setChecked(true);
                break;
            case 1:
                rb = rg.findViewById(R.id.rb_theme_light);
                rb.setChecked(true);
                break;
            case 2:
                rb = rg.findViewById(R.id.rb_theme_dark);
                rb.setChecked(true);
                break;
        }

        builder.setTitle(R.string.choose_theme)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup rg = mView.findViewById(R.id.rg_theme);
                        View rb = rg.findViewById(rg.getCheckedRadioButtonId());
                        int idRb = rg.indexOfChild(rb);

                        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("theme", idRb);
                        editor.apply();

                        TextView txtOption = getActivity().findViewById(R.id.txt_theme_option);
                        String[] themestext = getResources().getStringArray(R.array.arr_theme_options);
                        txtOption.setText(themestext[idRb]);

                        ((SettingsActivity)getActivity()).checkTheme();
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
