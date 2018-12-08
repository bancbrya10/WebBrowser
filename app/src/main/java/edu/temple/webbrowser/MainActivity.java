package edu.temple.webbrowser;

import android.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements UrlChanger{

    EditText displayUrl;
    Button goButton;
    Toolbar toolbar;
    ViewPager vp;
    BrowserPagerAdapter browserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayUrl = findViewById(R.id.displayUrl);
        goButton = findViewById(R.id.goButton);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        browserAdapter = new BrowserPagerAdapter(getSupportFragmentManager());
        browserAdapter.addFrag(TabFragment.newInstance(""));

        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(browserAdapter);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabFragment tabFragment = browserAdapter.getItem(vp.getCurrentItem());
                tabFragment.loadPage(displayUrl.getText().toString());
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                browserAdapter.getItem(i).changePage();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                vp.setCurrentItem(vp.getCurrentItem()-1);
                return true;
            case R.id.newTab:
                browserAdapter.addFrag(TabFragment.newInstance(""));
                vp.setCurrentItem(browserAdapter.getCount()-1);
                return true;
            case R.id.forward:
                vp.setCurrentItem(vp.getCurrentItem()+1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onUrlChange(String url) {
        displayUrl.setText(url);
    }

    public class BrowserPagerAdapter extends FragmentStatePagerAdapter{

        private ArrayList<TabFragment> frags = new ArrayList<TabFragment>();

        public BrowserPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public TabFragment getItem(int i) {
            return frags.get(i);
        }

        @Override
        public int getCount() {
            return frags.size();
        }

        public void addFrag(TabFragment tabFragment){
            frags.add(tabFragment);
            notifyDataSetChanged();
        }
    }
}
