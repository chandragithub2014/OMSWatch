/*
 * Copyright (C) 2013 - Cognizant Technology Solutions.
 *
 * This file is a part of OneMobileStudio
 *
 * Licensed under the OneMobileStudio, Cognizant Technology Solutions,
 * Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.cognizant.com/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package watch.oms.omswatch.constants;

/**
 * This is an enumeration class to represent the default values for integers
 * which can be referred as constants.
 */

public enum OMSDefaultValues {

	/**
	 * Default values for integer constants
	 */
	MIN_INDEX_INT(0), NONE_DEF_CNT(-1), DEF_CNT(1), PROXY_PORT(6050), TWO_VALUE(
			2),

	/**
	 * HTTP Constant Values
	 */

	HTTPS_PORT(443), HTTP_PORT(80);

	/**
	 * A variable which holds the value of the message for <code>Integer</code>
	 */
	private int intValue;

	/**
	 * An <code>int</code> argument constructor to assign the <code>int</code>
	 * value to the message
	 * 
	 * @param intValue
	 */
	private OMSDefaultValues(int intValue) {
		this.intValue = intValue;
	}

	/**
	 * A method to return the <code>int</code> value of the message
	 * 
	 * @return <code>int</code> value of the message
	 */
	public int getValue() {
		return intValue;
	}
}