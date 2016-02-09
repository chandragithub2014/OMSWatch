/**
 * 
 */
package watch.oms.omswatch.actioncenter.blexecutor.dto;

import java.util.List;

/**
 * @author 245742
 *
 */
public class BLFileDownloadDTO {
	public String tableName;
	public String fileColName;
	public String urlColName;
	public String fileType;
	public String updateColumn;
	public String loadingText;

	public List<BLColumnDTO> getColList;
		
	
	public String getFileColName() {
		return fileColName;
	}
	public void setFileColName(String fileColName) {
		this.fileColName = fileColName;
	}
	public String getUrlColName() {
		return urlColName;
	}
	public void setUrlColName(String urlColName) {
		this.urlColName = urlColName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getUpdateColumn() {
		return updateColumn;
	}
	public void setUpdateColumn(String updateColumn) {
		this.updateColumn = updateColumn;
	}

	public List<BLColumnDTO> getGetColList() {
		return getColList;
	}
	public void setGetColList(List<BLColumnDTO> getColList) {
		this.getColList = getColList;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getLoadingText() {
		return loadingText;
	}
	public void setLoadingText(String loadingText) {
		this.loadingText = loadingText;
	}
	  
}
