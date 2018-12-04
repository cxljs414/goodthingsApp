package com.goodthings.app.application;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Keep;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/31
 * 修改内容：
 * 最后修改时间：
 */

public class SophixStubApplication extends SophixApplication {

    private final String TAG = SophixStubApplication.class.getSimpleName();

    @Keep
    @SophixEntry(MyApplication.class)
    static class RealApplicationStub{}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initSophix();
    }

    private void initSophix() {
        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData(null,null,null)
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(int mode, int code, String info, int patchVersion) {
                        if(code == PatchStatus.CODE_DOWNLOAD_SUCCESS){
                            Log.i(TAG,"sophix load patch success");
                        }else if(code == PatchStatus.CODE_LOAD_RELAUNCH){
                            Log.i(TAG, "sophix preload patch success. restart app to make effect.");
                        }else {
                            Log.i(TAG,"sophix load patch failure:"+info);
                        }
                    }
                }).initialize();
        instance.queryAndLoadNewPatch();
    }
}
