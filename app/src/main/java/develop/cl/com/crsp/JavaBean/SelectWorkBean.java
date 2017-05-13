package develop.cl.com.crsp.JavaBean;

/**
 * Created by Administrator on 2017/1/20.
 */

public class SelectWorkBean {

    private String[] work_title;
    private String[] work_sal;
    private String[] work_welfare;
    private String[] work_company;
    private String[] work_time;
    private String[] work_land;
    private String work_submit;

    public String getWork_submit() {
        return work_submit;
    }

    public void setWork_submit(String work_submit) {
        this.work_submit = work_submit;
    }

    public String[] getWork_company() {
        return work_company;
    }

    public void setWork_company(String[] work_company) {
        this.work_company = work_company;
    }

    public String[] getWork_land() {
        return work_land;
    }

    public void setWork_land(String[] work_land) {
        this.work_land = work_land;
    }

    public String[] getWork_sal() {
        return work_sal;
    }

    public void setWork_sal(String[] work_sal) {
        this.work_sal = work_sal;
    }

    public String[] getWork_time() {
        return work_time;
    }

    public void setWork_time(String[] work_time) {
        this.work_time = work_time;
    }

    public String[] getWork_title() {
        return work_title;
    }

    public void setWork_title(String[] work_title) {
        this.work_title = work_title;
    }

    public String[] getWork_welfare() {
        return work_welfare;
    }

    public void setWork_welfare(String[] work_welfare) {
        this.work_welfare = work_welfare;
    }
}
