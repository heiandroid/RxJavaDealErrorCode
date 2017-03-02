package sdx.rxjavadealerrorcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果请求某个接口但是token过期.或者用户需要退出登录,等等其他信息都可以在这里统一处理了
                RxService.createApi(MyApi.class).getdata()
                        .compose(RxUtils.<Data>preResult())
                        .subscribe(new MySubscribe<ResultDomain<Data>>(MainActivity.this) {
                            @Override
                            public void onSucceeded(ResultDomain<Data> dataResultDomain) {
                                     //// TODO: 2017/3/2 处理获取到的数据 
                            }

                            @Override
                            public void onFailed(Throwable e) {
                                // TODO: 2017/3/2 错误 
                            }
                        });
                RxService.createApi(MyApi.class).getdata()
                        .compose(RxUtils.<Data>preDataResult())
                        .subscribe(new MySubscribe<Data>(MainActivity.this) {
                            @Override
                            public void onSucceeded(Data data) {
                                // TODO: 2017/3/2 处理获取到的数据
                            }

                            @Override
                            public void onFailed(Throwable e) {
                            // TODO: 2017/3/2 错误
                            }
                        });
            }
        });
    }
}
