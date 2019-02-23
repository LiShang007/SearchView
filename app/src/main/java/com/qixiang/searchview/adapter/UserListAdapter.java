package com.qixiang.searchview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.qixiang.searchview.R;
import com.qixiang.searchview.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiShang on 2019/2/23
 * description：用户列表
 */
public class UserListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    OnCheckListener mOnCheckListener;
    private List<User> list = new ArrayList<>();

    public void setData(List<User> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void remove(User user) {
        int position = list.indexOf(user);
        remove(position);
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void notifyItemChanged(User user) {
        int position = getPoistionById(user);
        if (position > -1) {
            notifyItemChanged(position);
        }
    }

    /**
     * 通过用户id查找，避免数据源不同导致集合内不存在用户
     *
     * @param user
     * @return
     */
    private int getPoistionById(User user) {
        for (int i = 0; i < list.size(); i++) {
            User data = list.get(i);
            if (data.id == user.id) {
                data.isCheck = false;
                return i;
            }
        }
        return -1;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name, parent, false);
        return new BaseViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        final User user = list.get(position);

        holder.setText(R.id.txt_name, user.name)
                .setImageResource(R.id.img_head, user.head)
                .setChecked(R.id.checkbox, user.isCheck);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = holder.findViewById(R.id.checkbox);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    user.isCheck = false;
                } else {
                    checkBox.setChecked(true);
                    user.isCheck = true;
                }

                if (mOnCheckListener != null) {
                    mOnCheckListener.onCheck(user, true);
                }
            }
        });


        if (mOnCheckListener != null) {
            mOnCheckListener.onCheck(user, false);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnCheckListener(OnCheckListener listener) {
        mOnCheckListener = listener;
    }

    public interface OnCheckListener {
        void onCheck(User user, boolean isUser);
    }

}
