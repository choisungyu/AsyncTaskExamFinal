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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCountDownEvent(CountDownEvent event) {
        //AsyncTask에서 하던것을 데려옴
        mCountTextView.setText(event.count + "");
        //fragment에서 setText한 것
        mCountDownFragment.setCount(event.count);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

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
        mCountDownTask = new CountDownTask();
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

    static class CountDownEvent {
        int count;
    }

    static class CountDownTask extends AsyncTask<Void, Integer, Void> {
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

            CountDownEvent event = new CountDownEvent();
            // 값을 넣고
            event.count = values[0];
            // 쏘기(이벤트발생)
            EventBus.getDefault().post(event);

            //main에서의 setText한것
//            mCountTextView.setText(values[0] + "");
//            //fragment에서 setText한 것
//            mCountDownFragment.setCount(values[0]);
        }
    }
}
