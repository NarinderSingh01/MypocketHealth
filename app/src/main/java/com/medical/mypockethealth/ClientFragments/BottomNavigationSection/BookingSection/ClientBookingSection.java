package com.medical.mypockethealth.ClientFragments.BottomNavigationSection.BookingSection;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.R;
import com.google.android.material.tabs.TabLayout;

public class ClientBookingSection extends Fragment {

 
    public ClientBookingSection() {
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
        return inflater.inflate(R.layout.fragment_client_booking_section, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout=view.findViewById(R.id.homeTabLayout);

        ViewPager viewPager=view.findViewById(R.id.homeViewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Past Bookings"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final PagerAdapter pagerAdapter=new Adaptor(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), ClientMainFrame.class));
            }
        });

        enableBottomNav();

    }

    static class Adaptor extends FragmentStatePagerAdapter
    {

        private final int totalCount;

        public Adaptor(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);

            this.totalCount=behavior;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    return new CurrentBookingFragment();
                case 1:
                    return new PreviousBookingFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return totalCount;
        }
    }

    private void enableBottomNav() {

        View view1 = requireActivity().findViewById(R.id.meowBottomNavigation);

        view1.setVisibility(View.VISIBLE);

    }

}