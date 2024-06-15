package com.haleydu.cimoc.presenter;

import com.haleydu.cimoc.core.Manga;
import com.haleydu.cimoc.manager.HistorySearchManager;
import com.haleydu.cimoc.manager.SourceManager;
import com.haleydu.cimoc.ui.view.SearchView;
import com.haleydu.cimoc.utils.StringUtils;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Hiroshi on 2016/10/11.
 */

public class SearchPresenter extends BasePresenter<SearchView> {

    private SourceManager mSourceManager;

    private HistorySearchManager mHistorySearchManager;

    @Override
    protected void onViewAttach() {
        mSourceManager = SourceManager.getInstance(mBaseView);
        mHistorySearchManager = HistorySearchManager.getInstance(mBaseView);
    }

    public void loadSource() {
        mCompositeSubscription.add(mSourceManager.listEnableInRx()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> mBaseView.onSourceLoadSuccess(list), throwable -> mBaseView.onSourceLoadFail()));
    }

    public void loadAutoComplete(String keyword) {
        mCompositeSubscription.add(Manga.loadAutoComplete(keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> mBaseView.onAutoCompleteLoadSuccess(list), throwable -> throwable.printStackTrace()));
    }


    public void loadSearchHistory() {
        mCompositeSubscription.add(mHistorySearchManager.listHistoryInRx()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> mBaseView.onHistoryLoadSuccess(list), throwable -> mBaseView.onHistoryLoadFail(throwable)));

    }


    public void insertOrUpdateSearchHistory(String keyword) {
        if (StringUtils.isEmpty(keyword)){
            return;
        }
        mHistorySearchManager.insertOrUpdate(keyword);
    }

}
