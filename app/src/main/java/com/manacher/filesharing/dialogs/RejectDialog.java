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

public class RejectDialog extends AppCompatDialogFragment {
    private Button closeButton;
    private Button tryAgainButton;

    private RejectDialogListener listener;

    public RejectDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.reject_dialog, null);
        builder.setView(view);

        closeButton = view.findViewById(R.id.backButton);
        tryAgainButton = view.findViewById(R.id.tryAgainButton);

        listener.rejectCardButton(closeButton, tryAgainButton);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RejectDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface RejectDialogListener {
        void rejectCardButton(Button closeButton, Button tryAgainButton);
    }
}
