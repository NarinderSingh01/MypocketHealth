package com.medical.mypockethealth.Adaptors.UserSection.BookingSection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.BookDoctorInformation.BookDoctorInformation;
import com.medical.mypockethealth.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BookedDoctorsRecycleViewAdaptor extends RecyclerView.Adapter<BookedDoctorsRecycleViewAdaptor.InnerBookedDoctorsRecycleViewAdaptor> {

    private static final String TAG = "BookedDoctorsRecycleVie";


    private List<BookDoctorInformation> bookDoctorInformation;

    private final callBackFromBookedDoctorsRecycleViewAdaptor callBackFromBookedDoctorsRecycleViewAdaptor;

    private final Context context;

    public BookedDoctorsRecycleViewAdaptor(List<BookDoctorInformation> bookDoctorInformation,
                                           BookedDoctorsRecycleViewAdaptor.callBackFromBookedDoctorsRecycleViewAdaptor
                                                   callBackFromBookedDoctorsRecycleViewAdaptor, Context context) {
        this.bookDoctorInformation = bookDoctorInformation;
        this.callBackFromBookedDoctorsRecycleViewAdaptor = callBackFromBookedDoctorsRecycleViewAdaptor;
        this.context = context;
    }

    public interface callBackFromBookedDoctorsRecycleViewAdaptor {

        void cancelBooking(BookDoctorInformation bookDoctorInformation);

        void viewDetail(BookDoctorInformation bookDoctorInformation);

    }


    @NonNull
    @Override
    public InnerBookedDoctorsRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerBookedDoctorsRecycleViewAdaptor(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.booked_doctor_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerBookedDoctorsRecycleViewAdaptor holder, int position) {

        holder.booking_cancelled_tag.setVisibility(View.GONE);
        holder.cancel.setVisibility(View.GONE);

        String name;

        if (bookDoctorInformation.get(position).getUserTitle() != null && bookDoctorInformation.get(position).getUserType() != null) {

            name = bookDoctorInformation.get(position).getUserTitle() + " " +
                    bookDoctorInformation.get(position).getFirstName().charAt(0) + " " + bookDoctorInformation.get(position).getSurName();

        } else {

            name = "Dr. " + bookDoctorInformation.get(position).getFirstName().charAt(0) + " " + bookDoctorInformation.get(position).getSurName();

        }

//        String name="Dr. " + bookDoctorInformation.get(position).getFirstName().charAt(0)+" "+bookDoctorInformation.get(position).getSurName();
        holder.name.setText(name);


        holder.speciality.setText(bookDoctorInformation.get(position).getSpecialization());

        String experience = bookDoctorInformation.get(position).getExperience() + " yrs experience";
        holder.experience.setText(experience);


        holder.date.setText(filterDate(bookDoctorInformation.get(position).getDate()));

        holder.time.setText(bookDoctorInformation.get(position).getSlotTime());

//        holder.booking_id.setText(bookDoctorInformation.get(position).getId());

        Picasso.with(context).load(Uri.parse(bookDoctorInformation.get(position).getProfileImage())).error(R.drawable.profile).into(holder.profile_image);

        if (bookDoctorInformation.get(position).getBookingStatus().equals("2")) {
            holder.booking_cancelled_tag.setVisibility(View.VISIBLE);
            holder.cancel.setVisibility(View.GONE);
        } else {
            holder.booking_cancelled_tag.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.VISIBLE);
        }

        holder.view_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callBackFromBookedDoctorsRecycleViewAdaptor != null) {
                    callBackFromBookedDoctorsRecycleViewAdaptor.viewDetail(bookDoctorInformation.get(position));
                }

            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callBackFromBookedDoctorsRecycleViewAdaptor != null) {
                    callBackFromBookedDoctorsRecycleViewAdaptor.cancelBooking(bookDoctorInformation.get(position));
                }
            }
        });


        @SuppressLint("SimpleDateFormat") SimpleDateFormat dfDate = new SimpleDateFormat("yy-MM-dd");
        Date date = new Date();

        String start_date = dfDate.format(date);


        if (CheckDates(start_date, bookDoctorInformation.get(position).getDate()) == 3) {
            holder.cancel.setVisibility(View.GONE);
        } else {


            if (bookDoctorInformation.get(position).getBookingStatus().equals("2")) {
                holder.cancel.setVisibility(View.GONE);
            } else {
                holder.cancel.setVisibility(View.VISIBLE);

            }

        }

    }

    private String filterDate(String date) {
        String[] value = date.split("");

        StringBuilder stringBuilder = new StringBuilder();

        String year, month, day;

        int count = 0;

        for (int i = 0; i < value.length; i++) {

            if (value[i].equals("-")) {

                count = i;

                break;
            } else {
                stringBuilder.append(value[i]);

            }
        }

        year = stringBuilder.toString();

        stringBuilder.setLength(0);


        for (int i = count + 1; i < value.length; i++) {

            if (value[i].equals("-")) {

                count = i;

                break;
            } else {
                stringBuilder.append(value[i]);

            }

        }

        month = stringBuilder.toString();

        stringBuilder.setLength(0);

        for (int i = count + 1; i < value.length; i++) {

            if (value[i].equals("-")) {

                count = i;

                break;
            } else {
                stringBuilder.append(value[i]);

            }

        }

        day = stringBuilder.toString();

        stringBuilder.setLength(0);


        return setFilteredDate(year, month, day);

    }

    private String setFilteredDate(String year, String month, String day) {

        String result = "";

        try {
            result = context.getResources().getStringArray(R.array.month_names)[Integer.parseInt(month) - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            result = Integer.toString(Integer.parseInt(month) - 1);
        }


        return day + ", " + result + " " + year;

    }

    private int CheckDates(String startDate, String endDate) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dfDate = new SimpleDateFormat("yy-MM-dd");

        int b = 0;
        try {
            if (Objects.requireNonNull(dfDate.parse(startDate)).before(dfDate.parse(endDate))) {
                b = 1;//If start date is before end date
            } else if (Objects.equals(dfDate.parse(startDate), dfDate.parse(endDate))) {
                b = 2;//If two dates are equal
            } else {
                b = 3; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    public void loadData(List<BookDoctorInformation> bookDoctorInformation) {
        this.bookDoctorInformation = bookDoctorInformation;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (bookDoctorInformation != null && bookDoctorInformation.size() != 0 ? bookDoctorInformation.size() : 0);
    }

    static class InnerBookedDoctorsRecycleViewAdaptor extends RecyclerView.ViewHolder {

        ImageView profile_image;
        TextView name, qualification, speciality, experience, date, time, cancel, view_detail, booking_id, booking_cancelled_tag;

        public InnerBookedDoctorsRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            speciality = itemView.findViewById(R.id.speciality);
            experience = itemView.findViewById(R.id.experience);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            cancel = itemView.findViewById(R.id.cancel);
            view_detail = itemView.findViewById(R.id.view_detail);
            booking_id = itemView.findViewById(R.id.booking_id);
            booking_cancelled_tag = itemView.findViewById(R.id.booking_cancelled_tag);
        }
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
