package com.haleydu.cimoc.manager;

import android.util.SparseArray;

import com.haleydu.cimoc.component.AppGetter;
import com.haleydu.cimoc.model.Source;
import com.haleydu.cimoc.model.SourceDao;
import com.haleydu.cimoc.model.SourceDao.Properties;
import com.haleydu.cimoc.parser.Parser;
import com.haleydu.cimoc.source.*;
import com.haleydu.cimoc.source.WebtoonDongManManHua;

import java.util.List;

import okhttp3.Headers;
import rx.Observable;

/**
 * Created by Hiroshi on 2016/8/11.
 */
public class SourceManager {

    private static SourceManager mInstance;

    private SourceDao mSourceDao;
    private SparseArray<Parser> mParserArray = new SparseArray<>();

    private SourceManager(AppGetter getter) {
        mSourceDao = getter.getAppInstance().getDaoSession().getSourceDao();
    }

    public static SourceManager getInstance(AppGetter getter) {
        if (mInstance == null) {
            synchronized (SourceManager.class) {
                if (mInstance == null) {
                    mInstance = new SourceManager(getter);
                }
            }
        }
        return mInstance;
    }

    public Observable<List<Source>> list() {
        return mSourceDao.queryBuilder()
                .orderAsc(Properties.Type)
                .rx()
                .list();
    }

    public Observable<List<Source>> listEnableInRx() {
        return mSourceDao.queryBuilder()
                .where(Properties.Enable.eq(true))
                .orderAsc(Properties.Type)
                .rx()
                .list();
    }

    public List<Source> listEnable() {
        return mSourceDao.queryBuilder()
                .where(Properties.Enable.eq(true))
                .orderAsc(Properties.Type)
                .list();
    }

    public Source load(int type) {
        return mSourceDao.queryBuilder()
                .where(Properties.Type.eq(type))
                .unique();
    }

    public long insert(Source source) {
        return mSourceDao.insert(source);
    }

    public void update(Source source) {
        mSourceDao.update(source);
    }

    public Parser getParser(int type) {
        Parser parser = mParserArray.get(type);
        if (parser == null) {
            Source source = load(type);
            SourceEnum sourceEnum = SourceEnum.get(type);
            if (sourceEnum == null){
                parser = new Null();
                mParserArray.put(type,parser);
                return parser;
            }
            switch (sourceEnum) {
                case IKanman:
                    parser = new IKanman(source);
                    break;
                case Dmzj:
                    parser = new Dmzj(source);
                    break;
                case HHAAZZ:
                    parser = new HHAAZZ(source);
                    break;
                case CCTuku:
                    parser = new CCTuku(source);
                    break;
                case U17:
                    parser = new U17(source);
                    break;
                case DM5:
                    parser = new DM5(source);
                    break;
                case Webtoon:
                    parser = new Webtoon(source);
                    break;
                case MH57:
                    parser = new MH57(source);
                    break;
                case MH50:
                    parser = new MH50(source);
                    break;
                case Dmzjv2:
                    parser = new Dmzjv2(source);
                    break;
                case Locality:
                    parser = new Locality();
                    break;
                case MangaNel:
                    parser = new MangaNel(source);
                    break;
                case PuFei:
                    parser = new PuFei(source);
                    break;
                case Tencent:
                    parser = new Tencent(source);
                    break;
                case BuKa:
                    parser = new BuKa(source);
                    break;
                case EHentai:
                    parser = new EHentai(source);
                    break;
                case QiManWu:
                    parser = new QiManWu(source);
                    break;
                case Hhxxee:
                    parser = new Hhxxee(source);
                    break;
                case Cartoonmad:
                    parser = new Cartoonmad(source);
                    break;
                case Animx2:
                    parser = new Animx2(source);
                    break;
                case MH517:
                    parser = new MH517(source);
                    break;
                case MiGu:
                    parser = new MiGu(source);
                    break;
                case BaiNian:
                    parser = new BaiNian(source);
                    break;
                case ChuiXue:
                    parser = new ChuiXue(source);
                    break;
                case TuHao:
                    parser = new TuHao(source);
                    break;
                case SixMH:
                    parser = new SixMH(source);
                    break;
                case ManHuaDB:
                    parser = new ManHuaDB(source);
                    break;
                case Manhuatai:
                    parser = new Manhuatai(source);
                    break;
                case GuFeng:
                    parser = new GuFeng(source);
                    break;
                case CCMH:
                    parser = new CCMH(source);
                    break;
                case MHLove:
                    parser = new MHLove(source);
                    break;
                case YYLS:
                    parser = new YYLS(source);
                    break;
                case JMTT:
                    parser = new JMTT(source);
                    break;

                //haleydu
                case Mangakakalot:
                    parser = new Mangakakalot(source);
                    break;
                case Ohmanhua:
                    parser = new Ohmanhua(source);
                    break;
                case CopyMH:
                    parser = new CopyMH(source);
                    break;
                case HotManga:
                    parser = new HotManga(source);
                    break;
                case WebtoonDongManManHua:
                    parser = new WebtoonDongManManHua(source);
                    break;
                case MH160:
                    parser = new MH160(source);
                    break;
                case QiMiaoMH:
                    parser = new QiMiaoMH(source);
                    break;
                case YKMH:
                    parser = new YKMH(source);
                    break;
                case DmzjFix:
                    parser = new DmzjFix(source);
                    break;
                case CommonApi:
                    parser = new CommonApi(source);
                    break;
                case HanguoMH:
                    parser = new HanguoMH(source);
                    break;
                case MiaoQuMH:
                    parser = new MiaoQuMH(source);
                    break;
                default:
                    parser = new Null();
                    break;
            }
            mParserArray.put(type, parser);
        }
        return parser;
    }

    public class TitleGetter {

        public String getTitle(int type) {
            return getParser(type).getTitle();
        }

    }

    public class HeaderGetter {

        public Headers getHeader(int type) {
            return getParser(type).getHeader();
        }

    }
}
