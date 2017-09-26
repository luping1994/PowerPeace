package net.suntrans.powerpeace.ui.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.FragmentFindps1Binding;
import net.suntrans.powerpeace.databinding.FragmentJihuo1Binding;


/**
 * Created by Looney on 2017/9/27.
 */

public class FindPassFragment1 extends RxFragment {

    private FragmentFindps1Binding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_findps1, container, false);
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
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // If used on an activity that doesn't implement MediaFragmentListener, it
        // will throw an exception as expected:
        listener = (FindPassFragment1Listener) activity;
    }

    private FindPassFragment1Listener listener;

    public interface FindPassFragment1Listener {
        void onNextClicked();

        void updateTitle(String title);
    }

    @Override
    public void onStart() {
        super.onStart();
        listener.updateTitle("验证身份");
    }
}
