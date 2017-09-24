package com.antoniorosario.musicsearch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MusixMatchMessage {

    @SerializedName("body")
    @Expose
    private MusixMatchBody musixMatchBody;

    public MusixMatchBody getMusixMatchBody() {
        return musixMatchBody;
    }

    public void setMusixMatchBody(MusixMatchBody musixMatchBody) {
        this.musixMatchBody = musixMatchBody;
    }

}