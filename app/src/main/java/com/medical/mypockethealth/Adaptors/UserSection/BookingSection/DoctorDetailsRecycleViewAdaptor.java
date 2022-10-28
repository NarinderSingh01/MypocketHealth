package com.medical.mypockethealth.Adaptors.UserSection.BookingSection;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class DoctorDetailsRecycleViewAdaptor extends RecyclerView.Adapter<DoctorDetailsRecycleViewAdaptor.InnerDoctorDetailsRecycleViewAdaptor> {

    private List<DoctorInformation> doctorInformation;
    private final callBackFromDoctorDetailsRecycleViewAdaptor callBackFromDoctorDetailsRecycleViewAdaptor;
    private final Context context;

    public DoctorDetailsRecycleViewAdaptor(List<DoctorInformation> doctorInformation,
                                           DoctorDetailsRecycleViewAdaptor.callBackFromDoctorDetailsRecycleViewAdaptor callBackFromDoctorDetailsRecycleViewAdaptor,
                                           Context context) {
        this.doctorInformation = doctorInformation;
        this.callBackFromDoctorDetailsRecycleViewAdaptor = callBackFromDoctorDetailsRecycleViewAdaptor;
        this.context = context;
    }

    public interface callBackFromDoctorDetailsRecycleViewAdaptor {

        void book(DoctorInformation doctorInformation);

        void showDescription(DoctorInformation doctorInformation);

        void call(int position);

    }

    @NonNull
    @Override
    public InnerDoctorDetailsRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerDoctorDetailsRecycleViewAdaptor(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerDoctorDetailsRecycleViewAdaptor holder, int position) {


//        String name = "Dr. " + getFirstCharacter(doctorInformation.get(position).getFirstName()) + " " + doctorInformation.get(position).getSurName();

        String name;

        if (doctorInformation.get(position).getUserTitle() != null && doctorInformation.get(position).getUserType() != null) {

            name = doctorInformation.get(position).getUserTitle() + " " +
                    getFirstCharacter(doctorInformation.get(position).getFirstName()) + " " + doctorInformation.get(position).getSurName();

        } else {

            name = "Dr. " + getFirstCharacter(doctorInformation.get(position).getFirstName()) + " " + doctorInformation.get(position).getSurName();

        }

        holder.name.setText(name);

        holder.speciality.setText(doctorInformation.get(position).getSpecialization());

        String experience = doctorInformation.get(position).getExperience() + " yrs experience";
        holder.experience.setText(experience);

        String charges = URLBuilder.CurrencySign.R.toString() + " " + doctorInformation.get(position).getConsultCharges();
        holder.charges.setText(charges);

        holder.hospitalName.setText(doctorInformation.get(position).getWorkLocation());
        holder.address.setText(doctorInformation.get(position).getCity());

        String statement = "Consult online for R " + doctorInformation.get(position).getConsultCharges();

        holder.statement.setText(statement);

        Picasso.with(context).load(Uri.parse(doctorInformation.get(position).getProfileImage())).error(R.drawable.profile).into(holder.profile_image);


        if (doctorInformation.get(position).getStatus() != null) {

            if (doctorInformation.get(position).getStatus().equals("1")) {

                holder.book_layout.setBackgroundResource(R.drawable.book_layout);
                holder.book.setText("Book");

                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (callBackFromDoctorDetailsRecycleViewAdaptor != null) {
                            callBackFromDoctorDetailsRecycleViewAdaptor.book(doctorInformation.get(position));
                        }
                    }
                });

            } else {

                holder.book.setText("Unavailable");
                holder.book_layout.setBackgroundResource(R.drawable.unable_layout);

            }
//            if (doctorInformation.get(position).getStatus().equals("0"))  {
//
//                holder.book.setText("Unavailable");
//                holder.book_layout.setBackgroundResource(R.drawable.unable_layout);
//
//            }
        }

    }


    private char getFirstCharacter(String firstName) {
        return firstName.charAt(0);
    }

    @Override
    public int getItemCount() {

        return (doctorInformation != null && doctorInformation.size() != 0 ? doctorInformation.size() : 0);

    }

    public void loadData(List<DoctorInformation> doctorInformation) {
        this.doctorInformation = doctorInformation;
        notifyDataSetChanged();
    }

    static class InnerDoctorDetailsRecycleViewAdaptor extends RecyclerView.ViewHolder {

        ImageView profile_image;
        LinearLayout book_layout;
        TextView name, qualification, speciality, experience, charges, more, hospitalName, address, statement, book, view_more;

        public InnerDoctorDetailsRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            speciality = itemView.findViewById(R.id.speciality);
            experience = itemView.findViewById(R.id.experience);
            charges = itemView.findViewById(R.id.charges);
            book = itemView.findViewById(R.id.book);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            address = itemView.findViewById(R.id.address);
            statement = itemView.findViewById(R.id.statement);
            view_more = itemView.findViewById(R.id.view_more);
            book_layout = itemView.findViewById(R.id.book_layout);

        }
    }

//    private String getProviderType(DoctorInformation providerInformation) {
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
