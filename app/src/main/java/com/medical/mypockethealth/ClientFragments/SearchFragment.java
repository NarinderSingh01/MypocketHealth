package com.medical.mypockethealth.ClientFragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.SymptomChecker.GetSymptomDetails;
import com.medical.mypockethealth.Classes.SymptomChecker.SymptomInformation;
import com.medical.mypockethealth.Classes.SymptomChecker.SymptomRecycleViewAdaptor;
import com.medical.mypockethealth.ClientFragments.SymptomCheckerSection.SymptomCheckerFiveFragment;
import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SearchFragment extends Fragment implements GetSymptomDetails.CallBackFromGetSymptomDetails
        , SymptomRecycleViewAdaptor.callBackFromSymptomRecycleViewAdaptor {

    private SymptomRecycleViewAdaptor symptomRecycleViewAdaptor;
    private Dialog loading_box;
    private SearchView searchView;
    private ImageView not_found;
    private LinearLayout lay_main;

    public SearchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: symptom called");

        RecyclerView recyclerView = view.findViewById(R.id.current_holder);
        searchView = view.findViewById(R.id.searchView);
        not_found = view.findViewById(R.id.not_found);
        lay_main = view.findViewById(R.id.lay_main);

        symptomRecycleViewAdaptor = new SymptomRecycleViewAdaptor(new ArrayList<>(), SearchFragment.this);

        recyclerView.setAdapter(symptomRecycleViewAdaptor);

        openLoadingBox();

        new Thread(new GetSymptomDetails(SearchFragment.this)).start();

        searchOperation();

    }

    private void openLoadingBox() {

        Log.d(TAG, "open: entered loading");

        loading_box = new Dialog(getContext());
        loading_box.setContentView(R.layout.loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();
    }

    @Override
    public void symptomList(List<SymptomInformation> symptomInformation) {

        Log.d(TAG, "open: ");

        if (symptomInformation != null) {

            Log.d(TAG, "open: entered if");

            loading_box.dismiss();

            if (symptomInformation != null && symptomInformation.size() != 0) {

                symptomRecycleViewAdaptor.loadData(symptomInformation);

                SymptomRecycleViewAdaptor.unFilteredSymptomInformation = new ArrayList<>(symptomInformation);

                not_found.setVisibility(View.GONE);
                lay_main.setVisibility(View.VISIBLE);

            } else {

                symptomRecycleViewAdaptor.loadData(new ArrayList<>());

                SymptomRecycleViewAdaptor.unFilteredSymptomInformation = new ArrayList<>();

                not_found.setVisibility(View.VISIBLE);
                lay_main.setVisibility(View.GONE);

            }

        } else {

            Log.d(TAG, "symptomList: entered else");

            loading_box.dismiss();

        }

    }

    @Override
    public void onClick(SymptomInformation symptomInformation) {

        SymptomCheckerFiveFragment.symptoms_added_list.clear();

        SymptomCheckerFiveFragment.symptoms_added_list.add(symptomInformation);

        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new SymptomCheckerFiveFragment()).addToBackStack(null).commit();

//        if (SymptomCheckerFiveFragment.symptoms_added_list!=null && SymptomCheckerFiveFragment.symptoms_added_list.size()!=0){
//
//            for (int i = 0; i < SymptomCheckerFiveFragment.symptoms_added_list.size(); i++) {
//
//                if (SymptomCheckerFiveFragment.symptoms_added_list.get(i).getName().equalsIgnoreCase(symptomInformation.getName())){
//
//                    SymptomCheckerFiveFragment.symptoms_added_list.add(symptomInformation);
//
//                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new SymptomCheckerFiveFragment()).addToBackStack(null).commit();
//
//                }else {
//
//                    Toast.makeText(requireContext(), "Symptom already added", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//
//        }else {
//
//            assert SymptomCheckerFiveFragment.symptoms_added_list != null;
//            SymptomCheckerFiveFragment.symptoms_added_list.add(symptomInformation);
//
//            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new SymptomCheckerFiveFragment()).addToBackStack(null).commit();
//
//        }

        // old code

//        Bundle bundle = new Bundle();
//
//        bundle.putSerializable("key", symptomInformation);

//        GetResult getResult = new GetResult();
//
//        getResult.setArguments(bundle);
//
//        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder, getResult).addToBackStack(null).commit();

    }

    private void searchOperation() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                symptomRecycleViewAdaptor.getFilter().filter(newText);

                return false;
            }
        });

    }

}