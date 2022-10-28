package com.medical.mypockethealth.ClientFragments.EHRSection;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.medical.mypockethealth.Adaptors.UserSection.EHRSection.AddFileRecordRecycleViewAdaptor;
import com.medical.mypockethealth.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class AddRecordFragment extends Fragment implements AddFileRecordRecycleViewAdaptor.callBackFromAddFileRecordRecycleViewAdaptor{

    private static final String TAG = "AddRecordFragment";

    private AddFileRecordRecycleViewAdaptor addFileRecordRecycleViewAdaptor;
    private RecyclerView medicine_recycle;
    private Dialog pop_up_box;
    private ImageView found;
    private static List<String> medicine=new ArrayList<>();




    public AddRecordFragment() {
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
        return inflater.inflate(R.layout.fragment_add_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        found =view.findViewById(R.id.found);

        medicine_recycle =view.findViewById(R.id.medicine_holder);

        medicine_recycle.setLayoutManager(new LinearLayoutManager(getContext()));


        checkDataState();

        addFileRecordRecycleViewAdaptor=new AddFileRecordRecycleViewAdaptor(new ArrayList<>(),AddRecordFragment.this);


        medicine_recycle.setAdapter(addFileRecordRecycleViewAdaptor);



        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addFileBox();


            }
        });




        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new EHROneFragment()).addToBackStack(null).commit();


            }
        });

    }


    private void enableBottomNavigation()
    {

//        BottomNavigationView view1 = requireActivity().findViewById(R.id.bottom_navigation_view);
//
//        view1.setVisibility(View.VISIBLE);

    }



    @Override
    public void deleteItem(int position) {

        medicine.remove(position);

        addFileRecordRecycleViewAdaptor.loadData(medicine);

         checkDataState();

    }

    @Override
    public void innerFiles(String name) {

        Toast.makeText(getContext(),"need dynamic functionality",Toast.LENGTH_SHORT).show();
    }

    private void enableNotFound()
    {
        found.setVisibility(View.VISIBLE);
    }

    private void disableNotFound()
    {
        found.setVisibility(View.GONE);
    }

    private void checkDataState()
    {
        if(medicine.size()==0)
        {
            enableNotFound();
        }
        else
        {
            disableNotFound();
        }
    }

    private void addFileBox()
    {
        pop_up_box =new Dialog(getContext());
        pop_up_box.setContentView(R.layout.add_medicine_layout);
        pop_up_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_up_box.setCanceledOnTouchOutside(true);
        Window window= pop_up_box.getWindow();
        window.setGravity(Gravity.CENTER);
        pop_up_box.show();

        EditText editText= pop_up_box.findViewById(R.id.about_text);

        pop_up_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(editText.getText().toString().trim().length()==0)
                {
                    editText.setError("kindly add your file name");
                }

                else
                {

                    editText.setError(null);

                    pop_up_box.dismiss();

                    addFileData(editText.getText().toString());

                }

            }
        });

    }

    private void addFileData(String name)
    {

        medicine.add(name);
        addFileRecordRecycleViewAdaptor.loadData(medicine);

        checkDataState();

    }
}