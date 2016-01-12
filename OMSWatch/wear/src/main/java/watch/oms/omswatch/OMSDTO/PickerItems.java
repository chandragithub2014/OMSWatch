package watch.oms.omswatch.OMSDTO;

/**
 * Created by 245742 on 1/11/2016.
 */
public class PickerItems {
    public String pickerdatatablename;
    public String pickertype;
    public String pickercontent;
    public String pickerkey;
    public int pickermapping;
    public int useWhere;
    public String whereColumn;
    public String whereConstant;
    public String usId;
    public int isdependent;
    public int hasdependent;
    public String dependentpickerusid;
    public String pickerHint;
    public String pickerTransContent;

    public int isMandatory;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PickerItems [");
        if (pickerdatatablename != null) {
            builder.append("pickerdatatablename=");
            builder.append(pickerdatatablename);
            builder.append(", ");
        }

        if (pickerHint != null) {
            builder.append("pickerHint=");
            builder.append(pickerHint);
            builder.append(", ");
        }
        if (pickertype != null) {
            builder.append("pickertype=");
            builder.append(pickertype);
            builder.append(", ");
        }
        if (pickercontent != null) {
            builder.append("pickercontent=");
            builder.append(pickercontent);
            builder.append(", ");
        }
        if (pickerkey != null) {
            builder.append("pickerkey=");
            builder.append(pickerkey);
            builder.append(", ");
        }
        builder.append("pickermapping=");
        builder.append(pickermapping);
        builder.append(", useWhere=");
        builder.append(useWhere);
        builder.append(", ");
        if (whereColumn != null) {
            builder.append("whereColumn=");
            builder.append(whereColumn);
            builder.append(", ");
        }
        if (whereConstant != null) {
            builder.append("whereConstant=");
            builder.append(whereConstant);
        }

        builder.append(",isdependent=");
        builder.append(isdependent);
        builder.append(", hasdependent=");
        builder.append(hasdependent);
        if (dependentpickerusid != null) {
            builder.append(", dependentpickerusid=");
            builder.append(dependentpickerusid);
        }

        if (pickerTransContent != null) {
            builder.append(", pickerTransContent=");
            builder.append(pickerTransContent);
        }

        builder.append(", isMandatory=");
        builder.append(isMandatory);

        builder.append("]");
        return builder.toString();
    }



}
