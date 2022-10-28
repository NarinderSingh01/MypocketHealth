package com.medical.mypockethealth.BaseFragments.RegisterSection;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.R;

import im.delight.android.webview.AdvancedWebView;


public class PolicyFragment extends Fragment implements AdvancedWebView.Listener{

    private AdvancedWebView mWebView;
    private Dialog loading_box;
    public static final String data_key="data_key";

    public PolicyFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_policy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        assert getArguments() != null;
        String link=(String) getArguments().get(PolicyFragment.data_key);

        mWebView = (AdvancedWebView) view.findViewById(R.id.webview);
        mWebView.setListener(getActivity(), this);
        mWebView.setMixedContentAllowed(false);
        mWebView.loadUrl(link);

        openLoadingBox();


    }
    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        loading_box.dismiss();

    }

    @Override
    public void onPageFinished(String url)
    {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl)
    {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent)
    {

    }

    @Override
    public void onExternalPageRequest(String url)
    {

    }


    private void openLoadingBox() {
        loading_box = new Dialog(getContext());
        loading_box.setContentView(R.layout.loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();

    }
}
