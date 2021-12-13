package com.example.secondthings;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import Normal.INFO;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageMainActivity extends Activity {

	private TextView titleTV;
	private TextView nameTV;
	private TextView messageTV;
	private ImageView headIV;
	private ImageView photoIV;
	private ImageView deleteIV;
	private Button formBtn;

	private String username,id,name,head;
	private int idI;
	
	public Handler handler=new Handler() {
        public void handleMessage(Message msg)
        {
        	if(msg.obj.toString().equals("删除成功")){
        		Toast.makeText(MessageMainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        		Intent i=new Intent(MessageMainActivity.this,FormMainActivity.class);
        		i.putExtra("username", username);
				i.putExtra("name", name);
				i.putExtra("head", head);
        		startActivity(i);
        	}else {
        		Toast.makeText(MessageMainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        	}
			
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_main);
		
		init();
		
		formBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MessageMainActivity.this,FormMainActivity.class);
				i.putExtra("username", username);
				i.putExtra("name", name);
				i.putExtra("head", head);
				startActivity(i);
			}
		});
		
		deleteIV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new Builder(MessageMainActivity.this);
				builder.setMessage("确认删除吗");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new OnClickListener() {

					   @Override
					   public void onClick(DialogInterface dialog, int which) {
					    dialog.dismiss();
					    
					    	deleteServer(username,id);
					    	
					   
					   }
					  });
				builder.setNegativeButton("取消", new OnClickListener() {

					   @Override
					   public void onClick(DialogInterface dialog, int which) {
					    dialog.dismiss();
					   }
					  });

					  builder.create().show();
				
			}
		});
		
	}
	
	public void deleteServer(final String username,final String id){
		new Thread(new Runnable(){
			public void run(){
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/MessageDeleteSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username",username));
					params.add(new BasicNameValuePair("id",id));
					
					
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
					int code=httpResponse.getStatusLine().getStatusCode();
					
					if(code==200){
						Message msg=new Message();
						HttpEntity entityI=httpResponse.getEntity();
						String Result=EntityUtils.toString(entityI);
						msg.obj=Result;
						handler.sendMessage(msg);						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
			}
		}).start();
	}
	public void init(){
		titleTV=(TextView)findViewById(R.id.message_titleTV);
		nameTV=(TextView)findViewById(R.id.message_nameTV);
		messageTV=(TextView)findViewById(R.id.message_messageTV);
		headIV=(ImageView)findViewById(R.id.message_headIV);
		photoIV=(ImageView)findViewById(R.id.message_photoIV);
		deleteIV=(ImageView)findViewById(R.id.message_deleteIV);
		formBtn=(Button)findViewById(R.id.message_fromBtn);
		
		Intent intent=getIntent();
		titleTV.setText(intent.getStringExtra("title"));
		nameTV.setText(intent.getStringExtra("name"));
		messageTV.setText(intent.getStringExtra("message"));
		headIV.setImageBitmap(stringToBitmap(intent.getStringExtra("head")));
		photoIV.setImageBitmap(stringToBitmap(intent.getStringExtra("photo")));
	
		username=intent.getStringExtra("username");
		name=intent.getStringExtra("usrname");
		head=intent.getStringExtra("userHead");
		
		idI=intent.getIntExtra("id", 1);
		id=Integer.toString(idI);

	}

	public static Bitmap stringToBitmap(String string){
		Bitmap bitmap=null;
		try{
			byte[] bitmapArray=Base64.decode(string, Base64.DEFAULT);
			bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return bitmap;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
