package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.medical.mypockethealth.Adaptors.SliderAdaptor.SubscriptionRecycleViewAdaptor;
import com.medical.mypockethealth.Classes.SliderClasses.SliderInformation;
import com.medical.mypockethealth.Classes.SymptomChecker.UserSymptomsDataModel;
import com.medical.mypockethealth.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class SymptomCheckerThirteenFragment extends Fragment implements SubscriptionRecycleViewAdaptor.CallBackFromSubscriptionRecycleViewAdaptor {


    private BottomSheetDialog bottomSheetDialog;


    public SymptomCheckerThirteenFragment() {
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
        return inflater.inflate(R.layout.fragment_symptom_checker_thirteen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomCheckerTwelveFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.view_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetHandler();

            }
        });
    }

    private void bottomSheetHandler() {

        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.get_over_bottom_sheet_layout,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        setSlider(bottom_view);

    }


    private void setSlider(View view) {

        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager);

        List<SliderInformation> sliderInformation = new ArrayList<>();

        sliderInformation.add(new SliderInformation(R.drawable.map1));
        sliderInformation.add(new SliderInformation(R.drawable.map2));
        sliderInformation.add(new SliderInformation(R.drawable.map3));


        viewPager2.setAdapter(new SubscriptionRecycleViewAdaptor(sliderInformation,
                SymptomCheckerThirteenFragment.this));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

        viewPager2.setOffscreenPageLimit(3);

        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();

        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        compositePageTransformer.addTransformer((page, position) -> {

            float a = 1 - Math.abs(position);
            page.setScaleY(0.85f + a * 0.15f);
        });


        viewPager2.setPageTransformer(compositePageTransformer);

    }

    @Override
    public void onClicked(SliderInformation sliderInformation) {

        bottomSheetDialog.cancel();

        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                new SymptomConsultSectionFragment()).addToBackStack(null).commit();

    }
}