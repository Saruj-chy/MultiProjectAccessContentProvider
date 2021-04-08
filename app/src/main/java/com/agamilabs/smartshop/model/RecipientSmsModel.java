package com.agamilabs.smartshop.model;

public class RecipientSmsModel {
    private int RSNO ;
    private int SMSID ;
    private String RECIPIENTSNUMBER ;
    private String MESSAGE ;
    private int MESSAGESTATUS ;
    private String APITYPE ;
    private long ENTRYDATETIME;
    private String ASSIGNEDCLIENT ;
    private long ASSIGNDATETIME ;
    private long COMPLETEDATETIME ;
    private int RETRYCOUNT ;


    public RecipientSmsModel(int RSNO, int SMSID, String RECIPIENTSNUMBER, String MESSAGE, int MESSAGESTATUS, String APITYPE, long ENTRYDATETIME, String ASSIGNEDCLIENT, long ASSIGNDATETIME, long COMPLETEDATETIME, int RETRYCOUNT) {
        this.RSNO = RSNO;
        this.SMSID = SMSID;
        this.RECIPIENTSNUMBER = RECIPIENTSNUMBER;
        this.MESSAGE = MESSAGE;
        this.MESSAGESTATUS = MESSAGESTATUS;
        this.APITYPE = APITYPE;
        this.ENTRYDATETIME = ENTRYDATETIME;
        this.ASSIGNEDCLIENT = ASSIGNEDCLIENT;
        this.ASSIGNDATETIME = ASSIGNDATETIME;
        this.COMPLETEDATETIME = COMPLETEDATETIME;
        this.RETRYCOUNT = RETRYCOUNT;
    }

    public int getRSNO() {
        return RSNO;
    }

    public void setRSNO(int RSNO) {
        this.RSNO = RSNO;
    }

    public int getSMSID() {
        return SMSID;
    }

    public void setSMSID(int SMSID) {
        this.SMSID = SMSID;
    }

    public String getRECIPIENTSNUMBER() {
        return RECIPIENTSNUMBER;
    }

    public void setRECIPIENTSNUMBER(String RECIPIENTSNUMBER) {
        this.RECIPIENTSNUMBER = RECIPIENTSNUMBER;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public int getMESSAGESTATUS() {
        return MESSAGESTATUS;
    }

    public void setMESSAGESTATUS(int MESSAGESTATUS) {
        this.MESSAGESTATUS = MESSAGESTATUS;
    }

    public String getAPITYPE() {
        return APITYPE;
    }

    public void setAPITYPE(String APITYPE) {
        this.APITYPE = APITYPE;
    }

    public long getENTRYDATETIME() {
        return ENTRYDATETIME;
    }

    public void setENTRYDATETIME(long ENTRYDATETIME) {
        this.ENTRYDATETIME = ENTRYDATETIME;
    }

    public String getASSIGNEDCLIENT() {
        return ASSIGNEDCLIENT;
    }

    public void setASSIGNEDCLIENT(String ASSIGNEDCLIENT) {
        this.ASSIGNEDCLIENT = ASSIGNEDCLIENT;
    }

    public long getASSIGNDATETIME() {
        return ASSIGNDATETIME;
    }

    public void setASSIGNDATETIME(long ASSIGNDATETIME) {
        this.ASSIGNDATETIME = ASSIGNDATETIME;
    }

    public long getCOMPLETEDATETIME() {
        return COMPLETEDATETIME;
    }

    public void setCOMPLETEDATETIME(long COMPLETEDATETIME) {
        this.COMPLETEDATETIME = COMPLETEDATETIME;
    }

    public int getRETRYCOUNT() {
        return RETRYCOUNT;
    }

    public void setRETRYCOUNT(int RETRYCOUNT) {
        this.RETRYCOUNT = RETRYCOUNT;
    }
}
