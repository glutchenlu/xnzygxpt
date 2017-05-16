package develop.cl.com.crsp.JavaBean;

public class Courier {

    private int courierid;
    private String userid;
    private String receive_time;
    private String merchant;
    private String detail;
    private String prive;
    private String release_time;
    private int type;
    private String school;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCourierid() {
        return courierid;
    }

    public void setCourierid(int courierid) {
        this.courierid = courierid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrive() {
        return prive;
    }

    public void setPrive(String prive) {
        this.prive = prive;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }

}
