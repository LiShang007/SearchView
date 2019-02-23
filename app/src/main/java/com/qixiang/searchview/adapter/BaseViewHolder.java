package com.qixiang.searchview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


/**
 * Created by LiShang on 2016/7/27.
 * 通用holder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views;
    private final Context context;
    private View convertView;
    private int viewType;

    public BaseViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        this.views = new SparseArray<View>();
        convertView = view;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public <T extends View> T findViewById(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setTextSize(int viewId, float size) {
        TextView view = findViewById(viewId);
        view.setTextSize(size);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int color) {
        TextView view = findViewById(viewId);
        view.setTextColor(color);
        return this;
    }


    public BaseViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = findViewById(viewId);
        imageView.setImageResource(resId);
        return this;
    }


    @SuppressLint("WrongConstant")
    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = findViewById(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    @SuppressLint("WrongConstant")
    public BaseViewHolder setINVisible(int viewId, boolean visible) {
        View view = findViewById(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    public BaseViewHolder setBackgroundResource(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public BaseViewHolder setBackgroundColor(int viewId, int color) {
        View view = findViewById(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        findViewById(viewId).setOnLongClickListener(listener);
        return this;
    }

    public BaseViewHolder setPadding(int viewId, int left, int top, int right, int bottom) {
        findViewById(viewId).setPadding(left, top, right, bottom);
        return this;
    }

    public BaseViewHolder setRating(int viewId, float rating) {
        RatingBar ratingBar = findViewById(viewId);
        ratingBar.setRating(rating);
        return this;
    }

    public BaseViewHolder setEnabled(int viewId, boolean enabled) {
        findViewById(viewId).setEnabled(enabled);
        return this;
    }

    public BaseViewHolder setChecked(int viewId, boolean checked) {
        CheckBox checkBox = findViewById(viewId);
        checkBox.setChecked(checked);
        return this;
    }

}
