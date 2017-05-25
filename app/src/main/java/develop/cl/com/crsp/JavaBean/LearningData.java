package develop.cl.com.crsp.JavaBean;

public class LearningData {

    private int learning_dataid;
    private String userid;
    private String classify;
    private String title;
    private String detail;
    private String keyword;
    private String data_name;
    private String release_time;
    private int type;
    private String school;
    private String data_path;

    public String getData_path() {
        return data_path;
    }

    public void setData_path(String data_path) {
        this.data_path = data_path;
    }

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


    public int getLearning_dataid() {
        return learning_dataid;
    }

    public void setLearning_dataid(int learning_dataid) {
        this.learning_dataid = learning_dataid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getData_name() {
        return data_name;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }
}
