package com.csg.asynctaskexamfinal;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements CountDownFragment.OnCountDownFragmentListener {

    private TextView mCountTextView;
    private CountDownFragment mCountDownFragment;
    private CountDownTask mCountDownTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCountTextView = findViewById(R.id.count_text_view);

        mCountDownFragment = new CountDownFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_countdown, mCountDownFragment)
                    .commit();
        }
    }

    @Override
    public void onStartButtonClicked() {
        mCountDownTask = new CountDownTask(new CountDownTask.CountTickListener() {
            @Override
            public void onTick(int count) {
                mCountTextView.setText(count + "");
                mCountDownFragment.setCount(count);
            }
        });
        mCountDownTask.execute();
    }

    @Override
    public void onResetButtonClicked() {
        mCountDownTask.cancel(true);
        mCountDownTask = null;
        //초기화 하면 다시 setting해줘야 하기 때문
        mCountTextView.setText("0");
        mCountDownFragment.setCount(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mCountDownTask.cancel(true);
        mCountDownTask = null;
    }


    static class CountDownTask extends AsyncTask<Void, Integer, Void> {

        interface CountTickListener {
            void onTick(int count);
        }

        private CountTickListener mListener;

        public CountDownTask(CountTickListener listener) {
            mListener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i <= 10; i++) {
                try {
                    Thread.sleep(100);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            mListener.onTick(values[0]);

        }
    }
}
