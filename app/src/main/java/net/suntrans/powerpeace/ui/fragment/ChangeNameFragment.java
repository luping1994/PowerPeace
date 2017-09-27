package net.suntrans.powerpeace.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.R;

/**
 * Created by Looney on 2017/9/13.
 */

public class ChangeNameFragment extends DialogFragment {
    private static final String TYPE="type";
    private static final String MESSAGE="message";
    private int type = TYPE_SINGLE_TEXT;
    private String message;



    public static final int TYPE_SINGLE_TEXT = 0;
    public static final int TYPE_PASSWORD_TEXT = 1;

    public static ChangeNameFragment newInstance(int type,String message){
        ChangeNameFragment fragment= new ChangeNameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,type);
        bundle.putString(MESSAGE,message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments!=null){
            type = arguments.getInt(TYPE);
            message = arguments.getString(MESSAGE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "";


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_name, null);
        final EditText editText = (EditText) view.findViewById(R.id.name);
        final EditText name1 = (EditText) view.findViewById(R.id.name1);
        final EditText name2 = (EditText) view.findViewById(R.id.name2);

        switch (type){
            case TYPE_PASSWORD_TEXT:
                title = "修改密码";
                editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setHint("请输入旧密码");
                name1.setVisibility(View.VISIBLE);
                name2.setVisibility(View.VISIBLE);
                break;
            case TYPE_SINGLE_TEXT:
                title = "请输入要更改的手机号码";
                break;
        }
        builder.setView(view)
                .setMessage(title)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editText.getText().toString();
                        String name11 = name1.getText().toString();
                        String name22 = name2.getText().toString();
                        if (TextUtils.isEmpty(name)){
                            UiUtils.showToast("不能为空!");
                            return;
                        }
                        if (listener!=null)
                        listener.changeName(name,name11,name22);
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
        void changeName(String... name);
    }
}
