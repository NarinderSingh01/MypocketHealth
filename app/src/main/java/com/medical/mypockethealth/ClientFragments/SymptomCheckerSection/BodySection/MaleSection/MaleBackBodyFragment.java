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


public class MaleBackBodyFragment extends Fragment {

    private static final String TAG = "MaleBackBodyFragment";

    private ImageView body;


    public MaleBackBodyFragment() {
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
        return inflater.inflate(R.layout.fragment_male_back_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        establishViews(view);

        view.findViewById(R.id.rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_holder,
                        new MaleFrontBodyFragment()).addToBackStack(null).commit();

            }
        });


        view.findViewById(R.id.head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backhead);

            }
        });

        view.findViewById(R.id.left_ear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backears);

            }
        });

        view.findViewById(R.id.right_ear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backears);

            }
        });

        view.findViewById(R.id.neck_or_throat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backneckorthroat);

            }
        });

        view.findViewById(R.id.nape_of_neck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backnapeofneck);

            }
        });

        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backsideback);

            }
        });

        view.findViewById(R.id.left_upper_arm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backupperarm);

            }
        });

        view.findViewById(R.id.right_upper_arm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backupperarm);

            }
        });

        view.findViewById(R.id.left_elbow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.three);

            }
        });

        view.findViewById(R.id.right_elbow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.three);

            }
        });


        view.findViewById(R.id.left_forearm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.two);

            }
        });


        view.findViewById(R.id.right_forearm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.two);

            }
        });

        view.findViewById(R.id.lower_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backlowerback);

            }
        });

        view.findViewById(R.id.left_hand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backhand);

            }
        });

        view.findViewById(R.id.right_hand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backhand);

            }
        });

        view.findViewById(R.id.left_buttocks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.one);

            }
        });

        view.findViewById(R.id.right_buttocks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.one);

            }
        });

        view.findViewById(R.id.anus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backanus);

            }
        });

        view.findViewById(R.id.thigh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backthigh);

            }
        });

        view.findViewById(R.id.lower_leg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backlowerleg);

            }
        });


        view.findViewById(R.id.foot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.setImageResource(R.drawable.backfoot);

            }
        });


    }


    private void establishViews(View view)
    {
        body=view.findViewById(R.id.imageView11);

    }
}