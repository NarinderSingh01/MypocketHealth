package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection.BodySection.MaleSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.medical.mypockethealth.R;


public class MaleFrontBodyFragment extends Fragment {

    private static final String TAG = "MaleFrontBodyFragment";

    private ImageView body;
    
    

    public MaleFrontBodyFragment() {
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
        return inflater.inflate(R.layout.fragment_male_front_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        establishViews(view);


        view.findViewById(R.id.rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_holder,
                        new MaleBackBodyFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.head);

            }
        });

        view.findViewById(R.id.neck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.neck);

            }
        });

        view.findViewById(R.id.chest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.chest);

            }
        });


        view.findViewById(R.id.left_upper_arm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.upperarm);

            }
        });

        view.findViewById(R.id.right_upper_arm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.upperarm);

            }
        });

        view.findViewById(R.id.upper_abdomen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.upperabdomen);

            }
        });

        view.findViewById(R.id.middle_abdomen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.middleabdomen);

            }
        });

        view.findViewById(R.id.lest_forearm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.forearm);

            }
        });

        view.findViewById(R.id.right_forearm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.forearm);

            }
        });

        view.findViewById(R.id.lest_hand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.hand);

            }
        });

        view.findViewById(R.id.right_hand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.hand);

            }
        });

        view.findViewById(R.id.lower_abdomen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.lowerabdomen);

            }
        });

        view.findViewById(R.id.left_thigh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.thigh);

            }
        });

        view.findViewById(R.id.right_thigh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.thigh);

            }
        });

        view.findViewById(R.id.sexual_organs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.sexualorgans);

            }
        });

        view.findViewById(R.id.knee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.knee);

            }
        });

        view.findViewById(R.id.lower_leg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.lowerleg);

            }
        });

        view.findViewById(R.id.foot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.foot);

            }
        });
    }


    private void establishViews(View view)
    {
        body=view.findViewById(R.id.imageView11);

    }
}