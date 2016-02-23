/*
 * Copyright (C) 2013 - Cognizant Technology Solutions.
 * This file is a part of OneMobileStudio
 * Licensed under the OneMobileStudio, Cognizant Technology Solutions, 
 * Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.cognizant.com/
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package watch.oms.omswatch.toolbar.dto;

public class ToolBarConfigDTO {
	public int screenid;
	public String buttonTitle;
	public String buttonicon;
	public String taborder;
	public String nav_usid;
	public String parent_usid;
	public String styleguidename;
	public String buttonStyleName;
	public int position;
    public String blName;
    
	public boolean showBadge;
	public boolean badgeUseWhere;
    public String badgeTableName;
    public String badgeColumn;
    public String badgeWhereColumn;
    public String badgeWhereConst;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ToolBarConfig [screenid=");
		builder.append(screenid);
		builder.append(", ");
		if (buttonTitle != null) {
			builder.append("buttonTitle=");
			builder.append(buttonTitle);
			builder.append(", ");
		}
		if (buttonicon != null) {
			builder.append("buttonicon=");
			builder.append(buttonicon);
			builder.append(", ");
		}
		if (taborder != null) {
			builder.append("taborder=");
			builder.append(taborder);
			builder.append(", ");
		}
		if (nav_usid != null) {
			builder.append("nav_usid=");
			builder.append(nav_usid);
			builder.append(", ");
		}
		if (parent_usid != null) {
			builder.append("parent_usid=");
			builder.append(parent_usid);
			builder.append(", ");
		}
		if (styleguidename != null) {
			builder.append("styleguidename=");
			builder.append(styleguidename);
			builder.append(", ");
		}
		if (buttonStyleName != null) {
			builder.append("buttonStyleName=");
			builder.append(buttonStyleName);
			builder.append(", ");
		}
		if (blName != null) {
			builder.append("blName=");
			builder.append(blName);
			builder.append(", ");
		}
		builder.append("position=");
		builder.append(position);
		builder.append("]");
		return builder.toString();
	}

}
