package watch.oms.omswatch.OMSDTO;

/**
 * Created by CHANDRASAIMOHAN on 12/26/2015.
 */
public class LoadScreenDTO {
    private boolean isMain;
    private String filterColumnName;
    private String filterColumnVal;
    private String UIHeadingTitle;
    private int customScreenContainerID;

    public boolean isMain() {
        return isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public int getCustomScreenContainerID() {
        return customScreenContainerID;
    }

    public void setCustomScreenContainerID(int customScreenContainerID) {
        this.customScreenContainerID = customScreenContainerID;
    }

    public String getUIHeadingTitle() {
        return UIHeadingTitle;
    }

    public void setUIHeadingTitle(String UIHeadingTitle) {
        this.UIHeadingTitle = UIHeadingTitle;
    }

    public String getFilterColumnVal() {
        return filterColumnVal;
    }

    public void setFilterColumnVal(String filterColumnVal) {
        this.filterColumnVal = filterColumnVal;
    }

    public String getFilterColumnName() {
        return filterColumnName;
    }

    public void setFilterColumnName(String filterColumnName) {
        this.filterColumnName = filterColumnName;
    }
}
