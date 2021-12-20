package com.manacher.filesharing.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.manacher.filesharing.R;

import java.util.Objects;

public class SuccessDialog extends AppCompatDialogFragment {
    private Button closeButton;
    private Button sendMoreButton;
    private SuccessDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.success_dialog, null);
        builder.setView(view);


        closeButton = view.findViewById(R.id.backButton);
        sendMoreButton = view.findViewById(R.id.sendMoreButton);

        listener.successCardButton(closeButton, sendMoreButton);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SuccessDialogListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface SuccessDialogListener {
        void successCardButton(Button leaveButton, Button sendMoreButton);
    }
}
