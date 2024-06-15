package com.haleydu.cimoc.manager;

import com.haleydu.cimoc.component.AppGetter;
import com.haleydu.cimoc.model.ComicDao;
import com.haleydu.cimoc.model.SearchHistory;
import com.haleydu.cimoc.model.SearchHistoryDao;
import com.haleydu.cimoc.utils.CollectionUtils;

import java.util.List;

import rx.Observable;

/**
 * @auther shoulaxiao
 * @date 2024/6/15 14:03
 **/
public class HistorySearchManager {

    private static HistorySearchManager mInstance;

    private SearchHistoryDao mSearchHistoryDao;


    public HistorySearchManager(AppGetter getter){
        mSearchHistoryDao = getter.getAppInstance().getDaoSession().getSearchHistoryDao();
    }

    public static HistorySearchManager getInstance(AppGetter getter) {
        if (mInstance == null) {
            synchronized (HistorySearchManager.class) {
                if (mInstance == null) {
                    mInstance = new HistorySearchManager(getter);
                }
            }
        }
        return mInstance;
    }

    public Observable<List<SearchHistory>> listEnableInRx() {
        return mSearchHistoryDao.queryBuilder()
                .where(SearchHistoryDao.Properties.Keyword.isNotNull())
                .orderDesc(SearchHistoryDao.Properties.CreateTime)
                .limit(10)
                .rx()
                .list();
    }

    public Long insert(String keyword) {
        List<SearchHistory> list = mSearchHistoryDao.queryBuilder().where(SearchHistoryDao.Properties.Keyword.eq(keyword))
                .list();
        if (CollectionUtils.isEmpty(list)) {
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setKeyword(keyword);
            searchHistory.setCreateTime(System.currentTimeMillis());
            return mSearchHistoryDao.insert(searchHistory);
        }
        return 0L;
    }
}
