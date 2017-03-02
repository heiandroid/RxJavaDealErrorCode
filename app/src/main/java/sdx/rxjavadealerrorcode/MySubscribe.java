package sdx.rxjavadealerrorcode;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DialogTitle;

import com.gogo.vkan.business.exception.AppException;
import com.gogo.vkan.business.log.LogHelper;
import com.gogo.vkan.ui.activitys.user.manager.UserManager;
import com.gogo.vkan.ui.view.ProgressDialog;

import rx.Subscriber;

/**
 * Created by sdx on 2017/3/1.
 * 预处理结果
 */

public abstract class MySubscribe<T> extends Subscriber<T> {
    private final String TAG = this.getClass().getSimpleName();
    private Dialog mProcessDialog;

    public MySubscribe(Context context) {
        mProcessDialog = new Dialog(context);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onStart() {
        showDialog();
    }

    private void showDialog() {
        if (mProcessDialog != null && !mProcessDialog.isShowing()) {
            mProcessDialog.show();
        }
    }

    private void dismissDialog() {
        if (mProcessDialog != null && mProcessDialog.isShowing()) {
            mProcessDialog.dismiss();
            mProcessDialog = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof AppException) {
            switch (((AppException) e).getCode()) {
                case AppException.CODE_ERROR_LOGIN_OUT:
                    // TODO: 2017/3/2 做自己需要做的事情
                    //退出登录

                    break;
            }
//            LogHelper.e(TAG, e.getMessage());
        }
        dismissDialog();
        onFailed(e);

    }

    @Override
    public void onNext(T t) {
        dismissDialog();
        onSucceeded(t);
    }

    public abstract void onSucceeded(T t);

    public abstract void onFailed(Throwable e);
}
