package com.fcxin.voice;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.fcxin.bluetooth.DeviceListActivity;
import com.fcxin.bluetooth.DeviceListActivity1;
import com.fcxin.protocol.Command;
import com.fcxin.protocol.Protocol;
import com.fcxin.server.UartService;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,OnDoubleTapListener,OnTouchListener,OnGestureListener
{
	/** Called when the activity is first created. */
	public static MainActivity instance;
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	public static final int REQUEST_CONNECT_DEVICE = 1; // 连接蓝牙返回结果
	public static final int REQUEST_ENABLE_BT = 2;

	public static final byte COMMAND_CLOSE_SPEECH_RECOGNITION = 0x00;
	public static final byte COMMAND_OPEN_SPEECH_RECOGNITION = 0x01;
	public static final byte COMMAND_CLOSE_ALL_TRASHCAN = 0x02;
	public static final byte COMMAND_OPEN_RECOVERABLE_TRASHCAN= 0x03;
	public static final byte COMMAND_OPEN_KITCHEN_TRASHCAN = 0x04;
	public static final byte COMMAND_OPEN_HAZARDOURS_TRASHCAN = 0x05;
	public static final byte COMMAND_OPEN_OTHER_TRASHCAN = 0x06;

	public static final String SYNTHESIZER_NOTICE_VOICE = "请告诉我，垃圾是什么？我帮您打开垃圾桶。";
	public static final String DEVICE_NAME = "device_name";// 正在连接上来的蓝牙设备名称
	public static final String TOAST = "toast";

	public static final String RECOVERABLE_GARBAGE_CLASS = "可回收垃圾";
	public static final String RECOVERABLE_GARBAGE_BOOKS = "图书";
	public static final String RECOVERABLE_GARBAGE_NEWSPAPER = "报纸";
	public static final String RECOVERABLE_GARBAGE_WRAPPER = "包装纸";
	public static final String RECOVERABLE_GARBAGE_PLASTIC_BAG = "塑料袋";
	public static final String RECOVERABLE_GARBAGE_FOOD_PACKAGE = "食品包装袋";
	public static final String RECOVERABLE_GARBAGE_DISPOSABLE_PLASTIC_TABLEWARE = "一次性塑料餐具";
	public static final String RECOVERABLE_GARBAGE_GLASS = "玻璃";
	public static final String RECOVERABLE_GARBAGE_RING_PULL_CAN = "易拉罐";
	public static final String RECOVERABLE_GARBAGE_METAL_CANS = "金属罐头";
	public static final String RECOVERABLE_GARBAGE_OLD_CLOTHES = "旧衣服";
	public static final String RECOVERABLE_GARBAGE_TABLECLOTH = "桌布";
	public static final String RECOVERABLE_GARBAGE_TOWEL = "毛巾";
	public static final String RECOVERABLE_GARBAGE_SCHOOLBAG = "书包";
	public static final String RECOVERABLE_GARBAGE_SHOWS = "鞋子";
	public static final String RECOVERABLE_GARBAGE_PLASTIC_BOTTLE = "塑料瓶";
	public static final String RECOVERABLE_GARBAGE_POTATO_CHIPS_BAG = "薯片袋";
	public static final String RECOVERABLE_GARBAGE_COKE_BOTTLE = "可乐瓶";
	public static final String RECOVERABLE_GARBAGE_USED_MOBILEPHONE = "旧手机";
	public static final String RECOVERABLE_GARBAGE_USED_HOMEAPPLIANCE = "旧家电";

	public static final String KITCHEN_GARBAGE_CLASS = "厨余垃圾";
	public static final String KITCHEN_GARBAGE_LEFTOVERS = "剩饭剩菜";
	public static final String KITCHEN_GARBAGE_FRUIT_PEEL = "水果皮";
	public static final String KITCHEN_GARBAGE_FOOD_LEAVES = "菜叶子";
	public static final String KITCHEN_GARBAGE_VEGETABLE_ROOTS = "菜根";
	public static final String KITCHEN_GARBAGE_FISHBONE = "鱼骨头";
	public static final String KITCHEN_GARBAGE_CHICHEN_BONES = "鸡骨头";
	public static final String KITCHEN_GARBAGE_APPLE_CORE = "苹果核";
	public static final String KITCHEN_GARBAGE_BANANA_PEEL = "香蕉皮";
	public static final String KITCHEN_GARBAGE_PEANUT_SHELL = "花生壳";
	public static final String KITCHEN_GARBAGE_SHEEL_OF_MELON_SEED = "瓜子壳";
	public static final String KITCHEN_GARBAGE_WASTE_COOKING_OIL = "废弃食用油";
	public static final String KITCHEN_GARBAGE_TRA_DREGS = "茶叶渣";
	public static final String KITCHEN_GARBAGE_EXPIRED_FOOD = "过期食品";
	public static final String KITCHEN_GARBAGE_TREE_BRANCH = "树枝";
	public static final String KITCHEN_GARBAGE_FALLEN_LEAVES = "落叶";
	public static final String KITCHEN_GARBAGE_FRESH_FLOWER = "鲜花";

	public static final String HAZARDOUS_GARBAGE_CLASS = "有害垃圾";
	public static final String HAZARDOUS_GARBAGE_WASTE_BATTERY = "废旧电池";
	public static final String HAZARDOUS_GARBAGE_BULB= "灯泡";
	public static final String HAZARDOUS_GARBAGE_EXPIRED_DRUGS = "过期药品";
	public static final String HAZARDOUS_GARBAGE_EXPIRED_COSMETICS = "过期化妆品";
	public static final String HAZARDOUS_GARBAGE_MERCURIAL_THERMOMETER = "水银温度计";
	public static final String HAZARDOUS_GARBAGE_MODULATOR_TUBE = "灯管";
	public static final String HAZARDOUS_GARBAGE_PAINT_BUCKET = "油漆桶";
	public static final String HAZARDOUS_GARBAGE_NAIL_POLISH = "指甲油";
	public static final String HAZARDOUS_GARBAGE_INK_BOX = "墨盒";
	public static final String HAZARDOUS_GARBAGE_TONER_CARTRIDGES = "硒鼓";

	public static final String OTHER_GARBAGE_CLASS = "其他垃圾";
	public static final String OTHER_GARBAGE_DUST = "灰尘";
	public static final String OTHER_GARBAGE_BUTT = "烟头";
	public static final String OTHER_GARBAGE_TOILET_PAPER = "卫生纸";
	public static final String OTHER_GARBAGE_TILE = "砖瓦";
	public static final String OTHER_GARBAGE_CHINA_BOWL = "瓷碗";
	public static final String OTHER_GARBAGE_PLATES = "瓷盘";
	public static final String OTHER_GARBAGE_COAL_CINDER = "煤渣";
	public static final String OTHER_GARBAGE_BUILDING_RUBBISH = "建筑垃圾";
	public static final String OTHER_GARBAGE_LAVATORY_PAPER = "厕所纸";
	public static final String OTHER_GARBAGE_BABY_DIAPERS = "尿不湿";

	// 声明控件
	private static LinearLayout linerLayout;
	private static EditText editText1;
	private static ImageView imageView1;
	private static TextView titleView;
	private static Toast toast=null;

	//Detects various gestures
	private static GestureDetector mGestureDetector;

	//振动器接口函数
	private static Vibrator vibrator=null;

	private static SpeechRecognizer mIat;
	private static RecognizerDialog mIatDialog;
	private static SpeechSynthesizer mSpeechSynthesizer;
	private static SharedPreferences mSharedPreferences;

	private static final int REQUEST_SELECT_DEVICE = 1;
	private static final int UART_PROFILE_READY = 10;
	public static final String TAG = "nRFUART";
	private static final int UART_PROFILE_CONNECTED = 20;
	private static final int UART_PROFILE_DISCONNECTED = 21;
	private static final int STATE_OFF = 10;

	private int mState = UART_PROFILE_DISCONNECTED;
	private UartService mService = null;
	private BluetoothDevice mDevice = null;
	private BluetoothAdapter mBtAdapter = null;
	private ListView messageListView;
	private ArrayAdapter<String> listAdapter;

	//Name of the connected device
	private static String mConnectedDeviceName = null;
	private static String mEngineType = SpeechConstant.TYPE_CLOUD;

	private static boolean showIatDialogFlag=false;
	private static int clearIatDialogWaitTimes=0;

	private static TimerThread thread;
	private static boolean threadflag=false;
	private static ImageView iv_add;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//设置横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);
		instance=this;

		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);//振动器初始化
		mGestureDetector = new GestureDetector(this,this);

		linerLayout=(LinearLayout)findViewById(R.id.layout1);

		titleView=(TextView)findViewById(R.id.tv_title);
		titleView.setText(R.string.title_not_connected);
		titleView.setOnClickListener(this);
		EventBus.getDefault().register(this);
		editText1 = (EditText) findViewById(R.id.editText1);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		iv_add.setOnClickListener(this);
		imageView1.setOnTouchListener(this);
		imageView1.setLongClickable(true);

		mIat=SpeechRecognizer.createRecognizer(this,mInitListener);
		mIatDialog = new RecognizerDialog(this,mInitListener);
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this,mInitListener);
		mSharedPreferences = getSharedPreferences("com.iflytek.setting",Activity.MODE_PRIVATE);

		/*
		String contents = readFile(this, "userwords","UTF-8");
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		mIat.setParameter(SpeechConstant.TEXT_ENCODING, "UTF-8");
		int ret = mIat.updateLexicon("userword", contents, mLexiconListener);
		if (ret != ErrorCode.SUCCESS) ToastToShowText(R.string.text_upload_Failed + ret);
		else ToastToShowText(R.string.text_upload_success+ ret);
		*/

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBtAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		service_init();
	}

	@Override
	// 启动activity时，开启手机蓝牙
	public void onStart()
	{
		super.onStart();
		if (!mBtAdapter.isEnabled())
		{
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}

	}


	//UART service connected/disconnected
	public ServiceConnection mServiceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder rawBinder) {
			mService = ((UartService.LocalBinder) rawBinder).getService();
			Log.d(TAG, "onServiceConnected mService= " + mService);
			if (!mService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}

		}

		public void onServiceDisconnected(ComponentName classname) {
			////     mService.disconnect(mDevice);
			mService = null;
		}
	};



	private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			final Intent mIntent = intent;
			//*********************//
			if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
				runOnUiThread(new Runnable() {
					public void run() {
						String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
						Log.d(TAG, "UART_CONNECT_MSG");
						//  btnConnectDisconnect.setText("Disconnect");
						titleView.setText(mDevice.getName()+ " - ready");
						listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName());
						messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
						mState = UART_PROFILE_CONNECTED;
					}
				});
			}

			//*********************//
			if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
				runOnUiThread(new Runnable() {
					public void run() {
						String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
						Log.d(TAG, "UART_DISCONNECT_MSG");

						titleView.setText("Not Connected");
						listAdapter.add("["+currentDateTimeString+"] Disconnected to: "+ mDevice.getName());
						mState = UART_PROFILE_DISCONNECTED;
						mService.close();
						//setUiState();

					}
				});
			}


			//*********************//
			if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
				mService.enableTXNotification();
			}
			//*********************//
			if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {

				final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							String text = "";

							for (int i = 0; i < txValue.length; i++)
							{
								String hex = Integer.toHexString(txValue[i] & 0xFF);
								if (hex.length() == 1) {
									hex = '0' + hex;
								}
								text += hex.toUpperCase();
							}

							String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
							listAdapter.add("["+currentDateTimeString+"] RX: "+text);
							messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);

						} catch (Exception e) {
							Log.e(TAG, e.toString());
						}
					}
				});
			}
			//*********************//
			if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
				showMessage("Device doesn't support UART. Disconnecting");
				mService.disconnect();
			}


		}
	};


	private void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

	}



	private void service_init() {
		Intent bindIntent = new Intent(this, UartService.class);
		bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

		LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
	}
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
		return intentFilter;
	}


	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		if (!mBtAdapter.isEnabled()) {
			Log.i(TAG, "onResume - BT not enabled yet");
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}

	}
	@Override
	// 关闭activity时，关闭手机蓝牙
	public void onDestroy()
	{
		super.onDestroy();

		Log.d(TAG, "onDestroy()");

		try {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
		} catch (Exception ignore) {
			Log.e(TAG, ignore.toString());
		}
		unbindService(mServiceConnection);
		mService.stopSelf();
		mService= null;
		// 蓝牙聊天服务站
		threadflag=false;
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClick(View v)
	{
		if(v.equals(titleView))
		{
			vibrator.vibrate(50);
		}
		else if(v.equals(iv_add))
		{
			new AddPopWindow(this).showPopupWindow(iv_add);
		}
	}

	public class TimerThread extends Thread
	{
		@Override
		public void run()
		{
			while(threadflag)
			{
				try
				{
					clearIatDialogWaitTimes++;
					if(clearIatDialogWaitTimes>499)//5s
					{
						clearIatDialogWaitTimes=0;
						//synthetizeStopSpeaking();
						CancelIatDialog();
						threadflag=false;
					}
					sleep(10);//10ms
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			thread.interrupt();
			thread=null;
		}
	}

	/**
	 * 参数设置
	 *
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences.getString("iat_language_preference","mandarin");
		if (lag.equals("en_us"))
		{
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		}
		else
		{
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		//mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
		//mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");

		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		//mIat.setParameter(SpeechConstant.ASR_DWA, mSharedPreferences.getString("iat_dwa_preference", "0"));
	}

	protected void synthetizeInSilence()
	{
		// TODO Auto-generated method stub
		if (null == mSpeechSynthesizer)
		{
			// 创建合成对象.
			mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this,mInitListener);
		}
		mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
		mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "80");
		mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
		String source = editText1.getText().toString();
		if (source.length() == 0)
		{
			source = SYNTHESIZER_NOTICE_VOICE;
		}
		//进行语音合成.
		mSpeechSynthesizer.startSpeaking(source, mTtsListener);
		//ToastToShowText(R.string.buffering);
	}

	protected void synthetizeStopSpeaking()
	{
		mSpeechSynthesizer.stopSpeaking();
	}

	private SynthesizerListener mTtsListener = new SynthesizerListener()
	{

		@Override
		public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void onCompleted(SpeechError arg0) {
			// TODO 自动生成的方法存根
			if(showIatDialogFlag==true)
			{
				showIatDialog();
				showIatDialogFlag=false;
			}

		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void onSpeakBegin() {
			// TODO 自动生成的方法存根

		}

		@Override
		public void onSpeakPaused() {
			// TODO 自动生成的方法存根

		}

		@Override
		public void onSpeakProgress(int arg0, int arg1, int arg2) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void onSpeakResumed() {
			// TODO 自动生成的方法存根

		}
	};

	protected void showIatDialog()
	{
		// TODO Auto-generated method stub
		if (null == mIatDialog)
		{
			// 初始化听写Dialog
			mIatDialog = new RecognizerDialog(this,mInitListener);
		}
		setParam();
		// 清空Grammar_ID，防止识别后进行听写时Grammar_ID的干扰
		//mIatDialog.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
		// 设置听写Dialog的引擎
		//mIatDialog.setParameter(SpeechConstant.DOMAIN, "iat");
		//mIatDialog.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
		// 清空结果显示框.
		//editText1.setText(null);
		// 显示听写对话框
		mIatDialog.setListener(recognizerDialogListener);
		mIatDialog.show();
		//ToastToShowText(R.string.please_start_talking);
	}

	protected void CancelIatDialog()
	{
		mIatDialog.cancel();
	}

	RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener()
	{
		@Override
		public void onResult(RecognizerResult arg0, boolean arg1)
		{
			// TODO Auto-generated method stub
			String text=JsonParser.parseIatResult(arg0.getResultString());
			if(text.length()!=0)
			{
				editText1.append(text);
				editText1.setSelection(editText1.length());
				Garbage_Classification(text);
			}
		}

		@Override
		public void onError(SpeechError arg0)
		{
			// TODO Auto-generated method stub
			thread=new TimerThread();
			thread.start();
			threadflag=true;
			clearIatDialogWaitTimes=0;
			//ToastToShowText(arg0.toString());
		}
	};

	/**
	 * 上传联系人/词表监听器。
	 */
	private LexiconListener mLexiconListener = new LexiconListener() {

		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if (error != null) {
				ToastToShowText(error.toString());
			} else {
				//ToastToShowText(getString(R.string.text_upload_success));
			}
		}
	};

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				ToastToShowText(R.string.failed_init + code);
			}
		}
	};

	/**
	 * 用户登录回调监听器.
	 */
	private SpeechListener listener = new SpeechListener()
	{

		@Override
		public void onBufferReceived(byte[] arg0) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void onCompleted(SpeechError arg0) {
			// TODO 自动生成的方法存根
			if (arg0 != null)
			{
				ToastToShowText(R.string.login_failed);
			}
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
			// TODO 自动生成的方法存根

		}
	};

	/**
	 * 读取asset目录下文件。
	 * @return content
	 */
	public static String readFile(Context mContext,String file,String code)
	{
		int len = 0;
		byte []buf = null;
		String result = "";
		try {
			InputStream in = mContext.getAssets().open(file);
			len  = in.available();
			buf = new byte[len];
			in.read(buf, 0, len);

			result = new String(buf,code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void Garbage_Classification(String input_message)
	{
		byte garbage_type=0x00;

		input_message=input_message.replace("。", "").replace("！", "").replace("？", "").replace(".", "").replace(" ", "");

		switch(input_message)
		{
			//Recoverable garbage class
			case RECOVERABLE_GARBAGE_CLASS:
			case RECOVERABLE_GARBAGE_BOOKS:
			case RECOVERABLE_GARBAGE_NEWSPAPER:
			case RECOVERABLE_GARBAGE_WRAPPER:
			case RECOVERABLE_GARBAGE_PLASTIC_BAG:
			case RECOVERABLE_GARBAGE_FOOD_PACKAGE:
			case RECOVERABLE_GARBAGE_GLASS:
			case RECOVERABLE_GARBAGE_DISPOSABLE_PLASTIC_TABLEWARE:
			case RECOVERABLE_GARBAGE_RING_PULL_CAN:
			case RECOVERABLE_GARBAGE_METAL_CANS:
			case RECOVERABLE_GARBAGE_OLD_CLOTHES:
			case RECOVERABLE_GARBAGE_TABLECLOTH:
			case RECOVERABLE_GARBAGE_TOWEL:
			case RECOVERABLE_GARBAGE_SCHOOLBAG:
			case RECOVERABLE_GARBAGE_SHOWS:
			case RECOVERABLE_GARBAGE_PLASTIC_BOTTLE:
			case RECOVERABLE_GARBAGE_POTATO_CHIPS_BAG:
			case RECOVERABLE_GARBAGE_COKE_BOTTLE:
			case RECOVERABLE_GARBAGE_USED_MOBILEPHONE:
			case RECOVERABLE_GARBAGE_USED_HOMEAPPLIANCE:
			{
				garbage_type=0x01;
				break;
			}
			//kitchen garbage class
			case KITCHEN_GARBAGE_CLASS:
			case KITCHEN_GARBAGE_LEFTOVERS:
			case KITCHEN_GARBAGE_FRUIT_PEEL:
			case KITCHEN_GARBAGE_FOOD_LEAVES:
			case KITCHEN_GARBAGE_VEGETABLE_ROOTS:
			case KITCHEN_GARBAGE_FISHBONE:
			case KITCHEN_GARBAGE_CHICHEN_BONES:
			case KITCHEN_GARBAGE_APPLE_CORE:
			case KITCHEN_GARBAGE_BANANA_PEEL:
			case KITCHEN_GARBAGE_PEANUT_SHELL:
			case KITCHEN_GARBAGE_SHEEL_OF_MELON_SEED:
			case KITCHEN_GARBAGE_WASTE_COOKING_OIL:
			case KITCHEN_GARBAGE_TRA_DREGS:
			case KITCHEN_GARBAGE_EXPIRED_FOOD:
			case KITCHEN_GARBAGE_TREE_BRANCH:
			case KITCHEN_GARBAGE_FALLEN_LEAVES:
			case KITCHEN_GARBAGE_FRESH_FLOWER:
			{
				garbage_type=0x02;
				break;
			}
			//hazardous garbage class
			case HAZARDOUS_GARBAGE_CLASS:
			case HAZARDOUS_GARBAGE_WASTE_BATTERY:
			case HAZARDOUS_GARBAGE_BULB:
			case HAZARDOUS_GARBAGE_EXPIRED_DRUGS:
			case HAZARDOUS_GARBAGE_EXPIRED_COSMETICS:
			case HAZARDOUS_GARBAGE_MERCURIAL_THERMOMETER:
			case HAZARDOUS_GARBAGE_MODULATOR_TUBE:
			case HAZARDOUS_GARBAGE_PAINT_BUCKET:
			case HAZARDOUS_GARBAGE_NAIL_POLISH:
			case HAZARDOUS_GARBAGE_INK_BOX:
			case HAZARDOUS_GARBAGE_TONER_CARTRIDGES:
			{
				garbage_type=0x03;
				break;
			}
			//other garbage
			case OTHER_GARBAGE_CLASS:
			case OTHER_GARBAGE_DUST:
			case OTHER_GARBAGE_BUTT:
			case OTHER_GARBAGE_TOILET_PAPER:
			case OTHER_GARBAGE_TILE:
			case OTHER_GARBAGE_CHINA_BOWL:
			case OTHER_GARBAGE_PLATES:
			case OTHER_GARBAGE_COAL_CINDER:
			case OTHER_GARBAGE_BUILDING_RUBBISH:
			case OTHER_GARBAGE_LAVATORY_PAPER:
			case OTHER_GARBAGE_BABY_DIAPERS:
			{
				garbage_type=0x04;
				break;
			}
			//default class
			default:break;
		}

		byte[] send_data=new byte[7];
		switch(garbage_type)
		{
			case 0x01:
			{
				Command.Trashcan_RecoverableControlCommand(send_data,(byte) 0x01);
				break;
			}
			case 0x02:
			{
				Command.Trashcan_KitchenControlCommand(send_data,(byte) 0x01);
				break;
			}
			case 0x03:
			{
				Command.Trashcan_HazardousControlCommand(send_data,(byte) 0x01);
				break;
			}
			case 0x04:
			{
				Command.Trashcan_OtherControlCommand(send_data,(byte) 0x01);
				break;
			}
			default:break;
		}
	}

/*	public void sendMessage(String message)
	{
		if (chatService.getState() != BluetoothService.STATE_CONNECTED)
		{
			//在这里不是提示用户未连接，而是直接弹出蓝牙连接界面
			//Intent serverIntent = new Intent(this, DeviceListActivity.class);
			//startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			//ToastToShowText(R.string.not_connected);
			return;
		}
		if (message.length() > 0)
		{
			byte[] send = null;
			try {
				send = message.getBytes("GB2312");
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			chatService.write(send);
		}
	}*/

	/*public void sendMessage(byte[] message)
	{
		if (chatService.getState() != BluetoothService.STATE_CONNECTED)
		{
			//在这里不是提示用户未连接，而是直接弹出蓝牙连接界面
			//Intent serverIntent = new Intent(this, DeviceListActivity.class);
			//startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			//ToastToShowText(R.string.not_connected);
			return;
		}
		if (message.length> 0)
		{
			chatService.write(message);
		}
	}*/

	public void FeedBackCommand(byte command)
	{
		switch(command)
		{
			case COMMAND_CLOSE_SPEECH_RECOGNITION:
			{
				synthetizeStopSpeaking();
				CancelIatDialog();
				break;
			}
			case COMMAND_OPEN_SPEECH_RECOGNITION:
			{
				editText1.setText("");
				synthetizeStopSpeaking();
				CancelIatDialog();
				synthetizeInSilence();
				showIatDialogFlag=true;
				break;
			}
			case COMMAND_CLOSE_ALL_TRASHCAN:
			{
				editText1.setText("");
				linerLayout.setBackgroundResource(R.drawable.intelligent_garbage);
				break;
			}
			case COMMAND_OPEN_RECOVERABLE_TRASHCAN:
			{
				linerLayout.setBackgroundResource(R.drawable.recoverable_garbage);
				break;
			}
			case COMMAND_OPEN_KITCHEN_TRASHCAN:
			{
				linerLayout.setBackgroundResource(R.drawable.kitchen_garbage);
				break;
			}
			case COMMAND_OPEN_HAZARDOURS_TRASHCAN:
			{
				linerLayout.setBackgroundResource(R.drawable.hazardous_garbage);
				break;
			}
			case COMMAND_OPEN_OTHER_TRASHCAN:
			{
				linerLayout.setBackgroundResource(R.drawable.other_garbage);
				break;
			}
		}
	}

	public final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case MESSAGE_STATE_CHANGE:
					break;
				case MESSAGE_WRITE:
					break;
				case MESSAGE_READ:
					byte[] readBuf = new byte[msg.arg1];
					System.arraycopy(msg.obj, 0, readBuf, 0, msg.arg1);
					Protocol.Protocol_DataUnpack(readBuf, (byte) msg.arg1);
					break;
				case MESSAGE_DEVICE_NAME:
					mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
					ToastToShowText(getResources().getString(R.string.connected_to) + ' ' + mConnectedDeviceName);
					break;
				case MESSAGE_TOAST:
					ToastToShowText(msg.getData().getString(TOAST));
					break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

			case REQUEST_SELECT_DEVICE:
				//When the DeviceListActivity return, with the selected device address
				if (resultCode == Activity.RESULT_OK && data != null) {
					String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
					mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

					Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
					titleView.setText(mDevice.getName()+ " - connecting");
					mService.connect(deviceAddress);
				}
				break;
			case REQUEST_ENABLE_BT:
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK) {
					Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();

				} else {
					// User did not enable Bluetooth or an error occurred
					Log.d(TAG, "BT not enabled");
					Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
			default:
				Log.e(TAG, "wrong request code");
				break;
		}
	}



	public void ensureDiscoverable()
	{
		if (mBtAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
		{
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
			localBuilder.setIcon(R.drawable.logo).setTitle(R.string.tips).setMessage(R.string.exit_confirm);
			localBuilder.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface paramDialogInterface,int paramInt)
				{
					finish();
				}
			});
			localBuilder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface paramDialogInterface,int paramInt)
				{
					paramDialogInterface.cancel();
				}
			}).create();
			localBuilder.show();
		}
		else if(keyCode == KeyEvent.KEYCODE_MENU)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		setIconEnable(menu, true);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.setData(Uri.parse("http://www.fcxin.com"));
				startActivity(intent);
				return true;
			case R.id.connect:
				Intent serverIntent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
				return true;
			case R.id.discoverable:
				ensureDiscoverable();
				return true;
			case R.id.setup:
				Intent in=new Intent(this,Settings.class);
				startActivity(in);
				return true;
			case R.id.about:
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
				localBuilder.setIcon(R.drawable.logo).setTitle(R.string.app_tips).setMessage(R.string.app_provider);
				localBuilder.show();
				return true;
			case R.id.exit:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	//enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效
	public void setIconEnable(Menu menu, boolean enable)
	{
		try
		{
			Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
			Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
			m.setAccessible(true);
			//MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
			m.invoke(menu, enable);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void ToastToShowText(int showdata)
	{
		if (toast != null)
		{
			toast.setText(showdata);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		}
		else
		{
			toast=Toast.makeText(this, showdata, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void ToastToShowText(String ShowText)
	{
		if (toast != null)
		{
			toast.setText(ShowText);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		}
		else
		{
			toast=Toast.makeText(this, ShowText, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO 自动生成的方法存根
		v.performClick();
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO 自动生成的方法存根
		vibrator.vibrate(100);
		editText1.setText(null);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)
	{
		// TODO 自动生成的方法存根
		vibrator.vibrate(50);
		String source = editText1.getText().toString();
		if(source.length()==0)
		{
			synthetizeInSilence();
			showIatDialogFlag=true;
		}
		else
		{
			Garbage_Classification(source);
		}
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e)
	{
		// TODO 自动生成的方法存根
		vibrator.vibrate(50);
		synthetizeInSilence();
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
	public void getEventBusMessage(EventBusEntity entity) {
		switch (entity.getOption()) {
			case "serach"://断开连接了
				Intent serverIntent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(serverIntent, REQUEST_SELECT_DEVICE);
				break;

			case "discoverable":
				ensureDiscoverable();
				break;

			default:
				break;
		}
	}
}
