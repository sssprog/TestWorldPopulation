package com.sssprog.test2;

import com.google.gson.annotations.SerializedName;

public class Country {
    public String country;
    @SerializedName("flag")
    public String flagUrl;
    public String population;
}
