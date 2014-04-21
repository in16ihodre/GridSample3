package com.example.gridsample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private  int right_button_id = 0;
	private String right_name;
	private int right_id;

	int[] list = {1, 2, 3, 4, 5, 6, 7, 8, 9};

	private List<ImageButton> buttons;
	private String strings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		 buttons = new ArrayList<ImageButton>();

		MyOpenHelper helper = new MyOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		shuffle(list);

		Cursor c = db.rawQuery("Select * from TableTest order by random() limit 1;", null);
		boolean mov = c.moveToFirst();
		Cursor c2 = db.rawQuery("Select * from TableTest where name <> ? order by random() limit 8;", new String[]{c.getString(1)});
		boolean mov2 = c2.moveToFirst();

		right_name = c.getString(1);
		right_id = c.getInt(0);

		//まわりのボタン設定
		for(int i=0;i<c2.getCount();i++){
			ImageButton Button = (ImageButton) findViewById(0x7f080000 + list[i]);
			Button.setOnClickListener(this);
			Button.setImageResource(0x7f020000 + c2.getInt(0));
			buttons.add(Button);
			mov2 = c2.moveToNext();

			strings = strings + c2.getInt(0) + "  ";

/*
			Button[i] = (ImageButton)findViewById(0x7f080000+list[i]);
			Button[i] = (ImageButton)findViewById(R.id.imageButton1);
			Button[i].setImageResource(0x7f020000 + c2.getInt(0));
			Button[i].setOnClickListener(this);
			textView.setText(String.format("%d : %s : %d :%d", c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3)));
			ImageButton imgbtn = new ImageButton(this);
			imgbtn.setImageResource(list[i]+0x7f020000);
			*/
		}
		//正解ボタン設定
		right_button_id = 0x7f080000+list[8];
		ImageButton Button = (ImageButton)findViewById(right_button_id);
		Button.setImageResource(0x7f020000 + c.getInt(0));
		Button.setOnClickListener(this);

		TextView message = (TextView)this.findViewById(R.id.textView1);
		message.setText(right_name + "  は？");

		message = (TextView)this.findViewById(R.id.textView2);
		message.setText("id:"+ right_id + "   "+"name:" + right_name);

		message = (TextView)this.findViewById(R.id.textView3);
		message.setText(list[0] + "  "+  list[1] + "  " + list[2] + "  " + list[3] + "  " +list[4] + "  "  + list[5] + "  " + list[6] + "  " + list[7] + "  " + list[8]);

		TextView message2 = (TextView)this.findViewById(R.id.textView4);
		message2.setText(strings);
		/*
		Button1 = (ImageButton)findViewById(R.id.imageButton1);
		Button2 = (ImageButton)findViewById(R.id.imageButton2);
		Button3 = (ImageButton)findViewById(R.id.imageButton3);
		Button4 = (ImageButton)findViewById(R.id.imageButton4);
		Button5 = (ImageButton)findViewById(R.id.imageButton5);
		Button6 = (ImageButton)findViewById(R.id.imageButton6);
		Button7 = (ImageButton)findViewById(R.id.imageButton7);
		Button8 = (ImageButton)findViewById(R.id.imageButton8);
		Button9 = (ImageButton)findViewById(R.id.imageButton9);

		Button1.setOnClickListener(this);
		Button2.setOnClickListener(this);
		Button3.setOnClickListener(this);
		Button4.setOnClickListener(this);
		Button5.setOnClickListener(this);
		Button6.setOnClickListener(this);
		Button7.setOnClickListener(this);
		Button8.setOnClickListener(this);
		Button9.setOnClickListener(this);
		*/
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

	@Override
	public void onClick(View v) {
		if(v.getId() ==right_button_id ){
			Toast.makeText(MainActivity.this, "正解！", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(MainActivity.this, "残念！", Toast.LENGTH_SHORT).show();
		}
	}


	/*	protected void onStart(){

	}
	protected void onResume(){

	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
