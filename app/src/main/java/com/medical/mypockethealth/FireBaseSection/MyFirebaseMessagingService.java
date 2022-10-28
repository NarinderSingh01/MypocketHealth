package com.medical.mypockethealth.FireBaseSection;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatRequestInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.R;

import java.util.Objects;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    private final int NOTIFICATION_ID = 10;
    private final NotificationChannel channel = null;
    private Notification notification;
    private NotificationChannel mChannel;
    private Intent notificationIntent;
    private Intent voiceCutIntent;
    private final String checkType = "chatNotification";
    private VideoChatRequestInformation videoChatRequestInformation;

    String bigContent;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "onMessageReceived: push called " + remoteMessage.getData().get(URLBuilder.Parameter.type.toString()));
        Log.d(TAG, "onMessageReceived: push called " + remoteMessage.getData().get(URLBuilder.Parameter.message.toString()));
        Log.d(TAG, "onMessageReceived: push called " + remoteMessage.getData().get(URLBuilder.Parameter.toke.toString()));


        //---------------------------------------------------------------------------------------------------------------------------//

        // this section is for manage video call with provider on screen and off screen

        if (Objects.requireNonNull(remoteMessage.getData().get(URLBuilder.Parameter.type.toString()))
                .equalsIgnoreCase(URLBuilder.Type.VideoChatRequest.toString())) {

            ProviderInformation providerInformation = new ProviderInformation();

            providerInformation.setFirstName(remoteMessage.getData().get(URLBuilder.Parameter.firstName.toString()));
            providerInformation.setSurName(remoteMessage.getData().get(URLBuilder.Parameter.surName.toString()));
            providerInformation.setSpecialization(remoteMessage.getData().get(URLBuilder.Parameter.speciality.toString()));
            providerInformation.setExperience(remoteMessage.getData().get(URLBuilder.Parameter.experience.toString()));
            providerInformation.setBio(remoteMessage.getData().get(URLBuilder.Parameter.bio.toString()));
            providerInformation.setWorkLocation(remoteMessage.getData().get(URLBuilder.Parameter.workLocation.toString()));
            providerInformation.setAddress(remoteMessage.getData().get(URLBuilder.Parameter.address.toString()));
            providerInformation.setProfileImage(remoteMessage.getData().get(URLBuilder.Parameter.profileImage.toString()));

            videoChatRequestInformation = new VideoChatRequestInformation(remoteMessage.getData().get(URLBuilder.Parameter.toke.toString()),
                    remoteMessage.getData().get(URLBuilder.Parameter.channelName.toString()), remoteMessage.getData().get(URLBuilder.Parameter.rtmToken.toString()),
                    remoteMessage.getData().get(URLBuilder.Parameter.mainId.toString()),
                    providerInformation, null);


        }


        //-----------------------------------------------------------------------------------------------------------------------------------//


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.d(TAG, "onMessageReceived: more");

            if (ClientMainFrame.patientAppStatus && videoChatRequestInformation != null) {

                if (videoChatRequestInformation.getProviderInformation().getUserTitle() != null &&
                        videoChatRequestInformation.getProviderInformation().getUserTitle().length() != 0) {

                    String userMessage = videoChatRequestInformation.getProviderInformation().getUserTitle() +
                            videoChatRequestInformation.getProviderInformation().getFirstName().charAt(0) + " " +
                            videoChatRequestInformation.getProviderInformation().getSurName() + " invited you to join on video call";
                    setBookingOreoNotification(URLBuilder.Title.unKnown,
                            userMessage,
                            Objects.requireNonNull(URLBuilder.Title.unKnown), "", bigContent);

                } else {

                    String userMessage = "Dr. " + videoChatRequestInformation.getProviderInformation().getFirstName().charAt(0) + " " +
                            videoChatRequestInformation.getProviderInformation().getSurName() + " invited you to join on video call";
                    setBookingOreoNotification(URLBuilder.Title.unKnown,
                            userMessage,
                            Objects.requireNonNull(URLBuilder.Title.unKnown), "", bigContent);

                }


            } else {

                // this section managing the notification of user and provider with BookingRequest

                if (Objects.equals(remoteMessage.getData().get(URLBuilder.Parameter.type.toString()), URLBuilder.Type.BookingRequest.toString())) {
                    String message = remoteMessage.getData().get(URLBuilder.Parameter.message.toString());

                    assert message != null;

                    if (message.charAt(0) == 'B') {
                        setBookingOreoNotification(remoteMessage.getData().get(URLBuilder.Parameter.title.toString()),
                                remoteMessage.getData().get(URLBuilder.Parameter.message.toString()),
                                Objects.requireNonNull(URLBuilder.Title.unKnown), "", bigContent);
                    } else {

                        if (ProviderMainFrame.providerAppStatus) {
                            setBookingOreoNotification(remoteMessage.getData().get(URLBuilder.Parameter.title.toString()),
                                    remoteMessage.getData().get(URLBuilder.Parameter.message.toString()),
                                    Objects.requireNonNull(URLBuilder.Title.unKnown), "", bigContent);
                        } else {
                            setBookingOreoNotification(remoteMessage.getData().get(URLBuilder.Parameter.title.toString()),
                                    remoteMessage.getData().get(URLBuilder.Parameter.message.toString()),
                                    Objects.requireNonNull(remoteMessage.getData().get(URLBuilder.Parameter.type.toString())), "", bigContent);
                        }

                    }

                } else {
                    setBookingOreoNotification(remoteMessage.getData().get(URLBuilder.Parameter.title.toString()),
                            remoteMessage.getData().get(URLBuilder.Parameter.message.toString()),
                            Objects.requireNonNull(remoteMessage.getData().get(URLBuilder.Parameter.type.toString())), "", bigContent);
                }

            }


        } else {

            if (ClientMainFrame.patientAppStatus) {

//                String userMessage="Dr. "+videoChatRequestInformation.getProviderInformation().getFirstName().charAt(0)+" "+
//                        videoChatRequestInformation.getProviderInformation().getSurName()+" invited you to join on video call";
//                setBookingOreoNotification(URLBuilder.Title.unKnown,
//                        userMessage,
//                        Objects.requireNonNull(URLBuilder.Title.unKnown), "", bigContent);

                if (videoChatRequestInformation.getProviderInformation().getUserTitle() != null &&
                        videoChatRequestInformation.getProviderInformation().getUserTitle().length() != 0) {

                    String userMessage = videoChatRequestInformation.getProviderInformation().getUserTitle() +
                            videoChatRequestInformation.getProviderInformation().getFirstName().charAt(0) + " " +
                            videoChatRequestInformation.getProviderInformation().getSurName() + " invited you to join on video call";
                    setBookingOreoNotification(URLBuilder.Title.unKnown,
                            userMessage,
                            Objects.requireNonNull(URLBuilder.Title.unKnown), "", bigContent);

                } else {

                    String userMessage = "Dr. " + videoChatRequestInformation.getProviderInformation().getFirstName().charAt(0) + " " +
                            videoChatRequestInformation.getProviderInformation().getSurName() + " invited you to join on video call";
                    setBookingOreoNotification(URLBuilder.Title.unKnown,
                            userMessage,
                            Objects.requireNonNull(URLBuilder.Title.unKnown), "", bigContent);

                }

            } else {
                // this section managing the notification of user and provider with BookingRequest

                if (Objects.equals(remoteMessage.getData().get(URLBuilder.Parameter.type.toString()), URLBuilder.Type.BookingRequest.toString())) {

                    String message = remoteMessage.getData().get(URLBuilder.Parameter.message.toString());

                    assert message != null;

                    if (message.charAt(0) == 'B') {
                        showNotification(remoteMessage.getData().get(URLBuilder.Parameter.title.toString()),
                                remoteMessage.getData().get(URLBuilder.Parameter.message.toString()), "",
                                Objects.requireNonNull(URLBuilder.Title.unKnown), "", bigContent);
                    } else {

                        if (ProviderMainFrame.providerAppStatus) {
                            showNotification(remoteMessage.getData().get(URLBuilder.Parameter.title.toString()),
                                    remoteMessage.getData().get(URLBuilder.Parameter.message.toString()), "",
                                    Objects.requireNonNull(URLBuilder.Title.unKnown), "", bigContent);
                        } else {
                            showNotification(remoteMessage.getData().get(URLBuilder.Parameter.title.toString()),
                                    remoteMessage.getData().get(URLBuilder.Parameter.message.toString()), "",
                                    Objects.requireNonNull(remoteMessage.getData().get(URLBuilder.Parameter.type.toString())), "", bigContent);
                        }

                    }
                } else {
                    showNotification(remoteMessage.getData().get(URLBuilder.Parameter.title.toString()),
                            remoteMessage.getData().get(URLBuilder.Parameter.message.toString()), "",
                            Objects.requireNonNull(remoteMessage.getData().get(URLBuilder.Parameter.type.toString())), "", bigContent);
                }

            }


        }


        // this section is for to manage bookingRequest status for user and provider


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void showNotification(String title, String message, String sx, String type, String image, String bigContent) {
        Intent intent = null;
        if (!type.equalsIgnoreCase("")) {
            intent = new Intent(this, BaseActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BaseActivity.data_key, videoChatRequestInformation);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_IMMUTABLE);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Random random = new Random();
        final int m = random.nextInt(9999 - 1000) + 1000;
        if (!type.equalsIgnoreCase("")) {

            if (type.equalsIgnoreCase(URLBuilder.Type.VideoChatRequest.toString())) {
                final Notification.Builder builder = new Notification.Builder(getApplicationContext());
                Notification.BigTextStyle textStyle = new Notification.BigTextStyle(builder);
                if (image.equalsIgnoreCase("")) {
                    builder.setStyle(new Notification.BigTextStyle(builder)
                            .bigText(sx)
                            .setBigContentTitle("Chat Notification")
                            .setSummaryText("Notification"))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setContentIntent(pendingIntent)
                            .setGroup(type)
                            .setSmallIcon(R.drawable.main);

                    notificationManager.notify(m, builder.build());
                } else {
                    Notification.BigPictureStyle pictureStyle = new Notification.BigPictureStyle(builder);
                    builder.setStyle(pictureStyle
                            .setBigContentTitle(bigContent)
                            .setSummaryText("Notification"))
                            .setContentTitle(title)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setGroup(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setSmallIcon(R.drawable.main);


                }
            }

            if (type.equalsIgnoreCase(URLBuilder.Type.BookingRequest.toString())) {
                final Notification.Builder builder = new Notification.Builder(getApplicationContext());
                Notification.BigTextStyle textStyle = new Notification.BigTextStyle(builder);
                if (image.equalsIgnoreCase("")) {
                    builder.setStyle(new Notification.BigTextStyle(builder)
                            .bigText(sx)
                            .setBigContentTitle("Chat Notification")
                            .setSummaryText("Notification"))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setContentIntent(pendingIntent)
                            .setGroup(type)
                            .setSmallIcon(R.drawable.main);

                    notificationManager.notify(m, builder.build());
                } else {
                    Notification.BigPictureStyle pictureStyle = new Notification.BigPictureStyle(builder);
                    builder.setStyle(pictureStyle
                            .setBigContentTitle(bigContent)
                            .setSummaryText("Notification"))
                            .setContentTitle(title)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setGroup(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setSmallIcon(R.drawable.main);


                }
            } else {
                final Notification.Builder builder = new Notification.Builder(getApplicationContext());
                Notification.BigTextStyle textStyle = new Notification.BigTextStyle(builder);
                if (image.equalsIgnoreCase("")) {
                    builder.setStyle(new Notification.BigTextStyle(builder)
                            .bigText(sx)
                            .setBigContentTitle("Chat Notification")
                            .setSummaryText("Notification"))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
//                             .setContentIntent(pendingIntent)
                            .setGroup(type)
                            .setSmallIcon(R.drawable.main);

                    notificationManager.notify(m, builder.build());
                } else {
                    Notification.BigPictureStyle pictureStyle = new Notification.BigPictureStyle(builder);
                    builder.setStyle(pictureStyle
                            .setBigContentTitle(bigContent)
                            .setSummaryText("Notification"))
                            .setContentTitle(title)
                            .setAutoCancel(true)
//                             .setContentIntent(pendingIntent)
                            .setGroup(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setSmallIcon(R.drawable.main);


                }
            }

        } else {
            notification = new NotificationCompat.Builder(this)
                    .setGroup(type)
                    .setContentText(message)
                    .setContentTitle(title)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.main)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{200, 200, 200, 200})
                    .setSound(defaultSound)
                    .build();
            notificationManager.notify(m, notification);
        }
    }

    public void setNotification(String title, String body) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.drawable.main).setContentTitle(title).setContentText(body).setAutoCancel(true);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);


        notificationManagerCompat.notify(101, builder.build());

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void setBookingOreoNotification(String title, String message, String type, String image, String bigContent) {
        Intent intent = null;
        if (!type.equalsIgnoreCase("")) {

            intent = new Intent(this, BaseActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BaseActivity.data_key, videoChatRequestInformation);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101,
                intent, PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

// Sets an ID for the notification, so it can be updated.
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "@MyPocketHealth ";// The user-visible name of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
        final Notification.Builder builder = new Notification.Builder(getApplicationContext());
// Create a notification and set the notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setGroup(type)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.main)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{200, 200, 200, 200})
                    .setSound(defaultSound)
                    .setChannelId(CHANNEL_ID)
                    .build();

            if (!type.equalsIgnoreCase("")) {

                if (type.equalsIgnoreCase(URLBuilder.Type.VideoChatRequest.toString())) {
                    builder.setStyle(new Notification.BigTextStyle(builder)
                            .setBigContentTitle(bigContent)
                            .setSummaryText("Notification"))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setChannelId(CHANNEL_ID)
                            .setContentIntent(pendingIntent)
                            .setGroup(type)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setSmallIcon(R.drawable.main);
                }

                if (type.equalsIgnoreCase(URLBuilder.Type.BookingRequest.toString())) {
                    builder.setStyle(new Notification.BigTextStyle(builder)
                            .setBigContentTitle(bigContent)
                            .setSummaryText("Notification"))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setChannelId(CHANNEL_ID)
                            .setContentIntent(pendingIntent)
                            .setGroup(type)
                            .setSmallIcon(R.drawable.main);
                } else {
                    builder.setStyle(new Notification.BigTextStyle(builder)
                            .setBigContentTitle(bigContent)
                            .setSummaryText("Notification"))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setChannelId(CHANNEL_ID)
//                        .setContentIntent(pendingIntent)
                            .setGroup(type)
                            .setSmallIcon(R.drawable.main);
                }


            }

        }
        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }

// Issue the notification.
        Random random = new Random();
        final int m = random.nextInt(9999 - 1000) + 1000;

        if (!type.equalsIgnoreCase("")) {

            if (type.equalsIgnoreCase(URLBuilder.Type.VideoChatRequest.toString())) {
                if (image.equalsIgnoreCase("")) {
                    mNotificationManager.notify(m, builder.build());
                } else {
                    builder.setStyle(new Notification.BigTextStyle(builder)
                            .setBigContentTitle(message)
                            .setSummaryText("Notification"))
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setGroup(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setSmallIcon(R.drawable.main);

                }
            }
            if (type.equalsIgnoreCase(URLBuilder.Type.BookingRequest.toString())) {
                if (image.equalsIgnoreCase("")) {
                    mNotificationManager.notify(m, builder.build());
                } else {
                    builder.setStyle(new Notification.BigTextStyle(builder)
                            .setBigContentTitle(message)
                            .setSummaryText("Notification"))
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setGroup(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setSmallIcon(R.drawable.main);

                }
            } else {
                if (image.equalsIgnoreCase("")) {
                    mNotificationManager.notify(m, builder.build());
                } else {
                    builder.setStyle(new Notification.BigTextStyle(builder)
                            .setBigContentTitle(message)
                            .setSummaryText("Notification"))
                            .setAutoCancel(true)
//                        .setContentIntent(pendingIntent)
                            .setGroup(message)
                            .setVibrate(new long[]{200, 200, 200, 200})
                            .setSound(defaultSound)
                            .setSmallIcon(R.drawable.main);

                }
            }

        } else {
            mNotificationManager.notify(m, notification);
        }
    }

//    private String getProviderType(ProviderInformation providerInformation) {
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

