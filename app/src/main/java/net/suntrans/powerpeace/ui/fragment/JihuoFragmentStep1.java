package net.suntrans.powerpeace.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.FragmentJihuo1Binding;
import net.suntrans.powerpeace.ui.activity.JihuoActivity;

import java.util.List;


/**
 * Created by Looney on 2017/9/27.
 */

public class JihuoFragmentStep1 extends RxFragment {

    private FragmentJihuo1Binding binding;
    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            binding.getVerify.setText(millisUntilFinished/1000 + "秒");
        }

        @Override
        public void onFinish() {
            binding.getVerify.setEnabled(true);
            binding.getVerify.setText(getString(R.string.tx_get_verfiy));
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jihuo1, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNextClicked();
            }
        });

        binding.getVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.getVerify.setEnabled(false);
                timer.start();
            }
        });

    }


    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // If used on an activity that doesn't implement MediaFragmentListener, it
        // will throw an exception as expected:
        listener = (JihuoFragment1Listener) activity;
    }

    private JihuoFragment1Listener listener;

    public interface JihuoFragment1Listener {
        void onNextClicked();

        void updateTitle(String title);
    }

    @Override
    public void onStart() {
        super.onStart();
        listener.updateTitle(getString(R.string.verfidy_phone));
    }
}
