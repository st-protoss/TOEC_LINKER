package com.ck.toec.toec_linker.modules.device.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 2017/11/17.
 */

public class DeviceFragAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private  List<String> titles = new ArrayList<>();
    public DeviceFragAdapter(FragmentManager fm){
        super(fm);
    }



    public void addTab(Fragment frag , String title){
        fragments.add(frag);
        titles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
