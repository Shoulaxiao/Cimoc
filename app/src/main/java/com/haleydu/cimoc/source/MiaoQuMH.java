package com.haleydu.cimoc.source;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haleydu.cimoc.core.Manga;
import com.haleydu.cimoc.model.Chapter;
import com.haleydu.cimoc.model.Comic;
import com.haleydu.cimoc.model.ImageUrl;
import com.haleydu.cimoc.model.Source;
import com.haleydu.cimoc.model.vo.HanGuoManHuaRes;
import com.haleydu.cimoc.parser.MangaParser;
import com.haleydu.cimoc.parser.NodeIterator;
import com.haleydu.cimoc.parser.SearchIterator;
import com.haleydu.cimoc.soup.Node;
import com.haleydu.cimoc.utils.Base64Utils;
import com.haleydu.cimoc.utils.StringUtils;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;

/**
 * @auther shoulaxiao
 * @date 2024/6/14 15:30
 **/
public class MiaoQuMH extends MangaParser {

    public static final SourceEnum TYPE = SourceEnum.MiaoQuMH;

    public static final String[] CIPHER_TABLE = new String[]{
            "OC1iWGQ5aU4=",
            "OC1SWHlqcnk=",
            "OC1vWXZ3Vnk=",
            "OC00Wlk1N1U=",
            "OC1tYkpwVTc=",
            "OC02TU0yRWk=",
            "OC01NFRpUXI=",
            "OC1QaDV4eDk=",
            "OC1iWWdlUFI=",
            "OC1aOUEzYlc="
    };


    Pattern pattern = Pattern.compile("url\\((.*?)\\)");

    public MiaoQuMH(Source source) {
        init(source, null);
    }

    public static Source getDefaultSource() {
        return new Source(null, TYPE.getDesc(), TYPE.getCode(), true);
    }

    @Override
    public Request getSearchRequest(String keyword, int page) throws UnsupportedEncodingException, Exception {
        String url = StringUtils.format("https://www.miaoqumh.com/search?key=%s",
                URLEncoder.encode(keyword, "UTF-8"));

        return new Request.Builder()
                .url(url).build();
    }

    @Override
    public SearchIterator getSearchIterator(String html, int page) throws JSONException {
        Node body = new Node(html);
        if (html.contains("系统繁忙，请稍候重试")) {
            throw new JSONException(html);
        }
        return new NodeIterator(body.list("#manga-list  li")) {
            @Override
            protected Comic parse(Node node) {
                String cid = node.href("a");
                String coverStr = node.getChild("a").attr("style");
                String cover = "";
                Matcher matcher = pattern.matcher(coverStr);
                if (matcher.find()) {
                    // 获取url()括号内的内容
                    cover = matcher.group(1);
                }

                String title = node.text("div.manga-names > a");
                String update = node.text("div.manga-names > p > a:eq(0)");
                String author = node.text("div.manga-names > p > a:eq(1)");
                return new Comic(TYPE.getCode(), cid, title, cover, update, author);
            }
        };
    }

    @Override
    public Request getInfoRequest(String cid) {
        String url = "https://www.miaoqumh.com".concat(cid) + "/";
        return new Request.Builder().url(url).build();
    }

    @Override
    public Comic parseInfo(String html, Comic comic) throws UnsupportedEncodingException {
        Node body = new Node(html);
        String cover = body.src("div.manga-img.boxshadow > img");
        String intro = body.text("div.right.boxshadow p.manga-desc-font");
        String title = body.text("div.right.boxshadow h1.title-font");

        String update = "";
        String author = body.text("div.right.boxshadow div.manga-author > a");

        // 连载状态
        boolean status = isFinish("连载");
        comic.setInfo(title, cover, update, intro, author, status);
        return comic;
    }

    @Override
    public List<Chapter> parseChapter(String html, Comic comic, Long sourceComic) throws JSONException {
        List<Chapter> list = new LinkedList<>();
        int i = 0;
        for (Node node : new Node(html).list("#episodes a")) {
            String title = node.text();
            String path = node.href();
            list.add(new Chapter(Long.parseLong(sourceComic + "000" + i++), sourceComic, title, path));
        }
        return Lists.reverse(list);
    }

    @Override
    public Request getImagesRequest(String cid, String path) {
        String url = "https://www.miaoqumh.com" + path;
        return new Request.Builder().url(url).build();
    }

    @Override
    public List<ImageUrl> parseImages(String html, Chapter chapter) throws Manga.NetworkErrorException, JSONException {
        List<ImageUrl> list = new LinkedList<>();
        String str = StringUtils.match("var DATA=\'(.*?)\'", html, 1);
        String cidStr = StringUtils.match("var cid=(\\d+);", html, 1);
        Integer cid;
        try {
            Long comicChapter = chapter.getId();
            if (cidStr == null){
                cid = chapter.getId().intValue();
            }else {
                cid = Integer.parseInt(cidStr);
            }
            List<HanGuoManHuaRes> list1 = decryptData(cid,str);
            for (int i = 0; i < list1.size(); i++) {
                Long id = Long.parseLong(comicChapter + "000" + i);
                list.add(new ImageUrl(id, comicChapter, i + 1, list1.get(i).getUrl(), false));
            }
        } catch (Exception e) {

        }
        return list;
    }


    private List<HanGuoManHuaRes> decryptData(int cid, String data) {
        try {
            String originalData = Base64Utils.base64Decode(data);

            int cipherIndex = cid % CIPHER_TABLE.length;
            String cipher = Base64Utils.base64Decode(CIPHER_TABLE[cipherIndex]);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < originalData.length(); i++) {
                int offset = i % cipher.length();
                int retCode = utf8CharCodeAt(originalData, i) ^ utf8CharCodeAt(cipher, offset);
                sb.append((char) retCode);
            }

            String plaintext = Base64Utils.base64Decode(sb.toString());

            return JSON.parseArray(plaintext, HanGuoManHuaRes.class);
        }catch (Exception ex){
            Log.d("MiaoQuMH", Objects.requireNonNull(ex.getMessage()));
        }
        return new ArrayList<>();
    }

    private int utf8CharCodeAt(String str, int index) {
        return str.charAt(index);
    }
}
