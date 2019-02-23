package com.qixiang.searchview.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qixiang.searchview.R;
import com.qixiang.searchview.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiShang on 2019/2/23
 * description：选中的用户
 */
public class CheckListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    OnCheckChangeListener mOnCheckChangeListener;
    private List<User> list = new ArrayList<>();
    private int status;//状态 0：正常 1：即将删除

    public void add(User user) {
        if (status == 1) {
            status = 0;
            int start = list.size() - 1;
            if (start > -1) {
                list.add(user);
                notifyItemChanged(start, list.size());
            } else {
                notifyItemInserted(list.size());
            }
        } else {
            list.add(user);
            notifyItemInserted(list.size());
        }
    }

    public void remove(User user) {
        int position = list.indexOf(user);
        remove(position);
    }

    public void remove(int position) {
        status = 0;
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void onActionDel() {
        if (list.isEmpty()) {
            status = 0;
        } else {
            int index = getItemCount() - 1;
            if (index > -1) {
                User user = list.get(index);
                if (status == 0) {
                    status = 1;
                    notifyItemChanged(index);
                } else {
                    remove(user);
                    if (mOnCheckChangeListener != null) {
                        mOnCheckChangeListener.onDelete(user);
                    }
                }
            }
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head, parent, false);
        return new BaseViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        final User user = list.get(position);
        holder.setImageResource(R.id.img_head, user.head);

        if (position == getItemCount() - 1) {

            if (status == 1) {
                ImageView image = holder.findViewById(R.id.img_head);
                Drawable drawable = image.getDrawable();

                Drawable tintDrawable = DrawableCompat.wrap(drawable.getConstantState() == null ?
                        drawable : drawable.getConstantState().newDrawable()).mutate();

                DrawableCompat.setTintMode(tintDrawable, PorterDuff.Mode.SCREEN);
                DrawableCompat.setTint(tintDrawable, Color.parseColor("#D9D9D9"));
                image.setImageDrawable(tintDrawable);

            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(user);
                if (mOnCheckChangeListener != null) {
                    mOnCheckChangeListener.onDelete(user);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnCheckChangeListener(OnCheckChangeListener listener) {
        mOnCheckChangeListener = listener;
    }

    public interface OnCheckChangeListener {
        void onDelete(User user);

    }

}
