package com.example.gridsample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private  int right_button_id = 0;
	private String right_name;
	private int right_id;

	int[] list = {1, 2, 3, 4, 5, 6, 7, 8, 9};

	private List<ImageButton> buttons;
	private SQLiteDatabase db;
	private AlphaAnimation feedout;
	private int num_ok;
	private int num_miss;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buttons = new ArrayList<ImageButton>();

		MyOpenHelper helper = new MyOpenHelper(this);
		db = helper.getReadableDatabase();

		//正解画像透明
		findViewById(R.id.ImageView1).setVisibility(ImageView.INVISIBLE);

		feedout = new AlphaAnimation( 1, 0 );
		feedout.setDuration(1000);
	}
	protected void onStart(){
		super.onStart();
		shuffle(list);

		Cursor c = db.rawQuery("Select * from TableTest order by random() limit 1;", null);
		c.moveToFirst();
		Cursor c2 = db.rawQuery("Select * from TableTest where name <> ? order by random() limit 8;", new String[]{c.getString(1)});
		c2.moveToFirst();

		right_id = c.getInt(0);
		right_name = c.getString(1);
		num_ok = c.getInt(2);
		num_miss = c.getInt(3);

		//まわりのボタン設定
		for(int i=0;i<c2.getCount();i++){
			ImageButton Button = (ImageButton) findViewById(0x7f080001 + list[i]);
			Button.setOnClickListener(this);
			Button.setImageResource(0x7f020001 + c2.getInt(0));
			buttons.add(Button);
			c2.moveToNext();
			//strings = strings + c2.getInt(0) + "   ";
		}
		//正解ボタン設定
		right_button_id = 0x7f080001+list[8];
		ImageButton Button = (ImageButton)findViewById(right_button_id);
		buttons.add(Button);
		Button.setImageResource(0x7f020001 + c.getInt(0));
		Button.setOnClickListener(this);

		//ボタン有効化
		allbuttonEnable(true);
		TextView message = (TextView)this.findViewById(R.id.textView1);
		message.setText(right_name + "  は？");

		message = (TextView)this.findViewById(R.id.textView2);
		message.setText("id:"+ right_id + "   "+"name:" + right_name);
		//message.setText((char)buttons.size());

		message = (TextView)this.findViewById(R.id.textView3);
		message.setText(list[0] + "  "+  list[1] + "  " + list[2] + "  " + list[3] + "  " +list[4] + "  "  + list[5] + "  " + list[6] + "  " + list[7] + "  " + list[8]);

		message = (TextView)this.findViewById(R.id.textView4);
		//message.setText(2);
	}

	//ボタンが押されたら
	@Override
	public void onClick(View v) {
		//ボタン無効化
		allbuttonEnable(false);
		ContentValues updateValues = new ContentValues();
		//正解
		if(v.getId() ==right_button_id ){
			//Toast.makeText(MainActivity.this, "正解！", Toast.LENGTH_SHORT).show();
			ImageView img = (ImageView)findViewById(R.id.ImageView1);

			updateValues.put("ok",num_ok + 1 );

			img.setImageResource(R.drawable.circle);
			img.startAnimation( feedout );
		}
		//不正解
		else{
			updateValues.put("miss",num_miss + 1 );
			for(int i = 0;i<8;i++){
				ImageButton button = buttons.get(i);
				button.startAnimation(feedout);
			}
		}
		db.update("TableTest", updateValues, "name=?", new String[]{right_name});
		buttons.clear();
		//フェードアウト分の時間待ち
		new Handler().postDelayed(new Runnable() {
			public void run() {
				onStart();
			}
		}, 1000);
	}

	private void shuffle(int[] arr) {
		for(int i=arr.length-1; i>0; i--){
			int t = (int)(Math.random() * i);  //0～i-1の中から適当に選ぶ
			//選ばれた値と交換する
			int tmp = arr[i];
			arr[i]  = arr[t];
			arr[t]  = tmp;
		}
	}

	private void allbuttonEnable(boolean b) {
		for(int i = 0;i<buttons.size();i++){
			ImageButton button = buttons.get(i);
			button.setEnabled(b);
		}
	}


	/*protected void onResume(){
		super.onResume();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
