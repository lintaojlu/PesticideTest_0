package com.example.PesticideTest_0.ui.notifications;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.PesticideTest_0.CalculateActivity;
import com.example.PesticideTest_0.ImageActivity;
import com.example.PesticideTest_0.InputModelActivity;
import com.example.PesticideTest_0.NowModelActivity;
import com.example.PesticideTest_0.R;
import com.example.PesticideTest_0.SelectActivity;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
        Button bt_nowModel = root.findViewById(R.id.bt_nowModel);
        Button bt_newModel = root.findViewById(R.id.bt_newModel);
        Button bt_input_model= root.findViewById(R.id.bt_input_model);

        //查看当前模型
        bt_nowModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), NowModelActivity.class);
                startActivity(intent);
            }
        });

        //新建模型
        bt_newModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectActivity.class);
                startActivity(intent);
            }
        });
        //导入我的模型
        bt_input_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InputModelActivity.class);
                startActivity(intent);
            }
        });
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }
}