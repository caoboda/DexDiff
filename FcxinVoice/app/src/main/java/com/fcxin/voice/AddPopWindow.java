package com.fcxin.voice;


import org.greenrobot.eventbus.EventBus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 自定义popupWindow
 *
 * @author wwj
 *
 *
 */
public class AddPopWindow extends PopupWindow implements View.OnClickListener {
	private View conentView;
	private LinearLayout ll1;
	private LinearLayout ll2;
	private LinearLayout ll3;
	private LinearLayout ll4;
	private LinearLayout ll5;
	private int h;
	private int w;
	Context context;
	private String address;


	@SuppressWarnings("deprecation")
	public AddPopWindow(final Activity context) {
		/*context.getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);*/
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.add_popup_dialog, null);

		this.context = context;
		h = context.getWindowManager().getDefaultDisplay().getHeight();
		w = context.getWindowManager().getDefaultDisplay().getWidth();
		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w / 2 + 50);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = 0.8f;
		context.getWindow().setAttributes(lp);
		// 刷新状态
		this.update();
		ll1 = (LinearLayout) conentView.findViewById(R.id.ll1);
		ll2 = (LinearLayout) conentView.findViewById(R.id.ll2);
		ll3 = (LinearLayout) conentView.findViewById(R.id.ll3);
		ll4 = (LinearLayout) conentView.findViewById(R.id.ll4);
		ll5 = (LinearLayout) conentView.findViewById(R.id.ll5);
		ll1.setOnClickListener(this);
		ll2.setOnClickListener(this);
		ll3.setOnClickListener(this);
		ll4.setOnClickListener(this);
		ll5.setOnClickListener(this);
		this.setOnDismissListener(new OnDismissListener(){

			//在dismiss中恢复透明度
			public void onDismiss(){
				WindowManager.LayoutParams lp=context.getWindow().getAttributes();
				lp.alpha = 1f;
				context.getWindow().setAttributes(lp);
			}
		});
	}

	public AddPopWindow(Activity context, String address
	) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.add_popup_dialog, null);
		this.context = context;
		this.address = address;
		h = context.getWindowManager().getDefaultDisplay().getHeight();
		w = context.getWindowManager().getDefaultDisplay().getWidth();
		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w / 2 + 50);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);

		ll1 = (LinearLayout) conentView.findViewById(R.id.ll1);
		ll2 = (LinearLayout) conentView.findViewById(R.id.ll2);
		ll3 = (LinearLayout) conentView.findViewById(R.id.ll3);
		ll5 = (LinearLayout) conentView.findViewById(R.id.ll4);
		ll5 = (LinearLayout) conentView.findViewById(R.id.ll5);
		ll1.setOnClickListener(this);
		ll2.setOnClickListener(this);
		ll3.setOnClickListener(this);
		ll4.setOnClickListener(this);
		ll5.setOnClickListener(this);

	}

	/**
	 * 显示popupWindow
	 *
	 * @param parent
	 */
	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			// 显示在左上方
			this.showAtLocation(parent, Gravity.TOP | Gravity.RIGHT, 0,
					(int) (0.113 * h));
			// 以下拉方式显示popupwindow
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
		} else {
			this.dismiss();
		}
	}

	public interface Textcallback {
		void returnText(String str);
	}

	Textcallback callback;

	public void setCallback(Textcallback callback) {
		this.callback = callback;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ll1: //搜索设备
				EventBus.getDefault().post(new EventBusEntity("serach", "serach"));
				break;
			case R.id.ll2://设置
				Intent in=new Intent(context,Settings.class);
				context.startActivity(in);
				break;
			case R.id.ll3://设置蓝牙可见
				EventBus.getDefault().post(new EventBusEntity("discoverable", "discoverable"));
				break;
			case R.id.ll4://关于
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
				localBuilder.setIcon(R.drawable.logo).setTitle(R.string.app_message).setMessage(R.string.app_provider);
				localBuilder.show();
				break;
			case R.id.ll5://exit
				MainActivity.instance.finish();

				break;
			default:
				break;
		}
		AddPopWindow.this.dismiss();
	}
}
