package com.haleydu.cimoc.ui.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.haleydu.cimoc.R;
import com.haleydu.cimoc.model.Comic;
import com.haleydu.cimoc.model.SearchHistory;

import java.util.List;

import butterknife.BindView;

/**
 * @auther shoulaxiao
 * @date 2024/6/15 14:57
 **/
public class SearchHistoryAdapter extends BaseAdapter<SearchHistory> {


    public SearchHistoryAdapter(Context context, List<SearchHistory> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int offset = parent.getWidth() / 90;
                outRect.set(0, 0, 0, offset);
            }
        };
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        SearchHistory history = mDataSet.get(position);
        SearchHistoryAdapter.SearchHistoryViewHolder viewHolder = (SearchHistoryAdapter.SearchHistoryViewHolder) holder;

        viewHolder.historyKeyword.setText(history.getKeyword());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_history, parent, false);
        return new SearchHistoryAdapter.SearchHistoryViewHolder(view);
    }

    class SearchHistoryViewHolder extends BaseViewHolder {

        @BindView(R.id.search_h_d)
        TextView historyKeyword;

        SearchHistoryViewHolder(View view) {
            super(view);
        }
    }
}
