package com.example.lkimberly.userstories.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.User;

import static com.parse.ParseUser.getCurrentUser;

public class NumberPickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText etEstimation = getActivity().findViewById(R.id.etEstimation);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View customTitleView = inflater.inflate(R.layout.custom_title, null);
        final TextView title = customTitleView.findViewById(R.id.customTitle);
        title.setText("1 Hour");

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = layoutInflater.inflate(R.layout.dialog_number_picker, null);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);

        builder.setView(view)
                .setCustomTitle(customTitleView)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        String hourStr = " Hours";

                        if (numberPicker.getValue() == 1) {
                            hourStr = " Hour";

                        }

                        etEstimation.setText(numberPicker.getValue() + hourStr);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (numberPicker.getValue() == 1) {
                    title.setText(numberPicker.getValue() + " Hour");
                } else {
                    title.setText(numberPicker.getValue() + " Hours");
                }
            }
        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
