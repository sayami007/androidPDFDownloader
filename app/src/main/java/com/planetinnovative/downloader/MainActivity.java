package com.planetinnovative.downloader;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    PDFView pdfView;
    Button download;

    //String PDF_LINK = "http://www.fomecd.edu.np/cms/dhurbas/faculty%20member0001.PDF";
    String PDF_LINK = "https://sayami007.github.io/om.pdf";
    String MY_PDF = "pdf1.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download = findViewById(R.id.download);
        pdfView = findViewById(R.id.pdfView);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPdf(MY_PDF);
            }
        });
    }

    private void downloadPdf(final String my_pdf) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Integer, Boolean> var = new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return downloadPdf();
            }

            private Boolean downloadPdf() {
                try {
                    System.out.println(my_pdf);
                    FileOutputStream fileOutputStream = openFileOutput(my_pdf, Context.MODE_PRIVATE);
                    URL u = new URL(PDF_LINK);
                    URLConnection conn = u.openConnection();
                    int contentLength = conn.getContentLength();
                    InputStream input = new BufferedInputStream(u.openStream());
                    byte data[] = new byte[contentLength];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != 1) {
                        total += count;
                        publishProgress((int) ((total * 100) / contentLength));
                        fileOutputStream.write(data, 0, count);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    input.close();
                    return true;
                } catch (Exception err) {
                    System.out.println(err.getMessage());
                    System.out.println("ERROR");
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                openPDF(my_pdf);
                super.onPostExecute(aBoolean);
            }
        };
        var.execute();
    }

    void openPDF(String fileName) {
        File file = getFileStreamPath(fileName);
        pdfView.fromFile(file).enableSwipe(true)
                .swipeHorizontal(false)
                .load();
        System.out.println(file.getAbsoluteFile());
    }
}