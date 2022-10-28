package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.medical.mypockethealth.Adaptors.SliderAdaptor.MapSliderRecycleViewAdaptor;
import com.medical.mypockethealth.Classes.SliderClasses.SliderInformation;
import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SymptomCheckerSevenFragment extends Fragment implements MapSliderRecycleViewAdaptor.CallBackFromMapSliderRecycleViewAdaptor {


    public SymptomCheckerSevenFragment() {
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
        return inflater.inflate(R.layout.fragment_symptom_checker_seven, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSlider(view);

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomCheckerFiveFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.view_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(requireContext(), "working", Toast.LENGTH_SHORT).show();

//                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
//                        new SymptomCheckerEightFragment()).addToBackStack(null).commit();
            }
        });

    }

    private void setSlider(View view)
    {

        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager);

        List<SliderInformation> sliderInformation=new ArrayList<>();

        sliderInformation.add(new SliderInformation(R.drawable.map1));
        sliderInformation.add(new SliderInformation(R.drawable.map2));
        sliderInformation.add(new SliderInformation(R.drawable.map3));

        viewPager2.setAdapter(new MapSliderRecycleViewAdaptor(sliderInformation, SymptomCheckerSevenFragment.this));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

        viewPager2.setOffscreenPageLimit(3);

        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();

        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        compositePageTransformer.addTransformer((page, position) -> {

            Log.d(TAG, "setSlider: "+page);

            float a=1-Math.abs(position);
            page.setScaleY(0.85f + a * 0.15f);

        });

        viewPager2.setPageTransformer(compositePageTransformer);

    }

    @Override
    public void onClicked(SliderInformation sliderInformation) {

        // click on country callback

    }
}