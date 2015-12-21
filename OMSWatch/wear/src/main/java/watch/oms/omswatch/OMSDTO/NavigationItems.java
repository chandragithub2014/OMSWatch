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
package watch.oms.omswatch.OMSDTO;

public class NavigationItems {

	public int position;
	public int screenorder;
	public String screentype;
	public String parent;
	public int parent_id;
	public String uniqueId;
	public int appId;
	public String screenName;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NavigationItems [position=");
		builder.append(position);
		builder.append(", screenorder=");
		builder.append(screenorder);
		builder.append(", ");
		if (screentype != null) {
			builder.append("screentype=");
			builder.append(screentype);
			builder.append(", ");
		}
		if (screenName != null) {
			builder.append("screenName=");
			builder.append(screenName);
			builder.append(", ");
		}
		if (parent != null) {
			builder.append("parent=");
			builder.append(parent);
			builder.append(", ");
		}
		builder.append("parent_id=");
		builder.append(parent_id);
		builder.append(", ");
		if (uniqueId != null) {
			builder.append("uniqueId=");
			builder.append(uniqueId);
			builder.append(", ");
		}
		builder.append("appId=");
		builder.append(appId);
		builder.append("]");
		return builder.toString();
	}
	
	
}
