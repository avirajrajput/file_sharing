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

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.manacher.filesharing.R;

import java.util.Objects;

public class ScanDialog extends AppCompatDialogFragment {
    private CodeScannerView scannerView;
    public CodeScanner codeScanner;
    private ScanListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.scan_dialog, null);
        builder.setView(view);

        scannerView = view.findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(getActivity(), scannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = result.getText();
                        listener.ScanCardListener(data);
                    }
                });
            }
        });

        return builder.create();
    }

    @Override
    public void onPause() {
        super.onPause();
        codeScanner.releaseResources();
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ScanListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface ScanListener {
        void ScanCardListener(String data);
    }
}
