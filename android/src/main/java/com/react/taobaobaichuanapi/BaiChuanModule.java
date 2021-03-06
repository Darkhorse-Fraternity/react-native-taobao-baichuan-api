package com.react.taobaobaichuanapi;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.login.LoginService;
import com.ali.auth.third.login.callback.LogoutCallback;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.IllegalViewOperationException;

import java.util.HashMap;
import java.util.Map;

public class BaiChuanModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private Promise promise;
    
    public BaiChuanModule(ReactApplicationContext reactContext) {
        
        super(reactContext);
        reactContext.addActivityEventListener(this);
    }
    
    @Override
    public String getName() {
        return "React_Native_Taobao_Baichuan_Api";
    }
    
    @ReactMethod
    public void logout() {
        try {
            LoginService service = (LoginService) MemberSDK.getService(LoginService.class);
            service.logout(new LogoutCallback() {
                @Override
                public void onSuccess() {
                }
                
                @Override
                public void onFailure(int code, String message) {
                    //                    promise.reject(code+"", message);
                }
            });
            
            
        } catch (IllegalViewOperationException e) {
            //            promise.reject("001", e.getMessage());
        }
    }
    
    
    @ReactMethod
    public void jump(String itemId) {
        /**
         * 打开电商组件, 使用默认的webview打开
         *
         * @param activity             必填
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
         */
        //商品详情page
        AlibcBasePage detailPage = new AlibcDetailPage(itemId);
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Auto, false);
        Map<String, String> exParams = new HashMap<>();
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        AlibcTrade.show(currentActivity, detailPage, showParams, null, exParams, new AlibcTradeCallback() {
            
            @Override
            public void onTradeSuccess(TradeResult tradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
            }
            
            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                Log.i("百川" + code, msg);
            }
        });
    }
    
    
    @ReactMethod
    public void present(String url) {
        /**
         * 打开电商组件, 使用默认的webview打开
         *
         * @param activity             必填
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
         */
        //商品详情page
        Log.i("1111", url);
        AlibcPage detailPage = new AlibcPage(url);
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);
        Map<String, String> exParams = new HashMap<>();
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        AlibcTrade.show(currentActivity, detailPage, showParams, null, exParams, new AlibcTradeCallback() {
            
            @Override
            public void onTradeSuccess(TradeResult tradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
            }
            
            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                Log.i("百川" + code, msg);
            }
        });
    }
    
    @ReactMethod
    public void findUser(final Promise promise) {
        try {
            LoginService service = (LoginService) MemberSDK.getService(LoginService.class);
            service.setLoginCallback(new LoginCallback() {
                @Override
                public void onSuccess(Session session) {
                    WritableMap map = Arguments.createMap();
                    map.putString("openId", session.openId);
                    promise.resolve(map);
                }
                @Override
                public void onFailure(int code, String message) {
                    promise.reject(code+"", message);
                }
            });
            
            
        } catch (IllegalViewOperationException e) {
            promise.reject("001", e.getMessage());
        }
    }
    
    
    @ReactMethod
    public void login(final Promise promise) {
        try {
            
            if(!AlibcLogin.getInstance().isLogin()){
                LoginService service = (LoginService) MemberSDK.getService(LoginService.class);
                service.auth(new LoginCallback() {
                    @Override
                    public void onSuccess(Session session) {
                        WritableMap map = Arguments.createMap();
                        map.putString("openId", session.openId);
                        promise.resolve(map);
                    }
                    @Override
                    public void onFailure(int code, String message) {
                        promise.reject(code+"", message);
                    }
                });
            }else{
                Session session = AlibcLogin.getInstance().getSession();
                WritableMap map = Arguments.createMap();
                map.putString("openId", session.openId);
                promise.resolve(map);
            }
            
            
            
        } catch (IllegalViewOperationException e) {
            promise.reject("001", e.getMessage());
        }
    }
    
    
    @Override
    public void onActivityResult(Activity activity, final int requestCode, final int resultCode, final Intent intent) {
        
    }
    
    @Override
    public void onNewIntent(final Intent intent) {
        
    }
}
