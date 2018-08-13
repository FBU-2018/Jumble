package com.example.lkimberly.userstories.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.User;

import static com.parse.ParseUser.getCurrentUser;

public class FacebookDialogFragment extends DialogFragment {

    static final String DEFAULT_URL = "https://www.";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final User user = (User) getCurrentUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_facebook, null);

        final EditText etLinks = view.findViewById(R.id.etLinks);

        etLinks.setText(DEFAULT_URL);

        if (user.getFacebook() != null) {
            etLinks.setText(user.getFacebook());
        }

        builder.setView(view)
                .setPositiveButton("Set link", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        final String link = etLinks.getText().toString();

                        if (!link.equals("")) {
                            if (link.contains("https://www.") && link.contains(".com")) {
                                user.setFacebook(link);
                                user.saveInBackground();
                            } else {
                                Toast.makeText(getActivity(), "You did not enter a valid url", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "You did not enter a url", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}