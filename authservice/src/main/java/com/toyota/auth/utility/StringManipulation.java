package com.toyota.auth.utility;

import org.springframework.stereotype.Service;

@Service
public class StringManipulation {

    /**
     * Password hide
     *
     * @param password - String
     * @return hiddenPassword - String
     */
    public String passwordHide(String password){
        String hiddenPassword = "";
        for (int i = 0;i < password.length();i++){
            if (i < 3){
                hiddenPassword += password.substring(i,i+1);
            }
            else {
                hiddenPassword += "*";
            }
        }
        return hiddenPassword;
    }



}
