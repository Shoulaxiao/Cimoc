package com.haleydu.cimoc.ui.activity;

import android.os.Bundle;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.haleydu.cimoc.R;
import com.haleydu.cimoc.manager.PreferenceManager;
import com.haleydu.cimoc.misc.Switcher;
import com.haleydu.cimoc.model.SearchHistory;
import com.haleydu.cimoc.model.Source;
import com.haleydu.cimoc.presenter.BasePresenter;
import com.haleydu.cimoc.presenter.SearchPresenter;
import com.haleydu.cimoc.ui.adapter.AutoCompleteAdapter;
import com.haleydu.cimoc.ui.adapter.BaseAdapter;
import com.haleydu.cimoc.ui.adapter.SearchHistoryAdapter;
import com.haleydu.cimoc.ui.fragment.dialog.MultiAdpaterDialogFragment;
import com.haleydu.cimoc.ui.view.SearchView;
import com.haleydu.cimoc.utils.CollectionUtils;
import com.haleydu.cimoc.utils.HintUtils;
import com.haleydu.cimoc.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Hiroshi on 2016/10/11.
 */

public class SearchActivity extends BackActivity implements SearchView, TextView.OnEditorActionListener, BaseAdapter.OnItemClickListener {

    private final static int DIALOG_REQUEST_SOURCE = 0;

    @BindView(R.id.search_text_layout)
    TextInputLayout mInputLayout;
    @BindView(R.id.search_keyword_input)
    AppCompatAutoCompleteTextView mEditText;
    @BindView(R.id.search_action_button)
    FloatingActionButton mActionButton;
    @BindView(R.id.search_strict_checkbox)
    AppCompatCheckBox mCheckBox;
    @BindView(R.id.search_history_recycler_view)
    RecyclerView historyRecyclerView;

    private ArrayAdapter<String> mArrayAdapter;

    private SearchPresenter mPresenter;
    private List<Switcher<Source>> mSourceList;
    private boolean mAutoComplete;

    private FlexboxLayoutManager layoutManager;

    private SearchHistoryAdapter mSearchHistoryAdapter;


    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new SearchPresenter();
        mPresenter.attachView(this);
        return mPresenter;
    }

    @Override
    protected void initView() {
        mAutoComplete = mPreference.getBoolean(PreferenceManager.PREF_SEARCH_AUTO_COMPLETE, false);
        layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);

        mSearchHistoryAdapter = new SearchHistoryAdapter(this, new ArrayList<>());
        mSearchHistoryAdapter.setOnItemClickListener(this);
        historyRecyclerView.setLayoutManager(layoutManager);
        historyRecyclerView.addItemDecoration(mSearchHistoryAdapter.getItemDecoration());
        historyRecyclerView.setAdapter(mSearchHistoryAdapter);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mActionButton != null && !mActionButton.isShown()) {
                    mActionButton.show();
                }
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAutoComplete) {
                    String keyword = mEditText.getText().toString();
                    if (!StringUtils.isEmpty(keyword)) {
                        mPresenter.loadAutoComplete(keyword);
                    }
                }
            }
        });
        mEditText.setOnEditorActionListener(this);
        if (mAutoComplete) {
            mArrayAdapter = new AutoCompleteAdapter(this);
            mEditText.setAdapter(mArrayAdapter);
        }
    }

    @Override
    protected void initData() {
        mSourceList = new ArrayList<>();
        // 加载图源
        mPresenter.loadSource();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu_source:
                if (!mSourceList.isEmpty()) {
                    int size = mSourceList.size();
                    String[] arr1 = new String[size];
                    boolean[] arr2 = new boolean[size];
                    for (int i = 0; i < size; ++i) {
                        arr1[i] = mSourceList.get(i).getElement().getTitle();
                        arr2[i] = mSourceList.get(i).isEnable();
                    }
                    MultiAdpaterDialogFragment fragment =
                            MultiAdpaterDialogFragment.newInstance(R.string.search_source_select, arr1, arr2, DIALOG_REQUEST_SOURCE);
                    fragment.show(getSupportFragmentManager(), null);
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogResult(int requestCode, Bundle bundle) {
        switch (requestCode) {
            case DIALOG_REQUEST_SOURCE:
                boolean[] check = bundle.getBooleanArray(EXTRA_DIALOG_RESULT_VALUE);
                if (check != null) {
                    int size = mSourceList.size();
                    for (int i = 0; i < size; ++i) {
                        mSourceList.get(i).setEnable(check[i]);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mActionButton.performClick();
            return true;
        }
        return false;
    }

    @OnClick(R.id.search_action_button)
    void onSearchButtonClick() {
        String keyword = mEditText.getText().toString();
        mPresenter.insertOrUpdateSearchHistory(keyword);

        Boolean strictSearch = mCheckBox.isChecked();
        if (StringUtils.isEmpty(keyword)) {
            mInputLayout.setError(getString(R.string.search_keyword_empty));
        } else {
            goToSearch(keyword, strictSearch);
        }
    }

    private void goToSearch(String keyword, boolean strictSearch) {
        ArrayList<Integer> list = new ArrayList<>();
        for (Switcher<Source> switcher : mSourceList) {
            if (switcher.isEnable()) {
                list.add(switcher.getElement().getType());
            }
        }
        if (list.isEmpty()) {
            HintUtils.showToast(this, R.string.search_source_none);
        } else {
            startActivity(ResultActivity.createIntent(this, keyword, strictSearch,
                    CollectionUtils.unbox(list), ResultActivity.LAUNCH_MODE_SEARCH));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadSearchHistory();
    }

    @Override
    public void onAutoCompleteLoadSuccess(List<String> list) {
        mArrayAdapter.clear();
        mArrayAdapter.addAll(list);
    }

    @Override
    public void onSourceLoadSuccess(List<Source> list) {
        hideProgressBar();
        for (Source source : list) {
            mSourceList.add(new Switcher<>(source, true));
        }
    }

    @Override
    public void onSourceLoadFail() {
        hideProgressBar();
        HintUtils.showToast(this, R.string.search_source_load_fail);
    }

    @Override
    protected String getDefaultTitle() {
        return getString(R.string.comic_search);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    protected boolean isNavTranslation() {
        return true;
    }

    @Override
    public void onHistoryLoadSuccess(List<SearchHistory> list) {
        mSearchHistoryAdapter.clear();
        mSearchHistoryAdapter.addAll(list);
    }

    @Override
    public void onHistoryLoadFail(Throwable throwable) {
        Log.d("", throwable.getMessage());
    }

    @Override
    public void onItemClick(View view, int position) {
        SearchHistory item = mSearchHistoryAdapter.getItem(position);
        mPresenter.insertOrUpdateSearchHistory(item.getKeyword());

        goToSearch(item.getKeyword(), true);

    }
}
