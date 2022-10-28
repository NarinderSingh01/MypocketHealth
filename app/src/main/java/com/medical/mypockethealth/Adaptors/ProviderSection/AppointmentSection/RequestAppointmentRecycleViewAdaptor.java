package com.medical.mypockethealth.Adaptors.ProviderSection.AppointmentSection;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.PatientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.ProviderSection.BookingSection.ChangeBookingStatusCaller;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestAppointmentRecycleViewAdaptor extends 
        RecyclerView.Adapter<RequestAppointmentRecycleViewAdaptor.InnerRequestAppointmentRecycleViewAdaptor> 
        implements ChangeBookingStatusCaller.CallBackFromChangeBookingStatusCaller {


    private List<PatientInformation> patientInformation;
    private final callBackFromRequestAppointmentRecycleViewAdaptor callBackFromRequestAppointmentRecycleViewAdaptor;
    private final Context context;
    private static boolean state=false;
    private InnerRequestAppointmentRecycleViewAdaptor innerRequestAppointmentRecycleViewAdaptor;
    private final Handler handler=new Handler();
    
    public RequestAppointmentRecycleViewAdaptor(List<PatientInformation> patientInformation, RequestAppointmentRecycleViewAdaptor.
            callBackFromRequestAppointmentRecycleViewAdaptor callBackFromRequestAppointmentRecycleViewAdaptor, Context context) {
        this.patientInformation = patientInformation;
        this.callBackFromRequestAppointmentRecycleViewAdaptor = callBackFromRequestAppointmentRecycleViewAdaptor;
        this.context = context;
    }

    @Override
    public void confirmationCancelBookingCaller(ResponseInformation responseInformation,PatientInformation patientInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                innerRequestAppointmentRecycleViewAdaptor.setDefault();

                state=false;
                
                if(responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
                {

                    Toast.makeText(context,responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if(callBackFromRequestAppointmentRecycleViewAdaptor!=null)
                    {
                        callBackFromRequestAppointmentRecycleViewAdaptor.refresh(patientInformation);
                    }
                }
            }
        });
    }

    public interface callBackFromRequestAppointmentRecycleViewAdaptor
    {

        void refresh(PatientInformation patientInformation);
        
    }



    @NonNull
    @Override
    public InnerRequestAppointmentRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerRequestAppointmentRecycleViewAdaptor(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.patient_inner_appointment_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerRequestAppointmentRecycleViewAdaptor holder, int position) {


        holder.accept_loading.setVisibility(View.GONE);
        holder.reject_loading.setVisibility(View.GONE);
        holder.accept.setVisibility(View.VISIBLE);
        holder.reject.setVisibility(View.VISIBLE);

        this.innerRequestAppointmentRecycleViewAdaptor=holder;

        holder.name.setText(patientInformation.get(position).getPatientName());
        holder.age.setText(patientInformation.get(position).getPatientAge());
        holder.date.setText(patientInformation.get(position).getDate());
        holder.slot_time.setText(patientInformation.get(position).getSlotTime());
        Picasso.with(context).load(Uri.parse(patientInformation.get(position).getProfileImage())).error(R.drawable.profile).into(holder.profile_image);

        
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(state)
               {
                   Toast.makeText(context,"Please wait..!",Toast.LENGTH_SHORT).show();
               }
               else 
               {
                   if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                   {

                       AlertDialog.Builder builder=new AlertDialog.Builder(context)
                               .setMessage("Are you sure you want to accept this booking request").setNegativeButton("NO", (dialogInterface, i) -> {

                               }).setPositiveButton("YES", (dialogInterface, i) -> {

                                   holder.accept.setVisibility(View.GONE);
                                   holder.accept_loading.setVisibility(View.VISIBLE);

                                   state=true;

                           new Thread(new ChangeBookingStatusCaller(patientInformation.get(position),
                                   RequestAppointmentRecycleViewAdaptor.this,"1",context)).start();


                               });
                       builder.setCancelable(false);
                       builder.show();
                   }

                   else
                   {

                       holder.accept.setVisibility(View.VISIBLE);
                       holder.accept_loading.setVisibility(View.GONE);

                       DialogShower dialogShower=new DialogShower(R.layout.internet_error,context);
                       dialogShower.showDialog();
                   }
               }
               
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state)
                {
                    Toast.makeText(context,"Please wait..!",Toast.LENGTH_SHORT).show();
                }
                else 
                {
                    if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                    {


                        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                                .setMessage("Are you sure you want to reject this booking request").setNegativeButton("NO", (dialogInterface, i) -> {

                                }).setPositiveButton("YES", (dialogInterface, i) -> {

                                    holder.reject.setVisibility(View.GONE);
                                    holder.reject_loading.setVisibility(View.VISIBLE);
                                    state=true;
                                    
                                    new Thread(new ChangeBookingStatusCaller(patientInformation.get(position),
                                            RequestAppointmentRecycleViewAdaptor.this,"3",context)).start();

                                });
                        builder.setCancelable(false);
                        builder.show();
                    }

                    else
                    {
                        holder.reject.setVisibility(View.VISIBLE);
                        holder.reject_loading.setVisibility(View.GONE);

                        DialogShower dialogShower=new DialogShower(R.layout.internet_error,context);
                        dialogShower.showDialog();
                    }

                }

                
            }
        });
        
    }

    public void loadData(List<PatientInformation> patientInformation)
    {
        this.patientInformation=patientInformation;
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemCount() {
        return (patientInformation!=null && patientInformation.size()!=0 ? patientInformation.size():0);
    }

    static class InnerRequestAppointmentRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView name,age,date,slot_time,accept,reject;
         ImageView profile_image,accept_loading,reject_loading;

        public InnerRequestAppointmentRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);
            
            profile_image=itemView.findViewById(R.id.profile_image);
            name=itemView.findViewById(R.id.name);
            age=itemView.findViewById(R.id.age);
            date=itemView.findViewById(R.id.date);
            slot_time=itemView.findViewById(R.id.slot_time);
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
            accept_loading=itemView.findViewById(R.id.accept_loading);
            reject_loading=itemView.findViewById(R.id.reject_loading);
        }

        public void setDefault()
        {
            accept_loading.setVisibility(View.GONE);
            reject_loading.setVisibility(View.GONE);
            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
        }
    }
    
    
}
