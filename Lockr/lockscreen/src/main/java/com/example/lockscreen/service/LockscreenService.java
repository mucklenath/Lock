package com.example.lockscreen.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.lockscreen.Lockscreen;
import com.example.lockscreen.LockscreenActivity;
import com.example.lockscreen.LockscreenUtil;

public class LockscreenService extends Service {
    private final String TAG = "LockscreenService";
    private int mServiceStartId = 0;
    private Context mContext = null;


    private BroadcastReceiver mLockscreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != context) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    Intent startLockscreenIntent = new Intent(mContext, LockscreenViewService.class);
                    stopService(startLockscreenIntent);
                    TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    boolean isPhoneIdle = tManager.getCallState() == TelephonyManager.CALL_STATE_IDLE;
                    if (isPhoneIdle) {
                        startLockscreenActivity();
                    }
                }
            }
        }
    };

    public class StartMyServiceAtBootReceiver extends BroadcastReceiver { //TODO Fix
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Lockscreen.getInstance(mContext).startLockscreenService();
            }
        }
    }

    private void stateReceiver(boolean isStartReceiver) {
        if (isStartReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mLockscreenReceiver, filter);
        } else {
            if (null != mLockscreenReceiver) {
                unregisterReceiver(mLockscreenReceiver);
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mServiceStartId = startId;
        stateReceiver(true);
        Intent bundleIntent = intent;
        if (null != bundleIntent) {
            startLockscreenActivity();
        } else {
            Log.d(TAG, TAG + " onStartCommand intent NOT existed");
        }
        setLockGuard();
        return LockscreenService.START_STICKY;
    }

    private void setLockGuard() {
        initKeyguardService();
        if (!LockscreenUtil.getInstance(mContext).isStandardKeyguardState()) {
            setStandardKeyguardState(false);
        } else {
            setStandardKeyguardState(true);
        }
    }

    private KeyguardManager mKeyManager = null;
    private KeyguardManager.KeyguardLock mKeyLock = null;

    private void initKeyguardService() {
        if (null != mKeyManager) {
            mKeyManager = null;
        }
        mKeyManager =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        if (null != mKeyManager) {
            if (null != mKeyLock) {
                mKeyLock = null;
            }
            mKeyLock = mKeyManager.newKeyguardLock(KEYGUARD_SERVICE);
        }
    }

    private void setStandardKeyguardState(boolean isStart) {
        if (isStart) {
            if(null != mKeyLock){
                mKeyLock.reenableKeyguard();
            }
        }
        else {

            if(null != mKeyManager){
                mKeyLock.disableKeyguard();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stateReceiver(false);
        setStandardKeyguardState(true);
    }

    private void startLockscreenActivity() {
        Intent startLockscreenActIntent = new Intent(mContext, LockscreenActivity.class);
        startLockscreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startLockscreenActIntent);
    }

}
