package model;

/**
 * This model class creates the content object
 */
public class Contact {
    private int contactId;
    private String contactName;
    private String email;

    public Contact(int contactId, String contactName, String email){
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    public Contact() {

    }

    //getters

    public int getContactId() {
        return contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getEmail() {
        return email;
    }

    //setters


    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString(){
        return (contactName);
    }
}
