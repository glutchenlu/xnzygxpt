package develop.cl.com.crsp.JavaBean;

public class XUser {

    private String userid;
    private int datingid;
    private String password;
    private int basicid;
    private int settingid;
    private int status;
    private String crtime;

    public String getCrtime() {
        return crtime;
    }

    public void setCrtime(String crtime) {
        this.crtime = crtime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getDatingid() {
        return datingid;
    }

    public void setDatingid(int datingid) {
        this.datingid = datingid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBasicid() {
        return basicid;
    }

    public void setBasicid(int basicid) {
        this.basicid = basicid;
    }

    public int getSettingid() {
        return settingid;
    }

    public void setSettingid(int settingid) {
        this.settingid = settingid;
    }

}
