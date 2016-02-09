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
package watch.oms.omswatch.actioncenter.blexecutor.dto;

import java.util.Hashtable;
import java.util.List;

public class BLIUDTO {
	public String destinationTableName;
	public String destinationPrimaryKey;
	public String destinationPrimaryKeyType;
	public String destinationSchemaName;
	public String sourceSchemaName;
	public String sourceTableName;
	public List<BLColumnDTO> destinationColList;
	public List<BLColumnDTO> sourceColList;
	public String sourcePrimaryKey;
	public Hashtable<String,String> sourceColumnnameValue;
	public boolean isForceUpdate;
	public String sourceWhere;
	public String destinationWhere;

}
