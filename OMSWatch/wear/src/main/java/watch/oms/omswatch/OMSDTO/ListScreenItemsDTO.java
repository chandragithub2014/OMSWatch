package watch.oms.omswatch.OMSDTO;

/**
 * Created by 245742 on 12/22/2015.
 */
public class ListScreenItemsDTO {
    private String primaryText;
    private  String secondaryText;
    private String imageURL;
    private String childNavUsid;
    private int position;
    private int clickedPosition;

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getChildNavUsid() {
        return childNavUsid;
    }

    public void setChildNavUsid(String childNavUsid) {
        this.childNavUsid = childNavUsid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getClickedPosition() {
        return clickedPosition;
    }

    public void setClickedPosition(int clickedPosition) {
        this.clickedPosition = clickedPosition;
    }
}
