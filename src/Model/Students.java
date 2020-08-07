package Model;

public class Students {


    private String idST, nameST, emailST, phoneNumberST, addressST, imageST;
    private boolean sexST;

    public Students() {
    }

    public Students(String idST, String nameST, String emailST, String phoneNumberST, boolean sexST, String addressST, String imageST) {
        this.idST = idST;
        this.nameST = nameST;
        this.emailST = emailST;
        this.phoneNumberST = phoneNumberST;
        this.addressST = addressST;
        this.imageST = imageST;
        this.sexST = sexST;
    }

    public String getIdST() {
        return idST;
    }

    public void setIdST(String idST) {
        this.idST = idST;
    }

    public String getNameST() {
        return nameST;
    }

    public void setNameST(String nameST) {
        this.nameST = nameST;
    }

    public String getEmailST() {
        return emailST;
    }

    public void setEmailST(String emailST) {
        this.emailST = emailST;
    }

    public String getPhoneNumberST() {
        return phoneNumberST;
    }

    public void setPhoneNumberST(String phoneNumberST) {
        this.phoneNumberST = phoneNumberST;
    }

    public String getAddressST() {
        return addressST;
    }

    public void setAddressST(String addressST) {
        this.addressST = addressST;
    }

    public String getImageST() {
        return imageST;
    }

    public void setImageST(String imageST) {
        this.imageST = imageST;
    }

    public boolean isSexST() {
        return sexST;
    }

    public void setSexST(boolean sexST) {
        this.sexST = sexST;
    }

    @Override
    public String toString() {
        return "Students{" + "idST=" + idST + ", nameST=" + nameST + ", emailST=" + emailST + ", phoneNumberST=" + phoneNumberST + ", addressST=" + addressST + ", imageST=" + imageST + ", sexST=" + sexST + '}';
    }

}
