package sdx.rxjavadealerrorcode;


import rx.Observable;

/**
 * Created by sdx on 2017/2/16.
 */

public interface MyApi {
    Observable<ResultDomain<Data>> getdata();
    Observable<ResultDomain<String>> getToken();
}
