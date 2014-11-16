package com.plutolabs.kointrak.api;

import java.util.ArrayList;

/**
 * Holds the information provided by a get_addresses API call. Including an ArrayList of addresses (data).
 *
 * Created by chipwasson on 11/15/14.
 */
public class AddressImport {

    private Boolean result;
    private Boolean auth;
    private String msg;
    ArrayList<String> data;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<String> getData() {
        return data;
    }

    /**
     * Get an ArrayList of the addresses entered under the short code.
     * @return ArrayList<String> addresses
     */
    public ArrayList<String> getAddresses(){return data;}

    public void setData(ArrayList<String> data) {
        this.data = data;
    }



    public AddressImport(){}

    @Override
    public String toString() {
        return "AddressImport{" +
                "result=" + result +
                ", auth=" + auth +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
