/**
 * 
 */
package watch.oms.omswatch.actioncenter.blexecutor.dto;

import java.util.HashMap;

/**
 * @author 245742
 *
 */
public class BLStringModifierDTO {

	public  HashMap<String,String> stringModifierHash;
	public HashMap<Integer,BLStringModifierOperandDTO> stringModifierOperandHash; 
	public HashMap<Integer,String> stringModifierOperatorHash;
	  
}
