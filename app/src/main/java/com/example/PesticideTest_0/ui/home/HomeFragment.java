package com.example.PesticideTest_0.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.PesticideTest_0.AboutActivity;
import com.example.PesticideTest_0.CPWActivity;
import com.example.PesticideTest_0.InputModelActivity;
import com.example.PesticideTest_0.R;
import com.example.PesticideTest_0.signout.Signout;
import com.example.PesticideTest_0.ui.login.LoginActivity;

public class HomeFragment extends Fragment {

//    private SharedPreferences sp;
    private static int LOGIN_REGISTER =3,CPW=4;
    private Button bt_cpw, bt_about,bt_signout;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        //修改密码
        bt_cpw= root.findViewById(R.id.bt_cpw);
        bt_cpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CPWActivity.class);
                startActivityForResult(intent, CPW);
            }
        });
        //关于我们
        bt_about = root.findViewById(R.id.bt_about);
        bt_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
        //退出
        bt_signout=root.findViewById(R.id.bt_signout);
        bt_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signout.finishAllActivity();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加到activity列表
        Signout.addActivity(getActivity());
    }
}