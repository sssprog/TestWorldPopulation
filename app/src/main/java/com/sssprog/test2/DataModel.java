package com.sssprog.test2;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataModel {
    @SerializedName("worldpopulation")
    public ArrayList<Country> countries;
}
