package com.android.s19110281;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class PageSourceLoader extends AsyncTaskLoader<String> {
    private String mQueryString;
    private String protocol;
    PageSourceLoader(Context context, String queryString, String protocol) {
        super(context);
        this.mQueryString = queryString;
        this.protocol = protocol;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(this.mQueryString, this.protocol);
    }
}
