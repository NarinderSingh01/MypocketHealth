package com.medical.mypockethealth.Classes.ClientInformation;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientInformation implements Serializable {

    public String id;
    public String username;
    public String medicalNumber;
    public String phone;
    public String email;
    public String profileImage;
    public String socialId;
    public String occupation;
    public String password;
    public String image;
    public String deviceType;
    public String deviceId;
    public String regId;
    public String age;
    public String created;
    public String updated;
    public String passwordBackup;
    public String loginType;
    public String modeType;
    public String orderId;
    public String providerId;
    public String userId;
    public String patientName;
    public String patientAge;
    public String patientProblem;
    public String date;
    public String bookingDate;
    public String slotTime;
    public String status;
    public String firstName;
    public String surName;
    public String medicalAid;
    public String idNumber;
    public String phoneNumber;
    public String isUser;
    public String isAdmin;
    public String isDoctor;
    public String created_at;
    public String updated_at;
    public String journeyInformationStatus="0";
    public Object deleted_at;
    public String allergies;
    public String sessionType;
    public String morbidity;
    public String aidNumber;
    public String requestId;
    public String countryCode;
    public String script_image;
    public String medicalAidStatus;
    public byte[] selectedImage;
    public boolean scriptImageStatus;
    public List<File> imageList=new ArrayList<>();
    public List<String> medicineList=new ArrayList<>();
    public List<String> MorbidityList=new ArrayList<>();
    public List<String> allergiesList=new ArrayList<>();
    public List<AllergieList> allergieList;
    public List<MedicationsList> medicationsList;
    public List<MorbiditiesList> morbiditiesList;
    public String bookingStatus;
    public String prescriptionImage;
    public String myStateKey;
    public String ehrStatus;
    public String profileStatus;
    public String activationStatus;
    public String address;
    public String city;
    public String scriptImage;
    public String postalCode;


    public String getMedicalAidStatus() {
        return medicalAidStatus;
    }

    public void setMedicalAidStatus(String medicalAidStatus) {
        this.medicalAidStatus = medicalAidStatus;
    }

    public String getScript_image() {
        return script_image;
    }

    public void setScript_image(String script_image) {
        this.script_image = script_image;
    }

    public List<AllergieList> getAllergieList() {
        return allergieList;
    }

    public void setAllergieList(List<AllergieList> allergieList) {
        this.allergieList = allergieList;
    }

    public List<MedicationsList> getMedicationsList() {
        return medicationsList;
    }

    public void setMedicationsList(List<MedicationsList> medicationsList) {
        this.medicationsList = medicationsList;
    }

    public List<MorbiditiesList> getMorbiditiesList() {
        return morbiditiesList;
    }

    public void setMorbiditiesList(List<MorbiditiesList> morbiditiesList) {
        this.morbiditiesList = morbiditiesList;
    }

    public String getScriptImage() {
        return scriptImage;
    }

    public void setScriptImage(String scriptImage) {
        this.scriptImage = scriptImage;
    }

    public boolean isScriptImageStatus() {
        return scriptImageStatus;
    }

    public void setScriptImageStatus(boolean scriptImageStatus) {
        this.scriptImageStatus = scriptImageStatus;
    }

    public byte[] getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(byte[] selectedImage) {
        this.selectedImage = selectedImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<String> getAllergiesList() {
        return allergiesList;
    }

    public void setAllergiesList(List<String> allergiesList) {
        this.allergiesList = allergiesList;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getEhrStatus() {
        return ehrStatus;
    }

    public void setEhrStatus(String ehrStatus) {
        this.ehrStatus = ehrStatus;
    }

    public String getAidNumber() {
        return aidNumber;
    }

    public void setAidNumber(String aidNumber) {
        this.aidNumber = aidNumber;
    }

    public String getMyStateKey() {
        return myStateKey;
    }

    public void setMyStateKey(String myStateKey) {
        this.myStateKey = myStateKey;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getPrescriptionImage() {
        return prescriptionImage;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }

    public List<String> getMorbidityList() {
        return MorbidityList;
    }

    public void setMorbidityList(List<String> morbidityList) {
        MorbidityList = morbidityList;
    }

    public String getMorbidity() {
        return morbidity;
    }

    public void setMorbidity(String morbidity) {
        this.morbidity = morbidity;
    }



    public List<File> getImageList() {
        return imageList;
    }

    public void setImageList(List<File> imageList) {
        this.imageList = imageList;
    }


    public List<String> getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(List<String> medicineList) {
        this.medicineList = medicineList;
    }

    public String getJourneyInformationStatus() {
        return journeyInformationStatus;
    }

    public void setJourneyInformationStatus(String journeyInformationStatus) {
        this.journeyInformationStatus = journeyInformationStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getMedicalAid() {
        return medicalAid;
    }

    public void setMedicalAid(String medicalAid) {
        this.medicalAid = medicalAid;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(String isDoctor) {
        this.isDoctor = isDoctor;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Object deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientProblem() {
        return patientProblem;
    }

    public void setPatientProblem(String patientProblem) {
        this.patientProblem = patientProblem;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getPasswordBackup() {
        return passwordBackup;
    }

    public void setPasswordBackup(String passwordBackup) {
        this.passwordBackup = passwordBackup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMedicalNumber() {
        return medicalNumber;
    }

    public void setMedicalNumber(String medicalNumber) {
        this.medicalNumber = medicalNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
