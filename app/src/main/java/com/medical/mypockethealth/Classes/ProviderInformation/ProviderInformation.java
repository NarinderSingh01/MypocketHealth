package com.medical.mypockethealth.Classes.ProviderInformation;

import com.medical.mypockethealth.Classes.ClientInformation.AllergieList;
import com.medical.mypockethealth.Classes.ClientInformation.MedicationsList;
import com.medical.mypockethealth.Classes.ClientInformation.MorbiditiesList;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class ProviderInformation implements Serializable {

    public String id;
    public String firstName;
    public String surName;
    public String passwordBackup;
    public String countryCode;
    public String phoneNumber;
    public String address;
    public String city;
    public String postalCode;
    public String professionalRegistrationNumber;
    public String email;
    public String patientName;
    public String patientAge;
    public String patientPhone;
    public String department;
    public String password;
    public String workLocation;
    public String specialization;
    public String profileImage;
    public String loginType;
    public String onlineOPDStatus;
    public String ficaDocuments;
    public String registrationDocuments;
    public String consultCharges;
    public String lat;
    public String log;
    public String bio;
    public String regId;
    public String journeyInformationStatus;
    public String professionalType;
    public String deviceId;
    public String isDoctor;
    public String modeType;
    public String deviceType;
    public String experience;
    public String userId;
    public String activationStatus;
    public String profileStatus;
    public String ficaDocument;
    public String ficaDocumentName;
    public String registrationDocument;
    public String registrationDocumentName;
    public String ehrStatus;
    public String medicalAid;
    public String practiceNumberPhone;
    public String requestId;
    public String practiceNumber;
    public File digitalSignature;
    public String uploadedSignature;
    public String profileImageName;
    public String script_image;
    public String userTitle;
    public String userType;
    public List<AllergieList> allergieList;
    public List<MorbiditiesList> morbiditiesList;
    public List<MedicationsList> medicationsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPasswordBackup() {
        return passwordBackup;
    }

    public void setPasswordBackup(String passwordBackup) {
        this.passwordBackup = passwordBackup;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getProfessionalRegistrationNumber() {
        return professionalRegistrationNumber;
    }

    public void setProfessionalRegistrationNumber(String professionalRegistrationNumber) {
        this.professionalRegistrationNumber = professionalRegistrationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getOnlineOPDStatus() {
        return onlineOPDStatus;
    }

    public void setOnlineOPDStatus(String onlineOPDStatus) {
        this.onlineOPDStatus = onlineOPDStatus;
    }

    public String getFicaDocuments() {
        return ficaDocuments;
    }

    public void setFicaDocuments(String ficaDocuments) {
        this.ficaDocuments = ficaDocuments;
    }

    public String getRegistrationDocuments() {
        return registrationDocuments;
    }

    public void setRegistrationDocuments(String registrationDocuments) {
        this.registrationDocuments = registrationDocuments;
    }

    public String getConsultCharges() {
        return consultCharges;
    }

    public void setConsultCharges(String consultCharges) {
        this.consultCharges = consultCharges;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getJourneyInformationStatus() {
        return journeyInformationStatus;
    }

    public void setJourneyInformationStatus(String journeyInformationStatus) {
        this.journeyInformationStatus = journeyInformationStatus;
    }

    public String getProfessionalType() {
        return professionalType;
    }

    public void setProfessionalType(String professionalType) {
        this.professionalType = professionalType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIsDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(String isDoctor) {
        this.isDoctor = isDoctor;
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getFicaDocument() {
        return ficaDocument;
    }

    public void setFicaDocument(String ficaDocument) {
        this.ficaDocument = ficaDocument;
    }

    public String getFicaDocumentName() {
        return ficaDocumentName;
    }

    public void setFicaDocumentName(String ficaDocumentName) {
        this.ficaDocumentName = ficaDocumentName;
    }

    public String getRegistrationDocument() {
        return registrationDocument;
    }

    public void setRegistrationDocument(String registrationDocument) {
        this.registrationDocument = registrationDocument;
    }

    public String getRegistrationDocumentName() {
        return registrationDocumentName;
    }

    public void setRegistrationDocumentName(String registrationDocumentName) {
        this.registrationDocumentName = registrationDocumentName;
    }

    public String getEhrStatus() {
        return ehrStatus;
    }

    public void setEhrStatus(String ehrStatus) {
        this.ehrStatus = ehrStatus;
    }

    public String getMedicalAid() {
        return medicalAid;
    }

    public void setMedicalAid(String medicalAid) {
        this.medicalAid = medicalAid;
    }

    public String getPracticeNumberPhone() {
        return practiceNumberPhone;
    }

    public void setPracticeNumberPhone(String practiceNumberPhone) {
        this.practiceNumberPhone = practiceNumberPhone;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPracticeNumber() {
        return practiceNumber;
    }

    public void setPracticeNumber(String practiceNumber) {
        this.practiceNumber = practiceNumber;
    }

    public File getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(File digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public String getUploadedSignature() {
        return uploadedSignature;
    }

    public void setUploadedSignature(String uploadedSignature) {
        this.uploadedSignature = uploadedSignature;
    }

    public String getProfileImageName() {
        return profileImageName;
    }

    public void setProfileImageName(String profileImageName) {
        this.profileImageName = profileImageName;
    }

    public String getScript_image() {
        return script_image;
    }

    public void setScript_image(String script_image) {
        this.script_image = script_image;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<AllergieList> getAllergieList() {
        return allergieList;
    }

    public void setAllergieList(List<AllergieList> allergieList) {
        this.allergieList = allergieList;
    }

    public List<MorbiditiesList> getMorbiditiesList() {
        return morbiditiesList;
    }

    public void setMorbiditiesList(List<MorbiditiesList> morbiditiesList) {
        this.morbiditiesList = morbiditiesList;
    }

    public List<MedicationsList> getMedicationsList() {
        return medicationsList;
    }

    public void setMedicationsList(List<MedicationsList> medicationsList) {
        this.medicationsList = medicationsList;
    }
}
