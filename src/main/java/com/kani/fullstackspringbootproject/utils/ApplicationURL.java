package com.kani.fullstackspringbootproject.utils;

import jakarta.servlet.http.HttpServletRequest;

public class ApplicationURL {

    public static String getApplicationUrl(HttpServletRequest request){
        String applicationUrl = request.getRequestURL().toString();
        return applicationUrl.replace(request.getServletPath(), "");
    }
}
