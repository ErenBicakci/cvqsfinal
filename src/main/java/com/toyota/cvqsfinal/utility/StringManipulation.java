package com.toyota.cvqsfinal.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StringManipulation {

    /**
     * Girilen şifrenin ilk 3 elemanı hariç saklar
     *
     * @param password
     * @return Saklanmış metin döner
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
