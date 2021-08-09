package com.alcanzar.cynapse.model;

public class GetTargetDepartmentModel {

    private String department_id;
    private String department_name;
    private String medical_profile_id;
    private String medical_profile_name;
    private boolean status;
    private String type;

    public GetTargetDepartmentModel() {
    }


    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getMedical_profile_id() {
        return medical_profile_id;
    }

    public void setMedical_profile_id(String medical_profile_id) {
        this.medical_profile_id = medical_profile_id;
    }

    public String getMedical_profile_name() {
        return medical_profile_name;
    }

    public void setMedical_profile_name(String medical_profile_name) {
        this.medical_profile_name = medical_profile_name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}



