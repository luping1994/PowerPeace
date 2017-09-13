package net.suntrans.powerpeace.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.R;

/**
 * Created by Looney on 2017/9/13.
 */

public class ChangeNameFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_name, null);
        final EditText editText = (EditText) view.findViewById(R.id.name);
        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editText.getText().toString();
                        if (TextUtils.isEmpty(name)){
                            UiUtils.showToast("名称不能为空!");
                            return;
                        }
                        listener.changeName(name);
                    }
                })
                .setNegativeButton("取消", null);
        return builder.create();
    }

    private ChangeNameListener listener;

    public ChangeNameListener getListener() {
        return listener;
    }

    public void setListener(ChangeNameListener listener) {
        this.listener = listener;
    }

    public interface ChangeNameListener {
        void changeName(String name);
    }
}
