package com.qixiang.searchview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixiang.searchview.R;

/**
 * Created by LiShang on 2019/2/21
 * description：仿微信 创建群组-选择联系人
 */
public class SearchView extends LinearLayout {
    private static final String TAG = "SearchView";
    protected View rootView;
    protected RecyclerView scRecycler;
    protected EditText scEditQuery;
    protected View scLine;
    private int textSize = 14;
    private int textColor = Color.BLACK;
    private CharSequence text;
    private CharSequence hint;
    private int hintColor = Color.GRAY;
    private int bottomLineColor = Color.BLACK;
    private boolean bottomLineShow = true;
    private Drawable searchIcon;
    private final RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {

        private void update() {
            if (scRecycler != null && scRecycler.getAdapter() != null) {
                boolean isShow = true;
                if (scRecycler.getAdapter().getItemCount() == 0) {
                    isShow = true;
                    scRecycler.setVisibility(GONE);
                } else {
                    isShow = false;
                    scRecycler.setVisibility(VISIBLE);
                    scRecycler.scrollToPosition(scRecycler.getAdapter().getItemCount() - 1);
                }
                updateSearchIcon(isShow);
            }
        }

        @Override
        public void onChanged() {
            super.onChanged();
            update();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            update();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            update();
        }
    };
    private int searchMinWidth; //最小長度
    private boolean searchIconShow = true;
    private OnQueryTextListener mOnQueryChangeListener;
    private final TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSubmitQuery();
            }

            return false;
        }
    };
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mOnQueryChangeListener != null) {
                mOnQueryChangeListener.onQueryTextChange(s.toString());
            }
        }
    };
    private OnActionListener mOnActionListener;
    private final OnKeyListener keyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    String newText = scEditQuery.getText().toString();

                    if (mOnActionListener != null) {
                        mOnActionListener.onDelText(newText);
                    }
                }
            }

            return false;
        }
    };

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SearchView);
        int fontSize = ta.getDimensionPixelSize(R.styleable.SearchView_scTextSize, 0);
        if (fontSize != 0) {
            textSize = px2sp(context, fontSize);
        }
        textColor = ta.getColor(R.styleable.SearchView_scTextColor, textColor);
        text = ta.getString(R.styleable.SearchView_scText);
        hint = ta.getString(R.styleable.SearchView_scHint);
        hintColor = ta.getColor(R.styleable.SearchView_scHintColor, hintColor);
        bottomLineColor = ta.getColor(R.styleable.SearchView_scDividerColor, bottomLineColor);
        bottomLineShow = ta.getBoolean(R.styleable.SearchView_scShowDividers, bottomLineShow);
        searchIcon = ta.getDrawable(R.styleable.SearchView_scSearchIcon);
        if (searchIcon == null) {
            searchIcon = context.getResources().getDrawable(android.R.drawable.ic_menu_search);
        }
        searchIconShow = ta.getBoolean(R.styleable.SearchView_scSearchIcon, searchIconShow);
        searchMinWidth = ta.getDimensionPixelSize(R.styleable.SearchView_scSearchMinWidth, 0);
        ta.recycle();
    }

    void onSubmitQuery() {
        CharSequence query = scEditQuery.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null
                    || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                setImeVisibility(false);
            }
        }
    }

    void setImeVisibility(final boolean visible) {

        final InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!visible) {
            imm.hideSoftInputFromWindow(scEditQuery.getWindowToken(), 0);
            return;
        }

        if (imm.isActive(scEditQuery)) {
            // This means that SearchAutoComplete is already connected to the IME.
            // InputMethodManager#showSoftInput() is guaranteed to pass client-side focus check.
            imm.showSoftInput(this, 0);
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(this.getContext(), R.layout.layout_search_view, this);
        rootView = this;
        initView(rootView);
    }

    private void initView(View rootView) {
        scRecycler = (RecyclerView) rootView.findViewById(R.id.sc_recycler);
        scEditQuery = (EditText) rootView.findViewById(R.id.sc_edit_query);
        scLine = (View) rootView.findViewById(R.id.sc_line);

        scRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        scRecycler.setItemAnimator(null);
        scRecycler.setVisibility(GONE);
        setBottomLineShow(bottomLineShow);
        scEditQuery.setText(text);
        scEditQuery.setHint(hint);
        scEditQuery.setTextSize(textSize);
        scEditQuery.setTextColor(textColor);
        scEditQuery.setHintTextColor(hintColor);
        setSearchIconShow(searchIconShow);

        scEditQuery.addTextChangedListener(textWatcher);
        scEditQuery.setOnEditorActionListener(editorActionListener);
        scEditQuery.setOnKeyListener(keyListener);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ViewGroup) {
                childMeasure((ViewGroup) view);
            }
        }
    }

    private void childMeasure(ViewGroup viewGroup) {

        int childCount = viewGroup.getChildCount();
        for (int j = 0; j < childCount; j++) {
            View child = viewGroup.getChildAt(j);

            if (child == scRecycler) {
                Log.e(TAG, "scRecycler  w:" + scRecycler.getMeasuredWidth() + "  h:" + scRecycler.getMeasuredHeight());

                int rootWidth = getMeasuredWidth();
                int width = scRecycler.getMeasuredWidth();
                if (rootWidth - width < searchMinWidth) {
                    setMeasureChildSpec(rootWidth - searchMinWidth, scRecycler.getMeasuredHeight(), scRecycler);
                    setMeasureChildSpec(searchMinWidth, scEditQuery.getMeasuredHeight(), scEditQuery);
                }
                break;
            } else {
                if (child instanceof ViewGroup) {
                    childMeasure((ViewGroup) child);
                    break;
                }
            }
        }
    }

    private void setMeasureChildSpec(int width, int height, View view) {
        int measuredWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int measuredHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        view.measure(measuredWidthSpec, measuredHeightSpec);
    }

    public void setBottomLineShow(boolean show) {
        this.bottomLineShow = show;
        scLine.setBackgroundColor(bottomLineColor);
        if (bottomLineShow) {
            scLine.setVisibility(VISIBLE);
        } else {
            scLine.setVisibility(GONE);
        }
    }

    public void setSearchIconShow(boolean show) {
        this.searchIconShow = show;
        updateSearchIcon(this.searchIconShow);
    }

    private void updateSearchIcon(boolean show) {
        if (show) {
            if (searchIcon != null) {
                final int textSize = (int) (scEditQuery.getTextSize() * 1.25);
                searchIcon.setBounds(0, 0, textSize, textSize);
                scEditQuery.setCompoundDrawables(searchIcon, null, null, null);
            }
        } else {
            scEditQuery.setCompoundDrawables(null, null, null, null);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        scRecycler.setAdapter(adapter);
        if (scRecycler.getAdapter() != null) {
            scRecycler.getAdapter().registerAdapterDataObserver(adapterDataObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    private int px2dip(Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    /**
     * dp转为px
     */
    private int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dipValue
     * @return
     */
    private int dip2px(Context context, float dipValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * density + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    private int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    public void setOnActionListener(OnActionListener listener) {
        this.mOnActionListener = listener;
    }

    @Override
    public void clearFocus() {
        super.clearFocus();
        scEditQuery.clearFocus();
        setImeVisibility(false);
    }

    public interface OnQueryTextListener {

        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public interface OnActionListener {
        void onDelText(String newText);
    }

}
