
package com.medical.mypockethealth.ClientFragments.BottomNavigationSection.BookingSection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medical.mypockethealth.Adaptors.UserSection.BookingSection.BookedDoctorsRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.BookDoctorInformation.BookDoctorInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.UserSection.BookingSection.CancelBookingCaller;
import com.medical.mypockethealth.Threads.UserSection.BookingSection.CurrentBookingDataFetcher;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CurrentBookingFragment extends Fragment implements BookedDoctorsRecycleViewAdaptor.callBackFromBookedDoctorsRecycleViewAdaptor,
        CurrentBookingDataFetcher.CallbackFromCurrentBookingDataFetcher,CancelBookingCaller.CallBackFromCancelBookingCaller{

    private static final String TAG = "CurrentBookingFragment";

    private final Handler handler=new Handler();
    private ImageView loading,reload;
    private  BottomSheetDialog bottomSheetDialog;
    private BookedDoctorsRecycleViewAdaptor doctorsRecycleViewAdaptor;


    public CurrentBookingFragment() {
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
        return inflater.inflate(R.layout.fragment_current_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loading=view.findViewById(R.id.loading);
        reload=view.findViewById(R.id.reload);

        RecyclerView recyclerView=view.findViewById(R.id.current_holder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        doctorsRecycleViewAdaptor=new BookedDoctorsRecycleViewAdaptor(new ArrayList<>(),
                CurrentBookingFragment.this,getContext());

        recyclerView.setAdapter(doctorsRecycleViewAdaptor);


        loadCurrentData();


        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadCurrentData();

            }
        });

    }

    private void loadCurrentData()
    {
        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
            loading.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);
            
            new Thread(new CurrentBookingDataFetcher(CurrentBookingFragment.this)).start();
        }

        else
        {
            reload.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }
    }

    @Override
    public void cancelBooking(BookDoctorInformation bookDoctorInformation) {

        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {

            AlertDialog.Builder builder=new AlertDialog.Builder(getContext())
                    .setMessage("Are you sure you want to cancel this booking").setNegativeButton("NO", (dialogInterface, i) -> {

                    }).setPositiveButton("YES", (dialogInterface, i) -> {

                     new Thread(new CancelBookingCaller(bookDoctorInformation,CurrentBookingFragment.this)).start();

                    });
            builder.setCancelable(false);
            builder.show();
        }

        else
        {
            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }

    }

    @Override
    public void viewDetail(BookDoctorInformation bookDoctorInformation) {
        
        bottomSheetHandler(bookDoctorInformation);
    }


    private void bottomSheetHandler(BookDoctorInformation bookDoctorInformation)
    {

        handler.post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                bottomSheetDialog=new BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme);

                View bottom_view= LayoutInflater.from(requireContext()).inflate(R.layout.detail_sheet_layout
                        ,
                        (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

                bottomSheetDialog.setContentView(bottom_view);

                bottomSheetDialog.show();
                ImageView doctor_image;

                TextView doctor_name,send_to,response,speciality,booking_status;

                doctor_name=bottomSheetDialog.findViewById(R.id.doctor_name);
                send_to=bottomSheetDialog.findViewById(R.id.send_to);
                response=bottomSheetDialog.findViewById(R.id.response);
                speciality=bottomSheetDialog.findViewById(R.id.speciality);
                doctor_image=bottomSheetDialog.findViewById(R.id.doctor_image);
                booking_status=bottomSheetDialog.findViewById(R.id.booking_status);

                String name;

                if (bookDoctorInformation.getUserTitle() != null && bookDoctorInformation.getUserType() != null) {

                    name = bookDoctorInformation.getUserTitle() + " " +
                            bookDoctorInformation.getFirstName().charAt(0) + " " + bookDoctorInformation.getSurName();

                } else {

                    name = "Dr. " + bookDoctorInformation.getFirstName().charAt(0) + " " + bookDoctorInformation.getSurName();

                }

//                String name="Dr. " + bookDoctorInformation.getFirstName().charAt(0)+" "+bookDoctorInformation.getSurName();

                assert doctor_name != null;
                doctor_name.setText(name);

                assert speciality != null;
                speciality.setText(bookDoctorInformation.getSpecialization());

                String sendTo;
//                String sendTo="send to Dr. "+bookDoctorInformation.getFirstName().charAt(0)+" "+bookDoctorInformation.getSurName();

                if (bookDoctorInformation.getUserTitle() != null && bookDoctorInformation.getUserType() != null) {

                    sendTo = bookDoctorInformation.getUserTitle() + " " +
                            bookDoctorInformation.getFirstName().charAt(0) + " " + bookDoctorInformation.getSurName();

                } else {

                    sendTo = "Dr. " + bookDoctorInformation.getFirstName().charAt(0) + " " + bookDoctorInformation.getSurName();

                }

                assert send_to != null;
                send_to.setText(sendTo);

                String responseTo;

                if (bookDoctorInformation.getUserTitle() != null && bookDoctorInformation.getUserType() != null) {

                    responseTo = bookDoctorInformation.getUserTitle() + " " +
                            bookDoctorInformation.getFirstName().charAt(0) + " " + bookDoctorInformation.getSurName() +" will respond to it soon";

                } else {

                    responseTo = "Dr. " + bookDoctorInformation.getFirstName().charAt(0) + " " + bookDoctorInformation.getSurName() +" will respond to it soon";

                }

                assert response != null;
                response.setText(responseTo);

                switch (bookDoctorInformation.getBookingStatus()) {
                    case "0":
                        assert booking_status != null;

                        booking_status.setText(R.string.pending);

                        booking_status.setTextColor(getResources().getColor(R.color.yellow));

                        break;
                    case "1":
                        assert booking_status != null;

                        booking_status.setText(R.string.confirmed);

                        booking_status.setTextColor(getResources().getColor(R.color.green_one));
                        break;
                    case "2":
                        assert booking_status != null;

                        booking_status.setText(R.string.Cancelled);

                        booking_status.setTextColor(getResources().getColor(R.color.red));
                        break;

                    case "3":

                        assert booking_status != null;
                        
                        booking_status.setText(R.string.Rejected);

                        booking_status.setTextColor(getResources().getColor(R.color.red));

                        break;



                }

                Picasso.with(requireContext()).load(Uri.parse(bookDoctorInformation.getProfileImage())).error(R.drawable.profile).into(doctor_image);


                Button save=bottomSheetDialog.findViewById(R.id.save);

                assert save != null;

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       bottomSheetDialog.dismiss();

                    }
                });

            }
        });




    }


    @Override
    public void getFinalData(List<BookDoctorInformation> bookDoctorInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                doctorsRecycleViewAdaptor.loadData(bookDoctorInformation);
            }
        });
        
    }

    @Override
    public void confirmation(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {
                    loading.setVisibility(View.GONE);
                    reload.setVisibility(View.VISIBLE);

                    boxIndicator();
                    doctorsRecycleViewAdaptor.loadData(new ArrayList<>());
                    
                }
            }
        });

    }

    private void boxIndicator()
    {

        DialogShower dialogShower=new DialogShower(R.layout.current_booking_box,getContext());
        dialogShower.showDialog();

    }

    @Override
    public void confirmationCancelBookingCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                    loadCurrentData();

                }
            }
        });
    }


//    private String getProviderType(BookDoctorInformation providerInformation) {
//
//        String type;
//
//        if (providerInformation.getUserType() != null && providerInformation.getUserType().length() != 0) {
//
//            if (providerInformation.getUserType().equalsIgnoreCase("doctor")) {
//
//                type = "Dr.";
//
//            } else if (providerInformation.getUserType().equalsIgnoreCase("nurse")) {
//
//                type = "Nur.";
//
//            } else {
//
//                type = "";
//
//            }
//
//        } else {
//
//            type = "";
//
//        }
//
//        return type;
//    }

}