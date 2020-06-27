package com.example.android.notepad.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.text.Layout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.*;

public class PdfUtil {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pdfModel(Context context,RelativeLayout ll_model) throws FileNotFoundException {
        PdfDocument document = new PdfDocument();
        // ll_model是一个LinearLayout
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(100,100,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        ll_model.draw(page.getCanvas());
        document.finishPage(page);

        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        FileOutputStream outputStream = new FileOutputStream(absolutePath);
        Toast.makeText(context, "PDF", Toast.LENGTH_LONG).show();
        try {
            document.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
    }
}
