package com.playdate.contactlist;

public class Model_contactlist implements Comparable<Model_contactlist>
{    
    private String name;
    
    private String phonenumber;
    
    private String email;
    
    public Model_contactlist() {
        // TODO Auto-generated constructor stub
    }
    
    public Model_contactlist(String name, String phonenumber, String email) {
        super();
        this.name = name;
        this.phonenumber = phonenumber;
        this.email = email;
       
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getphonenumber() {
        return phonenumber;
    }

    
    public void setphonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    
    @Override
    public int compareTo(Model_contactlist book) {
        return this.getName().compareTo(book.getName());
    }
}
