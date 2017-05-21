package develop.cl.com.crsp.JavaBean;


public class MyCollection {

    private int collectionid;
    private String userid;
    private String collection_time;
    private int serviceid;
    private int type;
    private String typeName;
    private String serviceTitle;

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getServiceid() {
        return serviceid;
    }

    public void setServiceid(int serviceid) {
        this.serviceid = serviceid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getCollectionid() {
        return collectionid;
    }

    public void setCollectionid(int collectionid) {
        this.collectionid = collectionid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCollection_time() {
        return collection_time;
    }

    public void setCollection_time(String collection_time) {
        this.collection_time = collection_time;
    }
}
