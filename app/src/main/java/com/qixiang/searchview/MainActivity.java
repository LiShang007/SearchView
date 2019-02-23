package com.qixiang.searchview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.qixiang.searchview.adapter.CheckListAdapter;
import com.qixiang.searchview.adapter.UserListAdapter;
import com.qixiang.searchview.entity.User;
import com.qixiang.searchview.view.SearchView;

public class MainActivity extends AppCompatActivity {

    protected SearchView searchPlate;
    protected RecyclerView recyclerView;


    private UserListAdapter adapter;
    private CheckListAdapter checkListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
        initListener();
    }


    private void initView() {
        searchPlate = (SearchView) findViewById(R.id.search_plate);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new UserListAdapter();
        adapter.setData(TestData.getData(this));
        recyclerView.setAdapter(adapter);

        checkListAdapter = new CheckListAdapter();
        searchPlate.setAdapter(checkListAdapter);


    }


    private void initListener() {
        //搜索
        searchPlate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("onQueryTextSubmit", "query:" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("onQueryTextChange", "newText:" + newText);
                return false;
            }
        });

        //刪除
        searchPlate.setOnActionListener(new SearchView.OnActionListener() {
            @Override
            public void onDelText(String newText) {
                Log.e("onDelText", "newText:" + newText);
                if (TextUtils.isEmpty(newText)) {
                    checkListAdapter.onActionDel();
                }
            }
        });
        //用户列表 选择更改
        adapter.setOnCheckListener(new UserListAdapter.OnCheckListener() {
            @Override
            public void onCheck(User user, boolean isUser) {
                if (!isUser) return; //不是用户操作
                if (user.isCheck) {
                    checkListAdapter.add(user);
                } else {
                    checkListAdapter.remove(user);
                }
            }
        });

        //选中用户--删除
        checkListAdapter.setOnCheckChangeListener(new CheckListAdapter.OnCheckChangeListener() {
            @Override
            public void onDelete(User user) {
                adapter.notifyItemChanged(user);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        searchPlate.clearFocus();
    }
}
