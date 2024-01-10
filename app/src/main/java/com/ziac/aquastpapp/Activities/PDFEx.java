package com.ziac.aquastpapp.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.R;

public class PDFEx extends AppCompatActivity {

    PDFView PdfView;
    FloatingActionButton Fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfex);

        PdfView = findViewById(R.id.pdfview);
        Fab = findViewById(R.id.fab);

        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("application/pdf");
            }
        });

    }

    private final ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    PDFView pdfView = findViewById(R.id.pdfview); // Replace R.id.pdfview with your actual PDFView ID
                    pdfView.fromUri(uri).load();
                }
            }
    );
}