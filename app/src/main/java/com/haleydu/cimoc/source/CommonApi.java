package com.haleydu.cimoc.source;

import com.haleydu.cimoc.core.Manga;
import com.haleydu.cimoc.model.Chapter;
import com.haleydu.cimoc.model.Comic;
import com.haleydu.cimoc.model.ImageUrl;
import com.haleydu.cimoc.model.Source;
import com.haleydu.cimoc.parser.MangaParser;
import com.haleydu.cimoc.parser.SearchIterator;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Request;

/**
 * @auther shoulaxiao
 * @date 2024/6/13 21:53
 **/
public class CommonApi extends MangaParser {

    public static final SourceEnum TYPE = SourceEnum.CommonApi;


    public CommonApi(Source source) {
    }

    public static Source getDefaultSource() {
        return new Source(null, TYPE.getDesc(), TYPE.getCode(), false);
    }

    @Override
    public Request getSearchRequest(String keyword, int page) throws UnsupportedEncodingException, Exception {
        return null;
    }

    @Override
    public SearchIterator getSearchIterator(String html, int page) throws JSONException {
        return null;
    }

    @Override
    public Request getInfoRequest(String cid) {
        return null;
    }

    @Override
    public Comic parseInfo(String html, Comic comic) throws UnsupportedEncodingException {
        return null;
    }

    @Override
    public List<Chapter> parseChapter(String html, Comic comic, Long sourceComic) throws JSONException {
        return null;
    }

    @Override
    public Request getImagesRequest(String cid, String path) {
        return null;
    }

    @Override
    public List<ImageUrl> parseImages(String html, Chapter chapter) throws Manga.NetworkErrorException, JSONException {
        return null;
    }
}
