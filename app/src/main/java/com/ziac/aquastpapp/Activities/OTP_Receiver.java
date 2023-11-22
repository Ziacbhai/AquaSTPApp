package com.ziac.aquastpapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.chaos.view.PinView;

public class OTP_Receiver extends BroadcastReceiver {

    private static PinView pinView;

        public void setPinView(PinView pinView) {
            OTP_Receiver.pinView = pinView;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            for (SmsMessage smsMessage : smsMessages) {
                String message = smsMessage.getMessageBody();

                // Extract OTP from the message (assuming it's a 6-digit OTP)
                String otp = extractOtp(message);

                // Set the OTP in the PinView
                setOtpInPinView(otp);
            }
        }

        private String extractOtp(String message) {
            // Implement your logic to extract the OTP from the message
            // This might involve regular expressions or other parsing methods
            // For simplicity, let's assume the OTP is a 6-digit number
            return message.replaceAll("[^0-9]", "");
        }

        private void setOtpInPinView(String otp) {
            if (pinView != null) {
                pinView.setText(otp);
            }
        }
    }
