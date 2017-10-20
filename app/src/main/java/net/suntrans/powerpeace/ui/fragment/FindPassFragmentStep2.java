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
import net.suntrans.powerpeace.databinding.FragmentFindps2Binding;
import net.suntrans.powerpeace.databinding.FragmentJihuo2Binding;


/**
 * Created by Looney on 2017/9/27.
 */

public class FindPassFragmentStep2 extends RxFragment {

    private FragmentFindps2Binding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_findps2, container,false);
        return binding.getRoot();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // If used on an activity that doesn't implement MediaFragmentListener, it
        // will throw an exception as expected:
        listener = (FindPassFragmentListener) activity;
    }

    private FindPassFragmentListener listener;

    public interface FindPassFragmentListener {
        void onJihuoClicked();

        void updateTitle(String title);
    }

    @Override
    public void onStart() {
        super.onStart();
        listener.updateTitle(getString(R.string.tx_reset_password));
    }
}
