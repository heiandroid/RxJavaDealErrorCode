package sdx.rxjavadealerrorcode;


import com.gogo.vkan.comm.constant.RelConstants;
import com.gogo.vkan.domain.comm.LogoUpdateDomain;
import com.gogo.vkan.domain.http.HttpResultStringDomain;
import com.gogo.vkan.domain.logo.CheckTokenDomain;
import com.gogo.vkan.domain.profile.ProfileResDomain;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sdx on 2017/2/16.
 */

public interface MyApi {
    Observable<ResultDomain<Data>> getdata();
    Observable<ResultDomain<String>> getToken();
}
