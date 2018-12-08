package edu.temple.webbrowser;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TabFragment extends Fragment {

    protected static final String ARG_URL = "url";

    private String url;
    Button backButton;
    Button forwardButton;
    WebView webView;
    View v;
    Context context;
    int index;
    ArrayList<String> urlList;
    UrlChanger urlChanger;


    public TabFragment() {
        urlList = new ArrayList<String>();
        index = 0;
    }

    public static TabFragment newInstance(String url) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if(context instanceof UrlChanger){
            urlChanger = (UrlChanger) context;
        }
        else {
            throw new RuntimeException("ERROR");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tab, container, false);
        webView = v.findViewById(R.id.tab);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        loadPage(url);

        backButton = v.findViewById(R.id.back_frag);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(index > 0){
                    index--;
                    changePage();
                }
            }
        });
        forwardButton = v.findViewById(R.id.forward_frag);
        forwardButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(index < urlList.size() - 1){
                    index++;
                    changePage();
                }
            }
        });

        return v;
    }

    public void loadPage(String url){
        if(url != null && url != ""){
            urlList.add(url);
            webView.loadUrl(url);
            index = urlList.size() - 1;
        }
    }

    public void changePage(){
        if(urlList.size() == 0){
            urlChanger.onUrlChange("");
        }
        else if(urlList.size() > 0){
            webView.loadUrl(urlList.get(index));
            urlChanger.onUrlChange(urlList.get(index));
        }
    }

}
