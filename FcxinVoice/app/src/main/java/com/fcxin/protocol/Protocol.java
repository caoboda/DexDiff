/**
  ******************************************************************************
  * @file    Protocol.java
  * @author  chaoqun.wu
  * @version V1.0.0
  * @date    28-Nov-2016
  * @brief   This file provides the data communication bridge.
  ******************************************************************************
  * @attention
  *
  * THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS
  * WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE
  * TIME. AS A RESULT, FCX SHALL NOT BE HELD LIABLE FOR ANY
  * DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS ARISING
  * FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS OF THE
  * CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.
  *
  * <h2><center>&copy; COPYRIGHT 2016 FCX</center></h2>
  ******************************************************************************
  */

package com.fcxin.protocol;

import com.fcxin.voice.MainActivity;

public class Protocol {

	public static final byte PROTOCOL_FRAME_HEADR = (byte) 0xC7; // Data request frame head
	public static final byte PROTOCOL_FRAME_HEADA =  0x38; // Data answer frame head
	public static final byte PROTOCOL_FRAME_TYPE = 0x01; // Frame data type

	public static final byte PROTOCOL_CONTROL_COMMAND01 = 0x01; // connect test command
	public static final byte PROTOCOL_CONTROL_COMMAND02 = 0x02; // Read device hardware version command
	public static final byte PROTOCOL_CONTROL_COMMAND03 = 0x03; // Read device software version command
	public static final byte PROTOCOL_CONTROL_COMMAND04 = 0x04; // Read bluetooth device name command
	public static final byte PROTOCOL_CONTROL_COMMAND05 = 0x05; // Write bluetooth device name command
	public static final byte PROTOCOL_CONTROL_COMMAND06 = 0x06; // Read bluetooth device passwd command
	public static final byte PROTOCOL_CONTROL_COMMAND07 = 0x07; // Write bluetooth device passwd command
	public static final byte PROTOCOL_CONTROL_COMMAND08 = 0x08; // Recoverable trashcan control command
	public static final byte PROTOCOL_CONTROL_COMMAND09 = 0x09; // Kitchen trashcan control command
	public static final byte PROTOCOL_CONTROL_COMMAND0A = 0x0A; // Hazardous trashcan control command
	public static final byte PROTOCOL_CONTROL_COMMAND0B = 0x0B; // Other trashcan control command
	public static final byte PROTOCOL_CONTROL_COMMAND0C = 0x0C; // Infrared device control command
	public static final byte PROTOCOL_CONTROL_COMMAND0D = 0x0D; // Recoverable trashcan feedback command
	public static final byte PROTOCOL_CONTROL_COMMAND0E = 0x0E; // Kitchen trashcan feedback command
	public static final byte PROTOCOL_CONTROL_COMMAND0F = 0x0F; // Hazardous trashcan feedback command
	public static final byte PROTOCOL_CONTROL_COMMAND10 = 0x10; // Other trashcan feedback command
	public static final byte PROTOCOL_CONTROL_COMMAND11 = 0x11; // Infrared device feedback command
	public static final byte PROTOCOL_CONTROL_COMMAND12 = 0x12; // Read device clock command
	public static final byte PROTOCOL_CONTROL_COMMAND13 = 0x13; // Write device clock command
	public static final byte PROTOCOL_CONTROL_COMMAND14 = 0x14; // Device shutdown command
	public static final byte PROTOCOL_CONTROL_COMMAND15 = 0x15; // Device startup command
	public static final byte PROTOCOL_CONTROL_COMMAND16 = 0x16; // Device restart command
	public static final byte PROTOCOL_CONTROL_COMMAND17 = 0x17; // Device restore command
	public static final byte PROTOCOL_CONTROL_COMMAND18 = 0x18; // Read all log count command
	public static final byte PROTOCOL_CONTROL_COMMAND19 = 0x19; // Read log detail message command

	public static final byte PROTOCOL_FRAME_END1 = (byte) 0x0D; // Data frame end data 1
	public static final byte PROTOCOL_FRAME_END2 = (byte) 0x0A; // Data frame end data 2

	public static final byte PROTOCOL_FRAME_MIN_LENGTH = 7; // minimum data frame length
	
	private static MainActivity mainActivity=new MainActivity();
		
	/**
	  * @brief  Calculate protocol sum16 value.
	  * @param  buf: the buffer data pointer
	  * @param  len: the buffer data length
	  * @retval sum16 value.
	  */
	public static short Sum16_Check(byte[] buf, byte len)
	{
	  short sum16=0;  
	  for (byte i = 0; i < len; i++)
	  {
	    sum16 += buf[i]&0xFF;
	  }
	  return sum16;
	}

	/**
	 * @brief Unpack receive data.
	 * @param buf:the buffer data pointer
	 * @param len:the buffer data length
	 * @retval Unpack data success flag. true:Unpack data is OK. false:Unpack
	 *         data is ERROR.
	 */
	public static boolean Protocol_DataUnpack(byte[] buf, byte len) {
		short temp, sum_value;

		// Judge data length
		if (len < PROTOCOL_FRAME_MIN_LENGTH) {
			return false;
		}

		// judge frame data answer head
		if (buf[0] != PROTOCOL_FRAME_HEADA) {
			return false;
		}

		// Judge data length
		if (buf[1] < PROTOCOL_FRAME_MIN_LENGTH) {
			return false;
		}

		// judge frame data type
		if (buf[2] != PROTOCOL_FRAME_TYPE) {
			return false;
		}

		// check sum16 value
		sum_value = Sum16_Check(buf, (byte) (buf[1] - 2));
		temp = (short) (((buf[buf[1] - 2] << 8) & 0xFF00) | (buf[buf[1] - 1]&0xFF));
		if (sum_value != temp) {
			return false;
		}

		// judge data command type
		switch (buf[3]) {
		case PROTOCOL_CONTROL_COMMAND01: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND02: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND03: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND04: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND05: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND06: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND07: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND08: {
			Command.Trashcan_RecoverableControlAnswerCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND09: {
			Command.Trashcan_KitchenControlAnswerCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND0A: {
			Command.Trashcan_HazardousControlAnswerCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND0B: {
			Command.Trashcan_OtherControlAnswerCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND0C: {
			Command.Infrared_DeviceControlAnswerCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND0D: {
			Command.Trashcan_RecoverableFeedbackCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND0E: {
			Command.Trashcan_KitchenFeedbackCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND0F: {
			Command.Trashcan_HazardousFeedbackCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND10: {
			Command.Trashcan_OtherFeedbackCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND11: {
			Command.Infrared_DeviceFeedbackCommand(buf);
			break;
		}
		case PROTOCOL_CONTROL_COMMAND12: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND13: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND14: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND15: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND16: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND17: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND18: {
			break;
		}
		case PROTOCOL_CONTROL_COMMAND19: {
			break;
		}
		default:
			break;
		}
		return true;
	}

	/**
	 * @brief Package data ready to send.
	 * @param buf:the buffer data pointer
	 * @param len:the buffer data length
	 * @retval package data success flag. true:package data is OK. false:package
	 *         data is ERROR.
	 */
	public static boolean Protocol_DataPackage(byte[] buf, byte len) {
		short sum_value = 0;

		// Judge data length
		if (len < PROTOCOL_FRAME_MIN_LENGTH) {
			return false;
		}

		// protocol encode
		buf[0] = PROTOCOL_FRAME_HEADR;// Frame data request head
		buf[1] = len;// Frame data length
		buf[2] = PROTOCOL_FRAME_TYPE;// Frame data command type
		buf[3] = buf[3];// Frame data command

		// check sum16 value
		sum_value = Sum16_Check(buf, (byte) (buf[1] - 2));
		buf[buf[1] - 2] = (byte) (sum_value >> 8);
		buf[buf[1] - 1] = (byte) sum_value;
             //send cmd
		return true;
	}
}

/******************* (C) COPYRIGHT 2016 FCX *****END OF FILE ****/
