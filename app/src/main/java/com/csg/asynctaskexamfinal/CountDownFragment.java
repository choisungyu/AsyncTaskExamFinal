package com.csg.asynctaskexamfinal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CountDownFragment extends Fragment {

    private OnCountDownFragmentListener mListener;
    private TextView mCountTextView;

    public CountDownFragment() {
    }

    public void setCount(int count) {
        mCountTextView.setText(count + "");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_count_down, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCountTextView = view.findViewById(R.id.count_text_view);
        view.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStartButtonClicked();
            }
        });

        view.findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onResetButtonClicked();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCountDownFragmentListener) {
            mListener = (OnCountDownFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCountDownFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCountDownFragmentListener {
        void onStartButtonClicked();

        void onResetButtonClicked();
    }
}
