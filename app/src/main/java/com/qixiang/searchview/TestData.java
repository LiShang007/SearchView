package com.qixiang.searchview;

import android.content.Context;
import android.content.res.TypedArray;

import com.qixiang.searchview.entity.User;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiShang on 2019/2/23
 * description：测试数据
 */
public class TestData {

    public static List<User> getData(Context context) {
        TypedArray ar = context.getResources().obtainTypedArray(R.array.drawable_array);
        List<User> list = new ArrayList<>();

        final int len = ar.length();
        for (int i = 0; i < len; i++) {
            User user = new User();
            user.id = i;
            user.head = ar.getResourceId(i, 0);
            user.name = getName();

            list.add(user);
        }
        ar.recycle();
        return list;
    }


    //获得汉字名字
    public static String getName() {
        String name = "";
        int chineseNameNum = (int) (Math.random() * 3 + 2);
        for (int i = 1; i <= chineseNameNum; i++) {
            name += getChinese();
        }
        return name;
    }

    //获得单个汉字
    public static String getChinese() {
        String chinese = "";
        int i = (int) (Math.random() * 40 + 16);
        int j = (int) (Math.random() * 94 + 1);
        if (i == 55) {
            j = (int) (Math.random() * 89 + 1);
        }
        byte[] bytes = {(byte) (i + 160), (byte) (j + 160)};
        try {
            chinese = new String(bytes, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return chinese;
    }


}
