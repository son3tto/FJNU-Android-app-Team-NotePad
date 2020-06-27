package com.example.android.notepad.activity;

import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.notepad.R;

import java.io.File;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class PDFActivity extends AppCompatActivity implements DownloadFile.Listener {


    private RelativeLayout pdf_root;
    private ProgressBar pb_bar;
    private RemotePDFViewPager remotePDFViewPager;
//    private EditText editText;
//    private String mUrl = getIntent().getStringExtra("url");
//            "https://cs.nju.edu.cn/zhouzh/zhouzh.files/publication/cccf07.pdf";
//
    private PDFPagerAdapter adapter;
    private ImageView iv_back;
//    private Button url_btn;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f);
        initView();

        String mUrl = Objects.requireNonNull(getIntent().getExtras()).getString("com.example.android.notepad.url","none");
        Toast.makeText(getApplicationContext(), mUrl, Toast.LENGTH_LONG).show();
//        String mUrl = "http://caii.ckgsb.com/uploads/life/201901/25/1548395019500411.pdf";
        setDownloadListener(mUrl);
//        editText = findViewById(R.id.murl);

//        url_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String mUrl = editText.getText().toString();

//                Toast.makeText(getApplicationContext(), mUrl, Toast.LENGTH_LONG).show();
//                setDownloadListener(mUrl);
//            }
//        });

    }

    protected void initView() {
//        url_btn = findViewById(R.id.murl_btn);
        pdf_root = findViewById(R.id.remote_pdf_root);
        pb_bar = findViewById(R.id.pb_bar);
//        editText = findViewById(R.id.murl);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*设置监听*/
    protected void setDownloadListener(String mUrl) {
        final DownloadFile.Listener listener = this;
        remotePDFViewPager = new RemotePDFViewPager(this, mUrl, listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
        Canvas canvas = new Canvas();

        remotePDFViewPager.draw(canvas);
    }


/*    protected String getPdfPathOnSDCard() {
        File f = new File(getExternalFilesDir("pdf"), "adobe.pdf");
        return f.getAbsolutePath();
    }*/

    /*加载成功调用*/
    @Override
    public void onSuccess(String url, String destinationPath) {
        pb_bar.setVisibility(View.GONE);
//        FileUtil.copyAsset()

        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
    }

    /*更新视图*/
    private void updateLayout() {
        pdf_root.removeAllViewsInLayout();
        pdf_root.addView(remotePDFViewPager, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /*加载失败调用*/
    @Override
    public void onFailure(Exception e) {
        pb_bar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "数据加载错误", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProgressUpdate(int progress, int total) {
    }
}