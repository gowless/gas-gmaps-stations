package com.example.myapplication.helper;

public class Checker {
    Boolean checker;
    public Boolean checkInputs(String gasTitle, String gasType, String totalNum, String totalCost){
        if (gasTitle.equals("") || gasType.equals("") || totalNum.equals("") || totalCost.equals("")){
            checker = false;
        } else {
            checker = true;
        }

        return checker;
    }
}
