package com.example.gridsample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
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
	private String strings;
	private SQLiteDatabase db;
	private AlphaAnimation feedout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buttons = new ArrayList<ImageButton>();

		MyOpenHelper helper = new MyOpenHelper(this);
		db = helper.getReadableDatabase();

		//正解画像透明
		findViewById(R.id.ImageView1).setVisibility(ImageView.INVISIBLE);

		//フェードアウトアニメーションの設定
		feedout = new AlphaAnimation( 1, 0 );
		feedout.setDuration(1000);
	}
	protected void onStart(){
		super.onStart();
		shuffle(list);

		//正解パネルの取り出し
		Cursor c = db.rawQuery("Select * from TableTest order by random() limit 1;", null);
		c.moveToFirst();
		//周りのパネルの取り出し
		Cursor c2 = db.rawQuery("Select * from TableTest where name <> ? order by random() limit 8;", new String[]{c.getString(1)});
		c2.moveToFirst();

		right_name = c.getString(1);
		right_id = c.getInt(0);

		//まわりのボタン設定
		for(int i=0;i<c2.getCount();i++){
			ImageButton Button = (ImageButton) findViewById(0x7f080000 + list[i]+1);
			Button.setOnClickListener(this);
			Button.setImageResource(0x7f020000 + c2.getInt(0)+1);
			buttons.add(Button);
			c2.moveToNext();
			//strings = strings + c2.getInt(0) + "   ";
		}
		//正解ボタン設定
		right_button_id = 0x7f080000+list[8]+1;
		ImageButton Button = (ImageButton)findViewById(right_button_id);
		buttons.add(Button);
		Button.setImageResource(0x7f020000 + c.getInt(0)+1);
		Button.setOnClickListener(this);

		//ボタン有効化
		allbuttonEnable(true);

		TextView message = (TextView)this.findViewById(R.id.textView1);
		message.setText(right_name + "  は？");

		message = (TextView)this.findViewById(R.id.textView2);
		message.setText("id:"+ right_id + "   "+"name:" + right_name);

		message = (TextView)this.findViewById(R.id.textView3);
		message.setText(list[0] + "  "+  list[1] + "  " + list[2] + "  " + list[3] + "  " +list[4] + "  "  + list[5] + "  " + list[6] + "  " + list[7] + "  " + list[8]);

		TextView message2 = (TextView)this.findViewById(R.id.textView4);
		//message2.setText(x);
	}


	@Override
	public void onClick(View v) {
		//ボタン無効化
		allbuttonEnable(false);
		if(v.getId() ==right_button_id ){
			//Toast.makeText(MainActivity.this, "正解！", Toast.LENGTH_SHORT).show();
			ImageView img = (ImageView)findViewById(R.id.ImageView1);
			img.setImageResource(R.drawable.circle);
			img.startAnimation( feedout );

		}
		else{
			ImageButton button = (ImageButton)findViewById(v.getId());
			button.startAnimation(feedout);
		}
		new Handler().postDelayed(new Runnable() {
			public void run() {
				onStart();
			}
		}, 1000);
	}

	//1～9をランダムに並び替え　ボタン配置用
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
