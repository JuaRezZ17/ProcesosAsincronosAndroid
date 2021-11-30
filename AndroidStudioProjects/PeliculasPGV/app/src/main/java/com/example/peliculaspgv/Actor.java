package com.example.peliculaspgv;

public class Actor {
    private boolean adult;
    private int gender;
    private int id;
    private String know_for_department;
    private String name;
    private String original_name;
    private int popularity;
    private String profile_path;
    private int cast_id;
    private String character;
    private String credit_id;
    private int order;

    public Actor(int id, String name, String original_name, String profile_path) {
        this.id = id;
        this.name = name;
        this.original_name = original_name;
        this.profile_path = profile_path;
    }

    public boolean getAdult() {return adult;}
    public void setAdult(boolean adult) {this.adult = adult;}

    public int getGender() {return gender;}
    public void setAdult(int gender) {this.gender = gender;}

    public int getID() {return id;}
    public void setID(int id) {this.id = id;}

    public String getKnowForDepartment() {return know_for_department;}
    public void setKnowForDepartment(String know_for_department) {this.know_for_department = know_for_department;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getOriginalName() {return original_name;}
    public void setOriginalName(String original_name) {this.original_name = original_name;}

    public int getPopularity() {return popularity;}
    public void setPopularity(int popularity) {this.popularity = popularity;}

    public String getProfilePath() {return profile_path;}
    public void setProfilePath(String profile_path) {this.profile_path = profile_path;}

    public int getCastID() {return cast_id;}
    public void setCastID(int cast_id) {this.cast_id = cast_id;}

    public String getCharacter() {return character;}
    public void setCharacter(String character) {this.character = character;}

    public String getCreditID() {return credit_id;}
    public void setCreditID(String credit_id) {this.credit_id = credit_id;}

    public int getOrder() {return order;}
    public void setOrder(int order) {this.order = order;}

}
