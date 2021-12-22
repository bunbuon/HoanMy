package com.hoanmy.kleanco;

import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hoanmy.kleanco.adapters.TabsPagerAdapter;
import com.hoanmy.kleanco.commons.DeactivatedViewPager;
import com.hoanmy.kleanco.fragments.FragmentTaskDone;
import com.hoanmy.kleanco.fragments.FragmentTaskProcess;
import com.hoanmy.kleanco.models.Login;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class ManagementActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.view_pager_content)
    DeactivatedViewPager viewPagerContent;

    SearchView searchView = null;
    FragmentTaskProcess fragmentTaskProces;
    FragmentTaskDone fragmentTaskDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        ButterKnife.bind(this);
        Login loginData = Paper.book().read("login");
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(loginData.getUsername());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        TabsPagerAdapter sectionsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());
        viewPagerContent.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPagerContent);
        viewPagerContent.setCurrentItem(0);
        makeActionOverflowMenuShown();
        viewPagerContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

                if (searchView != null && !searchView.isIconified()) {
                    //searchView.onActionViewExpanded();
                    searchView.setIconified(true);
                    searchView.setIconified(true);

                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Log.d("", e.getLocalizedMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        if (mSearchMenuItem != null) {
            searchView = (SearchView) mSearchMenuItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this
                    .getComponentName()));
        }
        searchView.setIconifiedByDefault(true);
        MenuItemCompat.expandActionView(mSearchMenuItem);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String query) {
                // this is your adapter that will be filtered
                TabsPagerAdapter pagerAdapter = (TabsPagerAdapter) viewPagerContent
                        .getAdapter();
                for (int i = 0; i < pagerAdapter.getCount(); i++) {

                    Fragment viewPagerFragment = (Fragment) viewPagerContent
                            .getAdapter().instantiateItem(viewPagerContent, i);
                    if (viewPagerFragment != null
                            && viewPagerFragment.isAdded()) {

                        if (viewPagerFragment instanceof FragmentTaskProcess) {
                            fragmentTaskProces = (FragmentTaskProcess) viewPagerFragment;
                            if (fragmentTaskProces != null) {
                                fragmentTaskProces.beginSearch(query);
                            }
                        } else if (viewPagerFragment instanceof FragmentTaskDone) {
                            fragmentTaskDone = (FragmentTaskDone) viewPagerFragment;
                            if (fragmentTaskDone != null) {
                                fragmentTaskDone.beginSearch(query);
                            }
                        }
                    }
                }

                return false;

            }

            public boolean onQueryTextSubmit(String query) {
                // Here u can get the value "query" which is entered in the

                return false;

            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

}
