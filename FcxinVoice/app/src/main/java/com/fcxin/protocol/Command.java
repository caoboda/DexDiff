/**
  ******************************************************************************
  * @file    Command.java
  * @author  chaoqun.wu
  * @version V1.0.0
  * @date    28-Nov-2016
  * @brief   This file provides the data communication server function.
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

public class Command
{
	private static final byte CLOSE_TRASH_CAN = 0x00;
	private static final byte OPEN_TRASH_CAN = 0x01;
	private static final byte OPEN_SPEECH_RECOGNITION = 0x03;
	private static final byte CLOSE_SPEECH_RECOGNITION = 0x04;	
	
	private static MainActivity mainActivity=new MainActivity();
	
	/**
	  * @brief  Test bluetooth connect status command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Test_ConnectCommand(byte[] buf)
	{
		buf[3]=0x01;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Read device hardware version command
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Read_HardwareVersionCommand(byte[] buf)
	{
		buf[3]=0x02;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

	/**
	  * @brief  Read device software version command
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Read_SoftwareVersionCommand(byte[] buf)
	{
		buf[3]=0x03;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

	/**
	  * @brief  Read bluetooth device name command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Read_BTNameCommand(byte[] buf)
	{
		buf[3]=0x04;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}	

	/**
	  * @brief  Write bluetooth device name command.
	  * @param  buf:data unpackage and package buffer.
	  * @param  name:need to modified bluetooth device name.
	  * @retval none.
	  */
	public static void Write_BTNameCommand(byte[] buf,String name)
	{  	
		String AT_name="AT+NAME="+name;	  
		byte temp[]=AT_name.getBytes();
		byte len=(byte) temp.length;
		
		buf[3]=0x05;
		System.arraycopy(temp, 0, buf, 4, len);	  
		len=(byte) (len+6);
		Protocol.Protocol_DataPackage(buf,len);
	}

	/**
	  * @brief  Read bluetooth device passwd command.
	  * @param  buf:data unpackage and package buffer. 
	  * @retval none.
	  */
	public static void Read_BTPasswdCommand(byte[] buf)
	{
		buf[3]=0x06;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

	/**
	  * @brief  Write bluetooth device passwd command.
	  * @param  buf:data unpackage and package buffer.
	  * @param  name:need to modified bluetooth device passwd.
	  * @retval none.
	  */
	public static void Write_BTPasswdCommand(byte[] buf,String pass)
	{ 
		String AT_pass="AT+PASS="+pass;	  
		byte temp[]=AT_pass.getBytes();
		byte len=(byte) temp.length;	 
		
		buf[3]=0x07;
		System.arraycopy(temp, 0, buf, 4, len);	  
		len=(byte) (len+6);
		Protocol.Protocol_DataPackage(buf,len);
	}
	
	/**
	  * @brief  Recoverable trashcan control command.
	  * @param  buf:data unpackage and package buffer.
	  * @param  data:control device action data
	  * @retval none.
	  */
	public static void Trashcan_RecoverableControlCommand(byte[] buf,byte data)
	{
		buf[3]=0x08;
		buf[4]=data;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Kitchen trashcan control command.
	  * @param  buf:data unpackage and package buffer.
	  * @param  data:control device action data
	  * @retval none.
	  */
	public static void Trashcan_KitchenControlCommand(byte[] buf,byte data)
	{
		buf[3]=0x09;
		buf[4]=data;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Hazardous trashcan control command.
	  * @param  buf:data unpackage and package buffer.
	  * @param  data:control device action data
	  * @retval none.
	  */
	public static void Trashcan_HazardousControlCommand(byte[] buf,byte data)
	{
		buf[3]=0x0A;
		buf[4]=data;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Other trashcan control command.
	  * @param  buf:data unpackage and package buffer.
	  * @param  data:control device action data
	  * @retval none.
	  */
	public static void Trashcan_OtherControlCommand(byte[] buf,byte data)
	{
		buf[3]=0x0B;
		buf[4]=data;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Infrared device control command.
	  * @param  buf:data unpackage and package buffer.
	  * @param  data:control device action data
	  * @retval none.
	  */
	public static void Infrared_DeviceControlCommand(byte[] buf,byte data)
	{
		buf[3]=0x0C;
		buf[4]=data;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Recoverable trashcan device control answer command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Trashcan_RecoverableControlAnswerCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_RECOVERABLE_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
	}
	
	/**
	  * @brief  Kitchen trashcan control answer command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Trashcan_KitchenControlAnswerCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_KITCHEN_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
	}
	
	/**
	  * @brief  Hazardous trashcan control answer command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Trashcan_HazardousControlAnswerCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_HAZARDOURS_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
	}
	
	/**
	  * @brief  Other trashcan control answer command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Trashcan_OtherControlAnswerCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_OTHER_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
	}
	
	/**
	  * @brief  Inferiority device control answer command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Infrared_DeviceControlAnswerCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			//mainActivity.FeedBackCommand(mainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
	}
	
	/**
	  * @brief  Recoverable trashcan device feedback command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Trashcan_RecoverableFeedbackCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_RECOVERABLE_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
		buf[3]=0x0D;
		buf[4]=buf[4];
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Kitchen trashcan feedback command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Trashcan_KitchenFeedbackCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_KITCHEN_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
		buf[3]=0x0E;
		buf[4]=buf[4];
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Hazardous trashcan feedback command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Trashcan_HazardousFeedbackCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_HAZARDOURS_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
		buf[3]=0x0F;
		buf[4]=buf[4];
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Other trashcan feedback command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Trashcan_OtherFeedbackCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_OTHER_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
		buf[3]=0x10;
		buf[4]=buf[4];
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Infrared device feedback command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Infrared_DeviceFeedbackCommand(byte[] buf)
	{
		switch(buf[4])
		{
		case OPEN_TRASH_CAN:
		{
			//mainActivity.FeedBackCommand(mainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case CLOSE_TRASH_CAN:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_ALL_TRASHCAN);
			break;
		}
		case OPEN_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_OPEN_SPEECH_RECOGNITION);
			break;
		}
		case CLOSE_SPEECH_RECOGNITION:
		{
			mainActivity.FeedBackCommand(MainActivity.COMMAND_CLOSE_SPEECH_RECOGNITION);
			break;
		}
		}
		buf[3]=0x11;
		buf[4]=buf[4];
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Read device clock command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Read_DeviceClockCommand(byte[] buf)
	{
		buf[3]=0x12;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}
	
	/**
	  * @brief  Read device clock command..
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Write_DeviceClockCommand(byte[] buf,String time)
	{	
		byte temp[]=time.getBytes();
		byte len=(byte) temp.length;	 
		
		buf[3]=0x13;
		System.arraycopy(temp, 0, buf, 4, len);
		len=(byte) (len+6);
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

	/**
	  * @brief  Device shutdown command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Device_ShutdownCommand(byte[] buf)
	{
		buf[3]=0x14;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

	/**
	  * @brief  Device startup command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Device_StartupCommand(byte[] buf)
	{
		buf[3]=0x15;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

	/**
	  * @brief  Device restart command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Device_RestartCommand(byte[] buf)
	{
		buf[3]=0x16;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

	/**
	  * @brief  Device restore command.
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Device_RestoreCommand(byte[] buf)
	{
		buf[3]=0x17;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

	/**
	  * @brief  Read all log count command
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Read_LogMessageCountCommand(byte[] buf)
	{
		buf[3]=0x18;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

	/**
	  * @brief  Read log detail message command
	  * @param  buf:data unpackage and package buffer.
	  * @retval none.
	  */
	public static void Read_LogMessageDetailCommand(byte[] buf)
	{
		buf[3]=0x19;
		buf[4]=0x01;
		Protocol.Protocol_DataPackage(buf,(byte) 0x07);
	}

}

/******************* (C) COPYRIGHT 2016 FCX *****END OF FILE ****/
