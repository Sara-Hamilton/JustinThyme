package com.JustinThyme.justinthymer.controllers.TwilioReminder;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


public class TwillSend {

    // Setup your own account for personal SID, TOKEN, and phone number
    public static final String ACCOUNT_SID = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    public static final String  AUTH_TOKEN = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";




    public static void twill_away(String message, String number) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message twill_message = Message.creator(new PhoneNumber (number),//number to (receiving)
                new PhoneNumber("XXXXXXXXXXX"),//number from (this will be your Twillio number)



                message).create();//message from form and passed via model

        System.out.println(twill_message.getSid());
    }
}
