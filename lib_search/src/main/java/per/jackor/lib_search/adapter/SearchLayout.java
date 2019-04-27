package per.jackor.lib_search.adapter;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import per.jackor.lib_search.R;
import per.jackor.lib_search.adapter.adapter.SearchTagAdapter;
import per.jackor.lib_search.adapter.util.DensityUtil;
import per.jackor.lib_search.adapter.widget.RecyclerViewItemDecoration;
import per.jackor.lib_search.adapter.widget.SupportPopupWindow;

/**
 * Created by Jackor on 2019/4/25.
 * Email: jackor.liao@foxmail.com
 * Description:
 */
public class SearchLayout implements View.OnClickListener, SearchTagAdapter.OnItemClickListener {
    public static final int HEADER_HISTORY = 0x01;
    public static final int HEADER_HOT = 0x0;
    List<String> mHistoryList = new ArrayList<>();
    List<String> mHotList = new ArrayList<>();
    private SearchTagAdapter mHistoryAdapter;
    private SearchTagAdapter mHotAdapter;
    // 标签之间的间隔
    private int mItemGapWidth = 100;
    private int mBgColor;
    private int mTextSize;

    private int width;
    private int height;

    private RecyclerView mHistoryRecycler;
    private RecyclerView mHotRecycler;
    private View mHistoryHeader;
    private View mHotHeader;

    private Context mContext;
    private PopupWindow mPopupWindow;
    private EditText mEditQuery;

    private OnSearchClickListener mOnSearchClickListener;
    private Handler mHandler = new Handler();
    private int mAnimationStyle;

    public interface OnSearchClickListener{
        void onSearchClick(String searchContent);
    }
    public SearchLayout(Context context) {
        this.mContext = context;
    }

    public void pop(View locationView, List<String> historyContent, List<String> hotContent) {
        if (mPopupWindow == null) {
            createPopupWindow(locationView, historyContent, hotContent);
        } else {

            /**
             * 判断传入的数据是否为null，如果null，就隐藏历史搜索/热门搜索头部，如果不为null,就刷新RecyclerView
             */
            if (historyContent != null) {
                mHistoryList.clear();
                mHistoryList.addAll(historyContent);
                mHistoryAdapter.notifyDataChanged();
            } else {
                setHeaderVisibility(View.GONE, HEADER_HISTORY);
            }
            if (hotContent != null) {
                mHotList.clear();
                mHotList.addAll(hotContent);
                mHotAdapter.notifyDataChanged();
            } else {
                setHeaderVisibility(View.GONE, HEADER_HOT);
            }

            /**
             * 如果之前因为传入数据为null导致历史搜索/热门搜索头部隐藏，这里需要重新设置VISIBLE
             */
            if (mHistoryHeader.getVisibility() == View.GONE && mHistoryList.size() > 0) {
                setHeaderVisibility(View.VISIBLE, HEADER_HISTORY);
            }
            if (mHotRecycler.getVisibility() == View.GONE && mHotList.size() > 0) {
                setHeaderVisibility(View.VISIBLE, HEADER_HOT);
            }

            mPopupWindow.update();
            mPopupWindow.showAtLocation(locationView, Gravity.CENTER, 0,0);
        }
        // 解决edittext获取焦点但不弹出软键盘问题
        popupInputMethodWindow();
    }

    public void setOnSearchClickListener(OnSearchClickListener searchClickListener) {
        this.mOnSearchClickListener = searchClickListener;
    }

    private void createPopupWindow(View locationView, List<String> historyContent, List<String> hotContent) {
        // 获取屏幕宽高,没有设置宽高时，默认popupwindow=屏幕大小
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (this.width == 0) {
            this.width = dm.widthPixels;
        }
        if (this.height == 0) {
            this.height = dm.heightPixels;
        }

        // 初始化pupupWindow的各个View，
        View pupView = LayoutInflater.from(mContext).inflate(R.layout.search_view, null);
        ImageView back = pupView.findViewById(R.id.back);
        ImageView clearHistory = pupView.findViewById(R.id.clear_history);
        TextView search = pupView.findViewById(R.id.search);
        mHistoryHeader = pupView.findViewById(R.id.history);
        mHotHeader = pupView.findViewById(R.id.hot);
        mEditQuery = pupView.findViewById(R.id.edit_query);

        mHistoryRecycler = pupView.findViewById(R.id.history_content);
        mHotRecycler = pupView.findViewById(R.id.hot_content);
        mHistoryRecycler.setHasFixedSize(true);
        flexboxConfig(mHistoryRecycler);
        flexboxConfig(mHotRecycler);
        if (historyContent != null) {
            mHistoryList.addAll(historyContent);
        } else {
            setHeaderVisibility(View.GONE, HEADER_HISTORY);
        }
        if (hotContent != null) {
            mHotList.addAll(hotContent);
        } else {
            setHeaderVisibility(View.GONE, HEADER_HOT);
        }
        mHistoryAdapter = new SearchTagAdapter(mHistoryList);
        mHotAdapter = new SearchTagAdapter(mHotList);
        mHotAdapter.setTextSize(mTextSize);
        mHistoryAdapter.setTextSize(mTextSize);
        mHistoryRecycler.setAdapter(mHistoryAdapter);
        mHotRecycler.setAdapter(mHotAdapter);

        // popupWindow设置
        mPopupWindow = new SupportPopupWindow(pupView, width, height);
        if (mBgColor == 0) {
            mBgColor = ContextCompat.getColor(mContext, R.color.write);
        }
        if (mAnimationStyle != 0) {
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(mBgColor));
        // 设置可以获取焦点
        mPopupWindow.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        mPopupWindow.setOutsideTouchable(true);
        // 更新popupwindow的状态
        mPopupWindow.update();
        mPopupWindow.showAtLocation(locationView, Gravity.CENTER, 0,0);

        mHistoryAdapter.setOnItemClickListener(this);
        mHotAdapter.setOnItemClickListener(this);
        back.setOnClickListener(this);
        clearHistory.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    /**
     * Flexbox流式布局设置
     * @param recyclerView
     */
    private void flexboxConfig(RecyclerView recyclerView){
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        // 设置item间隔
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(DensityUtil.px2dip(mContext, mItemGapWidth)));
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 设置item间距
     * @param gapWidth
     */
    public void setItemGapWidth(int gapWidth) {
        this.mItemGapWidth = gapWidth;
    }

    /**
     * popupWindow背景色
     * @param colorStr
     */
    public void setBackgroundColor(String colorStr) {
        this.mBgColor = Color.parseColor(colorStr);
    }

    public void setBackgroundColor(int color) {
        this.mBgColor = color;
    }

    public void setPopupWindowWidth(int width) {
        this.width = width;
    }

    public void setPopupWindowHeight(int height) {
        this.height = height;
    }

    public void setAnimationStyle(int animationStyle) {
        this.mAnimationStyle = animationStyle;
    }

    /**
     * 设置标签字体大小
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    /**
     * 隐藏历史搜索/热门搜索头部
     * @param visibility
     * @param tag {@link #HEADER_HISTORY}, {@link #HEADER_HOT}
     */
    private void setHeaderVisibility(int visibility, int tag) {
        if (tag == HEADER_HISTORY) {
            mHistoryHeader.setVisibility(visibility);
            mHistoryRecycler.setVisibility(visibility);
        } else {
            mHotHeader.setVisibility(visibility);
            mHotRecycler.setVisibility(visibility);
        }
    }

    /**
     * 点击某个标签，将标签的String放入搜索框
     * @param itemStr
     */
    @Override
    public void onItemClick(String itemStr) {
        mEditQuery.setText(itemStr);
        mEditQuery.setSelection(mEditQuery.getText().length());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            mPopupWindow.dismiss();

        } else if (i == R.id.clear_history) {
            mHistoryList.clear();
            mHistoryAdapter.notifyDataChanged();
            setHeaderVisibility(View.GONE, HEADER_HISTORY);

        } else if (i == R.id.search) {
            Editable editQueryable = mEditQuery.getText();
            String editQueryText = "";
            if (editQueryable != null) {
                editQueryText = editQueryable.toString();
            }
            mOnSearchClickListener.onSearchClick(editQueryText);
            mEditQuery.setText("");
            mPopupWindow.dismiss();

        }
    }
    // 弹出软键盘
    private void popupInputMethodWindow() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

}
