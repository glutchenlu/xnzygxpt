package develop.cl.com.crsp.JavaBean;


public class PhotoDirInfo {
    private String dirId;
    private String dirName;
    private String firstPicPath;
    private boolean isUserOtherPicSoft;
    private int picCount = 0;

    public PhotoDirInfo() {
    }

    public PhotoDirInfo(String paramString, boolean paramBoolean) {
        this.dirName = paramString;
        this.isUserOtherPicSoft = paramBoolean;
    }

    public String getDirId() {
        return this.dirId;
    }

    public String getDirName() {
        return this.dirName;
    }

    public String getFirstPicPath() {
        return this.firstPicPath;
    }

    public int getPicCount() {
        return this.picCount;
    }

    public boolean isUserOtherPicSoft() {
        return this.isUserOtherPicSoft;
    }

    public void setDirId(String paramString) {
        this.dirId = paramString;
    }

    public void setDirName(String paramString) {
        this.dirName = paramString;
    }

    public void setFirstPicPath(String paramString) {
        this.firstPicPath = paramString;
    }

    public void setPicCount(int paramInt) {
        this.picCount = paramInt;
    }

    public void setUserOtherPicSoft(boolean paramBoolean) {
        this.isUserOtherPicSoft = paramBoolean;
    }
}
