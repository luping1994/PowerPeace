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
import android.widget.EditText;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.interf.TextChangeListener;

/**
 * Created by Looney on 2017/9/13.
 */

public class ChangeNameFragment extends DialogFragment {
    private static final String TYPE = "type";
    private static final String MESSAGE = "message";
    private int type = TYPE_SINGLE_TEXT;
    private String message;


    public static final int TYPE_SINGLE_TEXT = 0;
    public static final int TYPE_PASSWORD_TEXT = 1;

    public static ChangeNameFragment newInstance(int type, String message) {
        ChangeNameFragment fragment = new ChangeNameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putString(MESSAGE, message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt(TYPE);
            message = arguments.getString(MESSAGE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_name, null);
        final EditText editText = (EditText) view.findViewById(R.id.name);
        final EditText name1 = (EditText) view.findViewById(R.id.name1);
        final EditText name2 = (EditText) view.findViewById(R.id.name2);

//        switch (type) {
//            case TYPE_PASSWORD_TEXT:
//                title = "修改密码";
//                editText.setHint("请输入旧密码");
//                editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
//                name1.setVisibility(View.VISIBLE);
//                name2.setVisibility(View.VISIBLE);
//                break;
//            case TYPE_SINGLE_TEXT:
//                title = "请输入要更改的手机号码";
//                editText.setHint("请输入手机号码");
//                editText.setInputType(InputType.TYPE_CLASS_PHONE);
//                name1.setVisibility(View.GONE);
//                name2.setVisibility(View.GONE);
//                break;
//        }

        builder.setView(view)
                .setMessage(R.string.title_modify_phone)
                .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editText.getText().toString();
                        String name11 = name1.getText().toString();
                        String name22 = name2.getText().toString();
                        if (TextUtils.isEmpty(name)) {
                            UiUtils.showToast(getString(R.string.not_empty));
                            return;
                        }
                        if (listener != null)
                            listener.changeName(name, name11, name22);
                    }
                })
                .setNegativeButton(R.string.qvxiao, null);
        return builder.create();
    }

    private TextChangeListener listener;

    public TextChangeListener getListener() {
        return listener;
    }

    public void setListener(TextChangeListener listener) {
        this.listener = listener;
    }


}
