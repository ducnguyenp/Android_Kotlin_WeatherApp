package com.example.weatherapp

data class ResponseData(
    val message: String,
    val user_id: String,
    val name: String,
    val email: String,
    val mobile: String,
    val profile_details: ProfileDetails,
    val data_list: List<DataList>
)

data class ProfileDetails(val is_profile_completed: Boolean, val rating: Double)
data class DataList(val id: Int, val value: String)
