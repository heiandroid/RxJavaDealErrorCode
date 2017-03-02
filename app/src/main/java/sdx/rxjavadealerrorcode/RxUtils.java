package sdx.rxjavadealerrorcode;

import com.gogo.vkan.api.MyApi;
import com.gogo.vkan.api.RxService;
import com.gogo.vkan.business.exception.AppException;
import com.gogo.vkan.business.log.LogHelper;
import com.gogo.vkan.business.manager.TokenManager;
import com.gogo.vkan.comm.constant.Constants;
import com.gogo.vkan.domain.ResultDomain;
import com.gogo.vkan.domain.http.HttpResultStringDomain;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.gogo.vkan.business.exception.AppException.CODE_ERROR_LOGIN_OUT;
import static com.gogo.vkan.business.exception.AppException.CODE_ERROR_TOKEN;
import static sdx.rxjavadealerrorcode.AppException.CODE_ERROR_LOGIN_OUT;
import static sdx.rxjavadealerrorcode.AppException.CODE_ERROR_TOKEN;

/**
 * Created by sdx on 2017/3/1.
 * request net utils
 * to user this utils.you must subscribe VkanSubscribe
 * the domain must base on ResultDomain
 */

public class RxUtils {
    //请求过程中.重新获取的次数
    private static final int RETRY_TIME = 3;

    /**
     * io线程切换到AndroidMain线程
     */
    public static <T> Observable.Transformer IoTOMain() {
        return new Observable.Transformer<T, T>() {

            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * reprocessing all request.such as token ,login state
     * to use preDataResult;to user this when you need the api code,
     * otherwise use preDataResult
     *
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> Observable.Transformer<ResultDomain<T>, ResultDomain<T>> preResult() {
        return new Observable.Transformer<ResultDomain<T>, ResultDomain<T>>() {
            @Override
            public Observable<ResultDomain<T>> call(Observable<ResultDomain<T>> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<ResultDomain<T>, ResultDomain<T>>() {
                            @Override
                            public ResultDomain<T> call(ResultDomain<T> tResultDomain) {
                                //是否有任务数据.有的话显示toast
                                if (tResultDomain.task != null) {
                                    tResultDomain.task.showTips();
                                }
                                return tResultDomain;
                            }
                        })
                        .flatMap(new Func1<ResultDomain<T>, Observable<ResultDomain<T>>>() {
                            @Override
                            public Observable<ResultDomain<T>> call(ResultDomain<T> tResultDomain) {
                                //分类服务器的错误码
                                switch (tResultDomain.api_status) {
                                    case CODE_ERROR_TOKEN:
                                        return Observable.error(new AppException(CODE_ERROR_TOKEN));
                                    case CODE_ERROR_LOGIN_OUT:
                                        return Observable.error(new AppException(CODE_ERROR_LOGIN_OUT));
                                    default:
                                        return Observable.just(tResultDomain);
                                }

                            }
                        }).retryWhen(dealResult())//如果服务器有错误码需要处理一下然后继续之前的操作
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 直接返回ResultDomain 里边的data
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<ResultDomain<T>, T> preDataResult() {
        return new Observable.Transformer<ResultDomain<T>, T>() {
            @Override
            public Observable<T> call(Observable<ResultDomain<T>> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<ResultDomain<T>, ResultDomain<T>>() {
                            @Override
                            public ResultDomain<T> call(ResultDomain<T> tResultDomain) {
                                //是否有任务数据.有的话显示toast
                                if (tResultDomain.task != null) {
                                    tResultDomain.task.showTips();
                                }
                                return tResultDomain;
                            }
                        })
                        .flatMap(new Func1<ResultDomain<T>, Observable<T>>() {
                            @Override
                            public Observable<T> call(ResultDomain<T> tResultDomain) {
                                //分类服务器的错误码
                                switch (tResultDomain.api_status) {
                                    case CODE_ERROR_TOKEN:
                                        return Observable.error(new AppException(CODE_ERROR_TOKEN));
                                    case CODE_ERROR_LOGIN_OUT:
                                        return Observable.error(new AppException(CODE_ERROR_LOGIN_OUT));
                                    default:
                                        return Observable.just(tResultDomain.data);
                                }

                            }
                        }).retryWhen(dealResult())//如果服务器有错误码需要处理一下然后继续之前的操作
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 预处理服务器的数据,然后重新请求网络或者用户下线 具体的实现是在 VkanSubscribe中实现的
     *
     * @return
     */
    private static Func1<Observable<? extends Throwable>, Observable<?>> dealResult() {
        return new Func1<Observable<? extends Throwable>, Observable<?>>() {
            //重试的次数
            private int tryCount;

            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (throwable instanceof AppException) {
                            int code = ((AppException) throwable).getCode();
                            switch (code) {
                                //token 错误.重新请求token'
                                case CODE_ERROR_TOKEN:
                                    if (++tryCount > RETRY_TIME) {
                                        return Observable.error(new AppException(CODE_ERROR_TOKEN));
                                    }
//                                    LogHelper.e("重试获取token次数:" + tryCount);
                                    return RxService.createApi(MyApi.class)
                                            .getToken()
                                            .subscribeOn(Schedulers.io())
                                            .doOnNext(new Action1<ResultDomain<String>>() {
                                                @Override
                                                public void call(ResultDomain<String> stringResultDomain) {
                                                    tryCount = RETRY_TIME;
                                                    // TODO: 2017/3/2 处理token 
                                                }
                                            }).doOnError(new Action1<Throwable>() {
                                                @Override
                                                public void call(Throwable throwable) {
                                                    throwable.printStackTrace();
                                                }
                                            });
                                //用户未登录.退出登录.在错误处理中处理
                                case CODE_ERROR_LOGIN_OUT:
                                    return Observable.error(new AppException(AppException.CODE_ERROR_LOGIN_OUT));
                                default:
                                    return Observable.error(throwable);
                            }

                        } else {
                            return Observable.error(throwable);
                        }
                    }
                });
            }
        };
    }
}
