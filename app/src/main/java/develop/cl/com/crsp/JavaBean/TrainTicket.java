package develop.cl.com.crsp.JavaBean;

public class TrainTicket {

    private int train_ticketid;
    private String userid;
    private String receive_time;
    private String station;
    private String detail;
    private String price;
    private String release_time;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getTrain_ticketid() {
        return train_ticketid;
    }

    public void setTrain_ticketid(int train_ticketid) {
        this.train_ticketid = train_ticketid;
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

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }


}
