package com.tajr.tajr.enums;

public enum ResponseCodeEnums {


    code_1200("1200"),  // order fetched successfully,

    code_1300("1300"),  // All orders handled(nothing to fetch),

    code_1407("1407"),  // Not logged in

    code_1408("1408"),  // login timeout and token expired

    code_1440("1440"),  // Invalid Token

    code_1490("1490"),  // No Clinets can be found for this account (telesales),

    code_1511("1511");  // missing token

    public String code;

    ResponseCodeEnums(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
    
    public static boolean loginIssue(String code){
        return 
                code.equals(ResponseCodeEnums.code_1407.getCode()) ||
                        code.equals(ResponseCodeEnums.code_1408.getCode()) ||
                        code.equals(ResponseCodeEnums.code_1490.getCode()) ||
                        code.equals(ResponseCodeEnums.code_1511.getCode()) ||
                        code.equals(ResponseCodeEnums.code_1440.getCode());
    }
}
