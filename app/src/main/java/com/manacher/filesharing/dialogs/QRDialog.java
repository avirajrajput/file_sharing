package com.manacher.filesharing.dialogs;


import android.app.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.manacher.filesharing.R;
import com.manacher.filesharing.utils.QRUtil;

public class QRDialog extends AppCompatDialogFragment {

    private ImageView imageView;
    private String data;

    public QRDialog(String data){
        this.data = data;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.qr_dialog, null);
        builder.setView(view);

        QRUtil qrUtil = new QRUtil(getActivity());
        imageView = view.findViewById(R.id.imageView);

        imageView.setImageBitmap(qrUtil.createQR(data));


        return builder.create();
    }

}
