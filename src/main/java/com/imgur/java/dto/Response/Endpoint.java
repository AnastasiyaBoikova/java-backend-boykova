package com.imgur.java.dto.Response;

public class Endpoint {

    public static final String GET_ACCOUNT_REQUEST ="/account/{username}";
    public static final String GET_IMAGE_REQUEST ="/image/{imageHash}";
    public static final String POST_IMAGE_REQUEST ="/image/";
    public static final String POST_IMAGE_FAVORITE ="/image/{imageHash}";
    public static final String DELETE_IMAGE_HASH ="/image/{imageHash}";
    public static final String DELETE_IMAGE_REQUEST ="/image/{deleteHash}";

}
