package com.mcc.fshopper.listener;

import com.mcc.fshopper.model.UserModel;

/**
 * Created by Ashiq on 5/30/16.
 */
public interface RegistrationListener {
     void onValidationComplete(UserModel userModel);
}
