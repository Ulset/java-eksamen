package no.kristiania;

import javax.management.Query;
import java.util.HashMap;

public class QueryString {
    private HashMap<String, String> headerValues = new HashMap<>();
    private String target;

    //The QueryString allows us to check what values a user has used as an URL and respond accordingly. ? is usually a reference to the resource they wish to access.
    public QueryString(String queryStr) {
        //Takes a HTTP request and splits out the headers
        int questionPosIndex = queryStr.indexOf("?");
        target = questionPosIndex != -1 ? queryStr.substring(0, questionPosIndex) : queryStr;
        String headerString = questionPosIndex != -1 ? queryStr.substring(questionPosIndex + 1).trim() : "";

        //As long as the headerString is not empty
        if (!headerString.isEmpty()) {
            //Split the two values between the &
            String[] headSplit = headerString.split("&");

            //And separate the key and value, and store them in our hashmap.
            for (String headEl : headSplit) {
                String[] headElSplit = headEl.split("=");
                if (headElSplit.length == 2) {
                    String header = headEl.split("=")[0];
                    String value = headEl.split("=")[1];
                    headerValues.put(header, value);
                }
            }
        }
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getHeader(String header) {
        return headerValues.get(header);
    }

    public String getTarget() {
        return target;
    }
}
