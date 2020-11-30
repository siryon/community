package community2.demo.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String account_id;
    private String  token;
    private long gmt_create;
    private  long gmt_modified;
    private String bio;
    private String avatar_url;
    }
