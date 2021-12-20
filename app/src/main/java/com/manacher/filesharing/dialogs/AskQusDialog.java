package com.manacher.filesharing.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.manacher.filesharing.R;

import java.util.Objects;

public class AskQusDialog extends AppCompatDialogFragment {
    private Button yesButton;
    private Button noButton;
    private TextView qusText;
    private String qus;
    private AskDisconnectedDialogListener listener;

    public AskQusDialog(String qus) {
        this.qus = qus;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.ask_disconnect_dialog, null);
        builder.setView(view);


        yesButton = view.findViewById(R.id.yesButton);
        noButton = view.findViewById(R.id.noButton);
        qusText = view.findViewById(R.id.qus);
        qusText.setText(qus);


        listener.AskQusDialogButton(yesButton, noButton);

        return builder.create();
    }

    @Override
    public void onAttach( Context context) {
        super.onAttach(context);
        try {
            listener = (AskDisconnectedDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface AskDisconnectedDialogListener {
        void AskQusDialogButton(Button yesButton, Button noButton);
    }
}
