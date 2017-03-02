package sdx.rxjavadealerrorcode;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RxService {
    /*
    * 统一配置header
    * */
    private static Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    //// TODO: 2017/3/2 添加头
//                    .addHeader("token",)
                    .build();
            return chain.proceed(request);
        }
    };

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1500, TimeUnit.MILLISECONDS)
            .readTimeout(1500, TimeUnit.MILLISECONDS)
            .writeTimeout(1500, TimeUnit.MILLISECONDS)
            .addInterceptor(headerInterceptor)
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            // TODO: 2017/3/2 设置自己的主机名
            .baseUrl("www.baidu.com")
            .client(okHttpClient)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private RxService() {
        //construct
    }

    public static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}
