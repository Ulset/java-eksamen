package no.kristiania.server;

import java.util.Date;

public class HomemadeCookie {

    private String cookieName;
    private String cookieValue;
    private Date cookieExpiry = new Date();

    //Cookie uptime

    public HomemadeCookie(String cookieName) {
        this.cookieName = cookieName;
        this.cookieValue = getRandomString();
    }

    //Getters for cookiename, cookievalue, and cookie length
    public String getCookieName() {
        return cookieName;
    }

    public String getCookieValue() {
        return cookieValue;
    }


    //This retrieves our random string and is set when we call the constructor
    private String getRandomString() {
        //Hardcoding the string length, even though it can be passed as a parameter to our method
        int stringLength = 12;
        String randomOptions = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < stringLength; i++) {
            int randomNum = (int) (randomOptions.length() * Math.random());

            stringBuilder.append(randomOptions.charAt(randomNum));
        }
        return stringBuilder.toString();
    }
}