package com.medical.mypockethealth.ProviderFragments.MainFrameSection.AppointmentSection;

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
import android.widget.TextView;

import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.AppointmentSection.PatientListSection.CompletedPatientListFragment;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.AppointmentSection.PatientListSection.CurrentPatientListFragment;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.AppointmentSection.PatientListSection.UpcomingPatientListFragment;
import com.medical.mypockethealth.ProviderFragments.ProviderHomeFragment;
import com.medical.mypockethealth.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;


public class AppointmentFragment extends Fragment {

    private final Calendar calendar=Calendar.getInstance();

    public AppointmentFragment() {
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
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        TabLayout tabLayout=view.findViewById(R.id.homeTabLayout);

        ViewPager viewPager=view.findViewById(R.id.homeViewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Current List"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming List"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed List"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final PagerAdapter pagerAdapter=new Adaptor(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                startActivity(new Intent(getContext(), ProviderMainFrame.class));
            }
        });

        setDefaultDateTag(view);


    }

    private void setDefaultDateTag(View view) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String month1 = String.valueOf(month);

        setFilteredDate(String.valueOf(year), month1, String.valueOf(day),view);

    }

    private void setFilteredDate(String year, String month, String day,View view) {

        String result = "";

        try {
            result = requireContext().getResources().getStringArray(R.array.month_names)[Integer.parseInt(month)];
        } catch (ArrayIndexOutOfBoundsException e) {
            result = Integer.toString(Integer.parseInt(month));
        }


        String date = day + ", " + result + " " + year;

        ((TextView) view.findViewById(R.id.date)).setText(date);


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
                    return new CurrentPatientListFragment();
                case 1:
                    return new UpcomingPatientListFragment();
                case 2:
                    return new CompletedPatientListFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return totalCount;
        }
    }

}