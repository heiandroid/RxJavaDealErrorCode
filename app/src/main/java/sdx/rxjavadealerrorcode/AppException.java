package sdx.rxjavadealerrorcode;

/**
 * Created by sdx on 2017/3/1.
 */

public class AppException extends Exception {
    //token有问题需要重新获取
    public static final int CODE_ERROR_TOKEN = -9;
    //退出登录需要登录
    public static final int CODE_ERROR_LOGIN_OUT = -8;
    private static String message;
    private static int errorCode;

    AppException(String message) {
        super(message);
    }

    public AppException(int code) {
        this(getApiExceptionMessage(code));
    }

    /**
     * 获取到当前的错误码
     *
     * @return
     */
    public int getCode() {
        return errorCode;
    }

    private static String getApiExceptionMessage(int code) {
        errorCode = code;
        switch (code) {
            case CODE_ERROR_TOKEN:
                message = "token失效";
                break;
            case CODE_ERROR_LOGIN_OUT:
                message = "用户未登录";
                break;
            default:
                message = "未知错误";
        }
        return message;
    }
}
