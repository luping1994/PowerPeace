package net.suntrans.powerpeace.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.FragmentZongheNewBinding;

/**
 * Created by Looney on 2017/10/11.
 */

public class ZongheFragmentNew extends BasedFragment {

    private FragmentZongheNewBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zonghe_new,container,false);
        return binding.getRoot();
    }
}
