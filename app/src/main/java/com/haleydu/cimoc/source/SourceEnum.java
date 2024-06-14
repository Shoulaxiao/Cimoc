package com.haleydu.cimoc.source;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther shoulaxiao
 * @date 2024/6/13 21:55
 **/
public enum SourceEnum {
    Locality(-2,"本地漫画"),
    NULL(-1,"(null)"),
    IKanman(0,"漫画柜"),
    Dmzj(1,"动漫之家"),
    HHAAZZ(2,"汗汗酷漫"),
    CCTuku(3,"CC图库"),
    U17(4,"有妖气"),
    DM5(5,"动漫屋"),
    Webtoon(6,"Webtoon"),
    Chuiyao(7,"吹妖漫画"),
    MH57(8,"57漫画"),

    YYLS(9,"YYLS"),
    Dmzjv2(10,"动漫之家v2"),
    WebtoonDongManManHua(11,"咚漫漫画"),
    CommonApi(12,"漫画API"),

    BaiNian(13,"百年漫画"),
    HanguoMH(14,"韩国漫画"),
    CCMH(23,"CC漫画"),
    TuHao(24,"土豪漫画"),
    GuFeng(25,"古风漫画"),
    CopyMH(26,"拷贝漫画"),
    MHLove(27,"漫画Love"),
    MH160(28,"漫画160"),
    MangaNel(43,"MangaNel"),
    Mangakakalot(44,"Mangakakalot"),
    ManHuaDB(46,"漫画DB"),
    Manhuatai(49,"漫画台"),
    PuFei(50,"扑飞漫画"),
    Tencent(51,"腾讯动漫"),
    BuKa(52,"布卡漫画"),
    QiManWu(53,"奇漫屋"),
    Cartoonmad(54,"动漫狂"),
    Animx2(55,"2animx"),
    QiMiaoMH(56,"奇妙漫画"),
    MiGu(58,"咪咕漫画"),
    Hhxxee(59,"997700"),
    EHentai(60,"EHentai"),
    ChuiXue(69,"吹雪漫画"),
    MH517(70,"我要去漫画"),
    Ohmanhua(71,"oh漫画"),
    JMTT(72,"禁漫天堂"),
    MH50(80,"漫画堆"),
    YKMH(91,"优酷漫画"),
    DmzjFix(100,"动漫之家v2Fix"),
    SixMH(100,"6漫画"),
    HotManga(102,"热辣漫画"),

    ;

    private int code;
    private String desc;

    SourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    private static final Map<Integer, SourceEnum> lookup = new HashMap<>();

    static {
        for (SourceEnum type : EnumSet.allOf(SourceEnum.class)) {
            lookup.put(type.getCode(), type);
        }
    }

    public static SourceEnum get(int code){
        return lookup.get(code);
    }
}
