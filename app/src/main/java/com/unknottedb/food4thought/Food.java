package com.unknottedb.food4thought;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    String mUrl;
    String mLinkUrl;

    public Food(String photoUrl, String linkURL){
        mUrl = photoUrl;
        mLinkUrl = linkURL;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mLinkUrl);
        out.writeString(mUrl +"");
    }

    public String getPhotoUrl() {
        return mUrl;
    }

    public String getLinkUrl() {
        return mLinkUrl;
    }
}
