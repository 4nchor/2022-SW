package com.libienz.se_2022_closet.startApp_1.util;

import android.widget.EditText;
import android.widget.TextView;

public class InputCheckUtility {

    private InputCheckUtility() {}

    public static boolean isEditTextEmpty(EditText edt) {
        if (edt.getText().toString().equals("")) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void emailEmptyCheckAndNotify(EditText edt, TextView tv, String message) {

    }
}
