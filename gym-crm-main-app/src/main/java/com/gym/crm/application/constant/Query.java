package com.gym.crm.application.constant;

public class Query {

    public static final String SELECT_WHERE_USERNAME_LIKE = "SELECT username FROM User WHERE username LIKE :username";
    public static final String SELECT_ALL_WHERE_USERNAME_LIKE = "SELECT u FROM User u WHERE username LIKE :username";
    public static final String SELECT_BY_USERNAME = "SELECT t FROM %s t WHERE t.user.username = :username";
    public static final String SELECT_WHERE_TRAINING_TYPE_LIKE = "SELECT t FROM TrainingType t WHERE trainingTypeName LIKE :username";
}
