package com.zhiwailife.www.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.zhiwailife.www.myapplication.Database;
import com.zhiwailife.www.myapplication.MD5Utils;
import com.zhiwailife.www.myapplication.Open;
import com.zhiwailife.www.myapplication.R;

import org.litepal.LitePal;


import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {
    Button btnOpen;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btnOpen=root.findViewById(R.id.button_open_door);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open.open();
                Database database=new Database();
                database.setDormitory("327");
                database.setPasssword_MD5(MD5Utils.getMD5Code("123456"));
                database.save();
                LitePal.deleteAll(Database.class,"id>1");
                Log.d("123456", "dormitory name is "+LitePal.find(Database.class, 1).getDormitory());
                Log.d("123456", "password name is "+LitePal.find(Database.class, 1).getPasssword_MD5());

            }
        });
        return root;
    }
}