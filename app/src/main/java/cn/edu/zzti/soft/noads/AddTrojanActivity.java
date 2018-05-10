package cn.edu.zzti.soft.noads;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.greenrobot.eventbus.EventBus;

import cn.edu.zzti.soft.noads.bean.MessageEvent;
import cn.edu.zzti.soft.noads.sql.model.TrojanData;
import cn.edu.zzti.soft.noads.utils.Constant;
import cn.edu.zzti.soft.noads.utils.StringUtil;
import cn.edu.zzti.soft.noads.utils.ToastUtil;

public class AddTrojanActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup mRgType;
    private int type = -1;
    private EditText mEtText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trojan);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEtText = findViewById(R.id.et_path);
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == -1){
                    ToastUtil.showToast(AddTrojanActivity.this,"请输入选择屏蔽类型");
                    return;
                }
                String path = mEtText.getText().toString();
                if(TextUtils.isEmpty(path)){
                    ToastUtil.showToast(AddTrojanActivity.this,"地址不能为空");
                    return;
                }
                if (StringUtil.isHttpUrl(path)){
                    ToastUtil.showToast(AddTrojanActivity.this,"地址不合法");
                    return;
                }
                TrojanData data = new TrojanData();
                data.setPath(path);
                data.setType(type);
                data.setSource(1);
                data.save();
                EventBus.getDefault().post(new MessageEvent(data, Constant.Event.EVENT_ADD_TROJAN_PATH));
                type = -1;
                mEtText.setText("");
                mRgType.clearCheck();
            }
        });
        mRgType = findViewById(R.id.rg_type);
        mRgType.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_trojan:
                type = 1;
                break;
            case R.id.rb_bad:
                type = 2;
                break;
        }
    }
}
