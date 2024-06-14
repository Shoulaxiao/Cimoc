package com.haleydu.cimoc.source;

import com.google.common.collect.Lists;
import com.haleydu.cimoc.core.Manga;
import com.haleydu.cimoc.model.Chapter;
import com.haleydu.cimoc.model.Comic;
import com.haleydu.cimoc.model.ImageUrl;
import com.haleydu.cimoc.model.Source;
import com.haleydu.cimoc.parser.MangaParser;
import com.haleydu.cimoc.parser.NodeIterator;
import com.haleydu.cimoc.parser.SearchIterator;
import com.haleydu.cimoc.soup.Node;
import com.haleydu.cimoc.utils.StringUtils;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Request;

/**
 * @auther shoulaxiao
 * @date 2024/6/14 11:20
 **/
public class HanguoMH extends MangaParser {


    private final static SourceEnum TYPE = SourceEnum.HanguoMH;

    public HanguoMH(Source source) {
        init(source, null);
    }

    public static Source getDefaultSource() {
        return new Source(null, TYPE.getDesc(), TYPE.getCode(), true);
    }

    @Override
    public Request getSearchRequest(String keyword, int page) throws UnsupportedEncodingException, Exception {
        String url = StringUtils.format("https://www.hanguomh.com/search?key=%s",
                URLEncoder.encode(keyword, "UTF-8"));

        return new Request.Builder()
                .url(url).build();
    }

    @Override
    public SearchIterator getSearchIterator(String html, int page) throws JSONException {
        Node body = new Node(html);
        if (html.contains("系统繁忙，请稍候重试")){
            throw new JSONException(html);
        }
        return new NodeIterator(body.list("div.mh-item")) {
            @Override
            protected Comic parse(Node node) {
                String cid = node.getChild("a").attr("href");
                String cover = node.getChild("img").attr("src");
                String title = node.getChild("div.mh-item-detali > h2.title > a").attr("title");
                String update = node.getChild("div.mh-item-detali > p.chapter > a").attr("title");
                String author = "";
                return new Comic(TYPE.getCode(), cid, title, cover, update, author);
            }
        };
    }

    @Override
    public Request getInfoRequest(String cid) {
        String url = "https://www.hanguomh.com".concat(cid) + "/";
        return new Request.Builder().url(url).build();
    }

    @Override
    public Comic parseInfo(String html, Comic comic) throws UnsupportedEncodingException {
        Node body = new Node(html);
        String cover = body.src("div.detail-info-1 > div.container > div.detail-info > img.detail-info-cover");
        String intro = body.text("div.detail-info-2 > div.container > div.detail-info > p.detail-info-content");
        String title = body.text("div.detail-info-1 > div.container > div.detail-info > p.detail-info-title");

        String update = "";
        String author = body.text("div.detail-info-1 > div.container > div.detail-info > p.detail-info-tip > span > a");

        // 连载状态
        boolean status = isFinish("连载");
        comic.setInfo(title, cover, update, intro, author, status);
        return comic;
    }

    @Override
    public List<Chapter> parseChapter(String html, Comic comic, Long sourceComic) throws JSONException {
        List<Chapter> list = new LinkedList<>();
        int i = 0;
        for (Node node : new Node(html).list("#chapterlistload > a")) {
            String title = node.text();
            String path = node.href();
            list.add(new Chapter(Long.parseLong(sourceComic + "000" + i++), sourceComic, title, path));
        }
        return Lists.reverse(list);
    }

    @Override
    public Request getImagesRequest(String cid, String path) {
        String url = "https://www.hanguomh.com" + path;
        return new Request.Builder().url(url).build();
    }

    @Override
    public List<ImageUrl> parseImages(String html, Chapter chapter) throws Manga.NetworkErrorException, JSONException {
        List<ImageUrl> list = new LinkedList<>();
        Node body = new Node(html);

        try {
            List<Node> list1 = body.list("div.main main-scroll_mode J_scroll_mode J_block li.main-item");
            Long comicChapter = Long.parseLong(body.text("chapter-sub").trim());
            for (int i = 0; i < list1.size(); i++) {
                Node node = list1.get(i);
                String url = node.src("img");
                Long id = Long.parseLong(comicChapter + "000" + i);
                list.add(new ImageUrl(id, comicChapter, i + 1, url, false));
            }
        } catch (Exception e) {

        }
        return list;
    }
}
