package com.mailerdaemon.app.impContacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mailerdaemon.app.R;

public class ContactFragmentViewPager  extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contact_viewpager,container,false);
        viewPager=view.findViewById(R.id.contact_viewpager);
        tabLayout=view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        assert getArguments() != null;
        String[] tabs=getArguments().getStringArray("tabs");

        assert getArguments() != null;
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            String[] s=getArguments().getStringArray("pages");
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return getFragment(s[position]);
            }

            @Override
            public int getCount() {
                return s.length;
            }
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                assert tabs != null;
                return tabs[position];
            }
        });


        return view;
    }


    private Fragment getFragment(String s){
        ContactDetailFragment fragment=new ContactDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",s);
        fragment.setArguments(bundle);
        return fragment;
    }
 }