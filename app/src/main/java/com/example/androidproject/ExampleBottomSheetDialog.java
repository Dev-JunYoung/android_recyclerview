package com.example.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ExampleBottomSheetDialog extends BottomSheetDialogFragment {

    // 초기변수 설정
    private View view;
    // 인터페이스 변수
    private BottomSheetListener mListener;
    // 바텀시트 숨기기 버튼
    private Button btn_hide_bt_sheet;
    TextView tv_ing_AccuracySpeed, tv_ing_maxHeight, tv_ing_minHeight; //tv_ing_time,tv_ing_step,tv_ing_speed,tv_ing_height,tv_ing_distance,
    static TextView tv_ing_time,tv_ing_step,tv_ing_speed,tv_ing_height,tv_ing_distance;
    TextView tv_latitude, tv_longitude;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottome_sheet, container, false);
        return view;
    }

    // 부모 액티비티와 연결하기위한 인터페이스
    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
}
