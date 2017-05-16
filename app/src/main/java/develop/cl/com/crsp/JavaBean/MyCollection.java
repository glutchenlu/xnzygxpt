package develop.cl.com.crsp.JavaBean;


public class MyCollection {

    private int collectionid;
    private String userid;
    private String collection;
    private int collection_contentid;
    private int type;

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

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public int getCollection_contentid() {
        return collection_contentid;
    }

    public void setCollection_contentid(int collection_contentid) {
        this.collection_contentid = collection_contentid;
    }


}
