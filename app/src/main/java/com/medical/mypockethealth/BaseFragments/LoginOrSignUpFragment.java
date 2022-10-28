package com.medical.mypockethealth.BaseFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.RegisterSection.ProviderRegistrationSection.SignUpAsProviderFragment;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.SignUpAsUserFragment;
import com.medical.mypockethealth.R;

public class LoginOrSignUpFragment extends Fragment {


    public LoginOrSignUpFragment() {
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
        return inflater.inflate(R.layout.fragment_login_or_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        animationApplier(view);


        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder, new LoginFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.view_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (BaseActivity.mode == 0) {
                    requireActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.frame_holder, new SignUpAsUserFragment()).addToBackStack(null).commit();

                } else if (BaseActivity.mode == 1) {


//                    requireActivity().getSupportFragmentManager().beginTransaction().
//                            replace(R.id.frame_holder,new ProviderTypeFragment()).addToBackStack(null).commit();

                    requireActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.frame_holder, new SignUpAsProviderFragment()).addToBackStack(null).commit();

                }


            }
        });
    }

    private void animationApplier(View view) {

        ImageView imageView = view.findViewById(R.id.front);
        Button learner, instructor;
        learner = view.findViewById(R.id.login);
        instructor = view.findViewById(R.id.view_cart);

        Animation top, bottom;

        top = AnimationUtils.loadAnimation(getContext(), R.anim.top_mover);
        bottom = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_mover);

        imageView.setAnimation(top);
        learner.setAnimation(bottom);
        instructor.setAnimation(bottom);

    }
}