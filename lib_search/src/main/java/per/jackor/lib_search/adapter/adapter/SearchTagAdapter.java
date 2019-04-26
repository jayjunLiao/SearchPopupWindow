package per.jackor.lib_search.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import per.jackor.lib_search.R;

/**
 * Created by Jackor on 2019/4/25.
 * Email: jackor.liao@foxmail.com
 * Description:
 */
public class SearchTagAdapter extends RecyclerView.Adapter<SearchTagAdapter.CustomViewHolder> {
    private List<String> mTagList;
    private int mTextSize;
    public SearchTagAdapter(List<String> tagList) {
        this.mTagList = tagList;
    }
    OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(String itemStr);
    }
    @NonNull
    @Override
    public SearchTagAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tag, viewGroup, false);
        return new CustomViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int positon) {
        if (mTextSize != 0) {
            customViewHolder.tagText.setTextSize(mTextSize);
        }
        customViewHolder.tagText.setText(mTagList.get(positon));
    }

    @Override
    public int getItemCount() {
        return mTagList == null ? 0 : mTagList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public void notifyDataChanged() {
        notifyItemChanged(getItemCount());
    }
    static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tagText;
        OnItemClickListener mItemClickListener;
        public CustomViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            tagText = itemView.findViewById(R.id.tag_text);
            tagText.setOnClickListener(this);
            this.mItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null && v instanceof TextView) {
                mItemClickListener.onItemClick(((TextView)v).getText().toString());
            }
        }
    }

}
