package com.medical.mypockethealth.ClientFragments.DoctorBookingSection;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.PatientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.BookingSection.ClientBookingSection;
import com.medical.mypockethealth.ClientFragments.WalletSection.PaymentFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.UserSection.BookingSection.BookDoctorCaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class DoctorBookingAddPaymentFragment extends Fragment implements BookDoctorCaller.CallbackFromBookDoctorCaller{

    private static final String TAG = "DoctorBookingAddPayment";

    public static final String data_key="data_key";
    
    private Button next;
    private ImageView loading;
    private EditText name,surname,card_number,expiry_date,cvv;
    private final Handler handler = new Handler();
    private boolean nameConfirmation=false,surNameConfirmation=false,cardNumber_confirmation=false,expiryDate_confirmation=false,cvv_confirmation=false,isCardFilled=false,expiry_length=false,
            isExpiryDateFilled=false,isCvvFilled=false,isNameFilled=false,isSurnameFilled=false;
    String name_assign=null,surname_assign = null,card_assign=null,expiryDate_assign=null,cvv_assign=null;

    public DoctorBookingAddPaymentFragment() {
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
        return inflater.inflate(R.layout.fragment_doctor_booking_add_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        establishViews(view);
        
        loading.setVisibility(View.GONE);

        initializePayStack();

        expiryWatcher();
        
        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new PaymentFragment()).addToBackStack(null).commit();
            }
        });

        
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefault();

                if (name.getText().toString().trim().length() == 0) {

                    name.setError("Please enter name");

                    isNameFilled = false;

                } else if (profile_name_checker(name.getText().toString())) {
                    name.setError("spacial characters and numbers not allowed ");
                } else {

                    name.setError(null);
                    name_assign = name.getText().toString();
                    nameConfirmation = true;
                    isNameFilled = true;
                }

                if (surname.getText().toString().trim().length() == 0) {

                    surname.setError("Please enter surname");

                    isSurnameFilled = false;

                } else if (profile_name_checker(surname.getText().toString())) {
                    surname.setError("spacial characters and numbers not allowed ");
                } else {

                    surname.setError(null);
                    surname_assign = surname.getText().toString();
                    surNameConfirmation = true;
                    isSurnameFilled = true;
                }

                if(card_number.getText().toString().length()==0)
                {

                    card_number.setError("Please enter your card number");

                    isCardFilled=false;

                }

                else
                {

                    card_number.setError(null);
                    card_assign=card_number.getText().toString();
                    cardNumber_confirmation=true;
                    isCardFilled=true;
                }

                if(expiry_date.getText().toString().length()==0)
                {

                    expiry_date.setError("Please enter your expiry date");

                    isExpiryDateFilled=false;

                }

                else
                {

                    expiry_date.setError(null);
                    expiryDate_assign=expiry_date.getText().toString();
                    expiryDate_confirmation=true;
                    isExpiryDateFilled=true;
                }

                if(cvv.getText().toString().length()==0)
                {
                    cvv.setError("Please enter your cvv");

                    isCvvFilled=false;

                }

                else
                {

                    cvv.setError(null);
                    cvv_assign=cvv.getText().toString();
                    cvv_confirmation=true;
                    isCvvFilled=true;
                }



                if(nameConfirmation && surNameConfirmation && cardNumber_confirmation && expiryDate_confirmation &&
                        cvv_confirmation && isCardFilled && isExpiryDateFilled && isCvvFilled && isNameFilled && isSurnameFilled) {

                    if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                        loading.setVisibility(View.VISIBLE);
                        next.setVisibility(View.GONE);
                        disableFields();

                         performCharge();


                    } else {

                        enableFields();

                        DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                        dialogShower.showDialog();

                    }
                }

            }
        });


    }


    private void enableFields()
    {
        name.setEnabled(true);
        surname.setEnabled(true);
        card_number.setEnabled(true);
        expiry_date.setEnabled(true);
        cvv.setEnabled(true);
    }
    private void disableFields()
    {
        name.setEnabled(false);
        surname.setEnabled(false);
        card_number.setEnabled(false);
        expiry_date.setEnabled(false);
        cvv.setEnabled(false);

    }


    private void createBooking() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            new Thread(new BookDoctorCaller(DoctorBookingConfirmFragment.doctorInformation,
                    DoctorBookingAddPaymentFragment.this, getContext())).start();


        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }

    private void initializePayStack() {

        PaystackSdk.initialize(getContext());
        PaystackSdk.setPublicKey(URLBuilder.PayStackKeys.pk_live_3cb8070e3130e6b187652a6dfbd11efd9cd3919f.toString());
    }

    private void performCharge() {

        int amount=(int)Double.parseDouble(DoctorBookingConfirmFragment.doctorInformation.getConsultCharges());

        String[] cardExpiryArray = expiryDate_assign.split("/");
        int expiryMonth = Integer.parseInt(cardExpiryArray[0]);
        int expiryYear = Integer.parseInt(cardExpiryArray[1]);
        amount *= 10;
        amount *= 10;
        Card card = new Card(card_assign, expiryMonth, expiryYear, cvv_assign);
        Charge charge = new Charge();
        charge.setAmount(amount);
        charge.setEmail(URLBuilder.UrlMethods.payStackEmail);
        charge.setCard(card);
        charge.setCurrency(URLBuilder.PayStackKeys.ZAR.toString());

        PaystackSdk.chargeCard(getActivity(), charge, new Paystack.TransactionCallback() {

            @Override
            public void onSuccess(Transaction transaction) {

                createBooking();

                Toast.makeText(getContext(), "payment done", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onSuccess: payment done");

            }

            @Override
            public void beforeValidate(Transaction transaction) {

                Log.d("Main Activity", "beforeValidate: " + transaction.getReference());

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

                loading.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                enableFields();
                dialogCaller();

                Log.d("Main Activity", "onError: " + error.getLocalizedMessage());
                Log.d("Main Activity", "onError: " + error);
            }
        });

    }



    private void dialogCaller()
    {
        Dialog paymentBox=new Dialog(getContext());
        paymentBox.setContentView(R.layout.payment_error_layout);
        paymentBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        paymentBox.setCanceledOnTouchOutside(false);
        paymentBox.setCancelable(false);
        Window window=paymentBox.getWindow();
        window.setGravity(Gravity.CENTER);
        paymentBox.show();

        paymentBox.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentBox.dismiss();
            }
        });

    }

    public boolean profile_name_checker(String value) {
        boolean result=false;
        Pattern pattern=Pattern.compile("[^a-zA-Z]");
        Matcher matcher=pattern.matcher(value);

        while (matcher.find())
        {
            result=true;

        }
        return result;
    }


    private void setDefault()
    {
        name.setError(null);
        surname.setError(null);
        card_number.setError(null);
        expiry_date.setError(null);
        cvv.setError(null);
        nameConfirmation = false;
        surNameConfirmation = false;
        cardNumber_confirmation = false;
        expiryDate_confirmation = false;
        cvv_confirmation = false;
        isCardFilled = false;
        expiry_length = false;
        isExpiryDateFilled = false;
        isCvvFilled = false;
        isNameFilled = false;
        isSurnameFilled = false;
        name_assign = null;
        surname_assign = null;
        card_assign = null;
        expiryDate_assign = null;
        cvv_assign = null;

    }

    private void establishViews(View view) {

        next = view.findViewById(R.id.login);
        loading = view.findViewById(R.id.loading);
        name = view.findViewById(R.id.name);
        surname = view.findViewById(R.id.surname);
        card_number = view.findViewById(R.id.card_number);
        expiry_date = view.findViewById(R.id.expiry_date);
        cvv = view.findViewById(R.id.cvv);

    }

    private void expiryWatcher()
    {

        expiry_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 2) {
                    if(start==2 && before==1 && !s.toString().contains("/")){
                        expiry_date.setText(""+s.toString().charAt(0));
                        expiry_date.setSelection(1);
                    }
                    else {
                        expiry_date.setText(s + "/");
                        expiry_date.setSelection(3);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }


    @Override
    public void confirmation(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                
                loading.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                enableFields();

                if(responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {

                    sendAppointmentRequest(responseInformation.getBookingId());

                    Toast.makeText(getContext(),"Slot booked Successfully",Toast.LENGTH_SHORT).show();

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new ClientBookingSection()).addToBackStack(null).commit();
                }
                
            }
        });

    }


    private void sendAppointmentRequest(String bookingId)
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.BookingRequest);

        String random_key_generator=databaseReference.push().getKey();

        PatientInformation patientInformation=new PatientInformation(DoctorBookingConfirmFragment.doctorInformation.getPatientName(),
                DoctorBookingConfirmFragment.doctorInformation.getPatientAge(),DoctorBookingConfirmFragment.doctorInformation.getSelected_date(),
                DoctorBookingConfirmFragment.doctorInformation.getSlotTime(),random_key_generator,getProfileImage(),bookingId,ClientMainFrame.id);

        assert random_key_generator != null;

        databaseReference.child(DoctorBookingConfirmFragment.doctorInformation.getId()).child(random_key_generator).setValue(patientInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {





            }
        });



    }

    private String getProfileImage()
    {
        SharedPreferences preferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(json, ClientInformation.class);

        return clientInformation.getProfileImage();
    }
}