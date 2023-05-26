package guru.langer.phonebookdemojavafx;


public class ContactModel {
    private String name;
    private String phone;
    private String email;

    public ContactModel(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public boolean contains(String value) {
        if (this.name.toLowerCase().contains(value.toLowerCase())) {
            return true;
        }
        if (this.phone.toLowerCase().contains(value.toLowerCase())) {
            return true;
        }
        if (this.email.toLowerCase().contains(value.toLowerCase())) {
            return true;
        }
        return false;
    }

    public String getContactAsStringWithLineBreak() {
        return "Name: " + getName() +
                ", \nPhone: " + getPhone() + ", E-Mail: " + getEmail();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
