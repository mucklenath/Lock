package com.example.lockscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.lockscreen.service.LockscreenViewService;

public class LockscreenActivity extends Activity {
    private final String TAG = "LockscreenActivity";
    private static Context sLockscreenActivityContext = null;
    private RelativeLayout mLockscreenMainLayout = null;

    private static SendMessageHandler mMainHandler = null;

    public PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        sLockscreenActivityContext = this;
        mMainHandler = new SendMessageHandler();

        getWindow().setType(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        initLockScreenUi();

        setLockGuard();
    }

    private class SendMessageHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finishLockscreenAct();
        }
    }

    private void finishLockscreenAct() {
        finish();
    }

    private void initLockScreenUi() {
        setContentView(R.layout.activity_lockscreen);
        mLockscreenMainLayout = (RelativeLayout) findViewById(R.id.lockscreen_main_layout);
        mLockscreenMainLayout.getBackground().setAlpha(15);
    }

    private void setLockGuard() {
        boolean isLockEnable;
        isLockEnable = LockscreenUtil.getInstance(sLockscreenActivityContext).isStandardKeyguardState();

        Intent startLockscreenIntent = new Intent(this, LockscreenViewService.class);
        startService(startLockscreenIntent);

        boolean isSoftkeyEnable = LockscreenUtil.getInstance(sLockscreenActivityContext).isSoftKeyAvail(this);
        SharedPreferencesUtil.setBoolean(Lockscreen.ISSOFTKEY, isSoftkeyEnable);
        if (!isSoftkeyEnable) {
            mMainHandler.sendEmptyMessage(0);
        } else if (isSoftkeyEnable) {
            if (isLockEnable) {
                mMainHandler.sendEmptyMessage(0);
            }
        }
    }

}
