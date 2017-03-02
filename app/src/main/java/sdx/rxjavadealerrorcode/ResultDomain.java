package sdx.rxjavadealerrorcode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sdx on 2017/3/1.
 */

public class ResultDomain<T> implements Parcelable{
    //数据类型不允许修改
    public int sys_status;
    public int  api_status;
    public String info;
    public long current_time;
    public T data;
    public ScoreTaskDomain task;

    protected ResultDomain(Parcel in) {
        sys_status = in.readInt();
        api_status = in.readInt();
        info = in.readString();
        current_time = in.readLong();
    }

    public static final Creator<ResultDomain> CREATOR = new Creator<ResultDomain>() {
        @Override
        public ResultDomain createFromParcel(Parcel in) {
            return new ResultDomain(in);
        }

        @Override
        public ResultDomain[] newArray(int size) {
            return new ResultDomain[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sys_status);
        dest.writeInt(api_status);
        dest.writeString(info);
        dest.writeLong(current_time);
    }
}
