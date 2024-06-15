package com.haleydu.cimoc.ui.view;

import com.haleydu.cimoc.component.DialogCaller;
import com.haleydu.cimoc.model.SearchHistory;
import com.haleydu.cimoc.model.Source;

import java.util.List;

/**
 * Created by Hiroshi on 2016/10/11.
 */

public interface SearchView extends BaseView, DialogCaller {

    void onSourceLoadSuccess(List<Source> list);

    void onSourceLoadFail();

    void onAutoCompleteLoadSuccess(List<String> list);


    void onHistoryLoadSuccess(List<SearchHistory> list);

    void onHistoryLoadFail(Throwable throwable);

}
