package com.example.cfsearch;

import android.content.Context;

import android.content.AsyncTaskLoader;
import java.util.List;

public class CodeLoader extends AsyncTaskLoader<List<Code>> {


    private static final String LOG_TAG = CodeLoader.class.getSimpleName();

    private String mUrl;

    public CodeLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        //super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Code> loadInBackground() {
        if(mUrl==null){
            return null;
        }
        //else{
            List<Code> code = QueryUtils.fetchCodeData(mUrl);
            return code;
        //}
    }
}
