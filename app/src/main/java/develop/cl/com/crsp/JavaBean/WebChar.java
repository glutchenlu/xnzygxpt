package develop.cl.com.crsp.JavaBean;


public class WebChar {
    private int webcharid;
    private int informationid;
    private String userid;
    private String content;
    private String release_time;

    public int getWebcharid() {
        return webcharid;
    }

    public void setWebcharid(int webcharid) {
        this.webcharid = webcharid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getInformationid() {
        return informationid;
    }

    public void setInformationid(int informationid) {
        this.informationid = informationid;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
