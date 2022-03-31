package com.doan.mp3music.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

public class Dialog {
    private static AlertDialog dialog;
    public static void showDialog(Context context) {
        dismiss();
        dialog = new ProgressDialog.Builder(context)
                .setTitle("Processing...")
                .setCancelable(false)
                .create();
        dialog.show();
    }

    public static void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
