/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Maniek
 */
@XmlRootElement
public class ErrorMessage {
    
    private String message;
    private int errorCode;
    private String message2;
    
    public ErrorMessage(){
        
    }
    
    public ErrorMessage(String message, int errorCode, String message2){
        this.message = message;
        this.errorCode = errorCode;
        this.message2 = message2;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the message2
     */
    public String getMessage2() {
        return message2;
    }

    /**
     * @param message2 the message2 to set
     */
    public void setMessage2(String message2) {
        this.message2 = message2;
    }
}
