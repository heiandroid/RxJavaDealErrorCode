package sdx.rxjavadealerrorcode;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.gogo.vkan.android.app.App;
import com.gogo.vkan.comm.uitl.StringUtils;
import com.gogo.vkan.comm.uitl.ToastSingle;

import static android.text.TextUtils.isEmpty;

/**
 * Created by sdx on 2016/10/14.
 * 积分任务
 */
public class ScoreTaskDomain implements Parcelable{
    public String id;
    public String title;
    public String color;
    public String desc1;
    public String desc2;
    public String score;
    public int flag;
    public String hint;
    public void showTips() {
        if (flag != 1) return;
        if (!TextUtils.isEmpty(hint)) {
            //show toast
        }
    }

    protected ScoreTaskDomain(Parcel in) {
        id = in.readString();
        title = in.readString();
        color = in.readString();
        desc1 = in.readString();
        desc2 = in.readString();
        score = in.readString();
        flag = in.readInt();
        hint = in.readString();
    }

    public static final Creator<ScoreTaskDomain> CREATOR = new Creator<ScoreTaskDomain>() {
        @Override
        public ScoreTaskDomain createFromParcel(Parcel in) {
            return new ScoreTaskDomain(in);
        }

        @Override
        public ScoreTaskDomain[] newArray(int size) {
            return new ScoreTaskDomain[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(color);
        dest.writeString(desc1);
        dest.writeString(desc2);
        dest.writeString(score);
        dest.writeInt(flag);
        dest.writeString(hint);
    }
}
