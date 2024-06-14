package com.haleydu.cimoc.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.haleydu.cimoc.R;
import com.haleydu.cimoc.manager.SourceManager;
import com.haleydu.cimoc.source.Animx2;
import com.haleydu.cimoc.source.BaiNian;
import com.haleydu.cimoc.source.BuKa;
import com.haleydu.cimoc.source.Cartoonmad;
import com.haleydu.cimoc.source.ChuiXue;
import com.haleydu.cimoc.source.DM5;
import com.haleydu.cimoc.source.Dmzjv2;
import com.haleydu.cimoc.source.Hhxxee;
import com.haleydu.cimoc.source.HotManga;
import com.haleydu.cimoc.source.IKanman;
import com.haleydu.cimoc.source.MH50;
import com.haleydu.cimoc.source.MH517;
import com.haleydu.cimoc.source.MH57;
import com.haleydu.cimoc.source.ManHuaDB;
import com.haleydu.cimoc.source.MiGu;
import com.haleydu.cimoc.source.PuFei;
import com.haleydu.cimoc.source.SixMH;
import com.haleydu.cimoc.source.SourceEnum;
import com.haleydu.cimoc.source.Tencent;
import com.haleydu.cimoc.source.TuHao;
import com.haleydu.cimoc.source.U17;
import com.haleydu.cimoc.source.YKMH;

import java.util.ArrayList;
import java.util.List;

public class BrowserFilter extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_browser_filter;
    }

    @Override
    protected String getDefaultTitle() {
        return "jumping...";
    }

//    private Parser parser;
//    private SourceManager mSourceManager;

    public void openDetailActivity(int source, String comicId) {
        Intent intent = DetailActivity.createIntent(this, null, source, comicId);
        startActivity(intent);
    }

//    public void openReaderActivity(int source,String comicId) {
//        Intent intent = DetailActivity.createIntent(this, null, source, comicId);
//        startActivity(intent);
//    }

    private List<Integer> registUrlListener() {
        List<Integer> list = new ArrayList<>();

        list.add(SourceEnum.Dmzjv2.getCode());
        list.add(SourceEnum.BuKa.getCode());
        list.add(SourceEnum.PuFei.getCode());
        list.add(SourceEnum.Cartoonmad.getCode());
        list.add(SourceEnum.Animx2.getCode());
        list.add(SourceEnum.MH517.getCode());
        list.add(SourceEnum.BaiNian.getCode());
        list.add(SourceEnum.MiGu.getCode());
        list.add(SourceEnum.Tencent.getCode());
        list.add(SourceEnum.U17.getCode());
        list.add(SourceEnum.MH57.getCode());
        list.add(SourceEnum.MH50.getCode());
        list.add(SourceEnum.DM5.getCode());
        list.add(SourceEnum.IKanman.getCode());
        list.add(SourceEnum.Hhxxee.getCode());
        list.add(SourceEnum.BaiNian.getCode());
        list.add(SourceEnum.ChuiXue.getCode());
        list.add(SourceEnum.ManHuaDB.getCode());
        list.add(SourceEnum.TuHao.getCode());
        list.add(SourceEnum.YKMH.getCode());
        list.add(SourceEnum.SixMH.getCode());
        list.add(SourceEnum.HotManga.getCode());
        return list;
    }

    private void openReader(Uri uri) {
        try {
            SourceManager mSourceManager = SourceManager.getInstance(this);
            String comicId;

            for (int i : registUrlListener()) {
                if (mSourceManager.getParser(i).isHere(uri)
                        && ((comicId = mSourceManager.getParser(i).getComicId(uri)) != null)) {
                    openDetailActivity(i, comicId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openReaderByIntent(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();

        //来自url
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                openReader(uri);
            } else {
                Toast.makeText(this, "url不合法", Toast.LENGTH_SHORT);
            }
        }

        //来自分享
        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            try {
                openReader(Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT).replace("https://m.ykmh.commanhua", "https://m.ykmh.com/manhua")));
            } catch (Exception ex) {
                Toast.makeText(this, "url不合法", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_filter);

        openReaderByIntent(getIntent());

        finish();
    }
}
