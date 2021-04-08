// aidlInterface.aidl
package SeparatePk;

// Declare any non-default types here with import statements
import SeparatePk.aidlInterface;
import SeparatePk.IRemoteServiceCallback;

interface aidlInterface {
    void trigger(IRemoteServiceCallback iRemoteServiceCallback);
    void SendSMS(String camName,String camMsg,String numbers);
    void sendSingleSms(int rsno,String message,String number);
}