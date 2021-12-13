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



import Normal.INFO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterMainActivity extends Activity {

	public static final int SHOW_RESPONSE=1;
    public Handler handler=new Handler() {
        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response=(String)msg.obj;
                    if(response.equals("账号注册成功")) {
                    Toast.makeText(RegisterMainActivity.this, response, Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(RegisterMainActivity.this,MainActivity.class);
                    startActivity(i);
                    
                    }
                    break;
                default:
                    break;
            }
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_main);
		
		Button rbtn=(Button)findViewById(R.id.register_btn);
		

		rbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText runame=(EditText)findViewById(R.id.r_username);
				EditText rupass=(EditText)findViewById(R.id.r_password);
				EditText rurepass=(EditText)findViewById(R.id.r_repassword);
				EditText rname=(EditText)findViewById(R.id.r_name);
				EditText rgrade=(EditText)findViewById(R.id.r_grade);
				EditText runmber=(EditText)findViewById(R.id.p_acad);
				EditText racad=(EditText)findViewById(R.id.r_acad);
				EditText rmajor=(EditText)findViewById(R.id.r_major);
				EditText rclas=(EditText)findViewById(R.id.r_class);
				EditText rtele=(EditText)findViewById(R.id.r_telephone);
				
				String r_uname=runame.getText().toString().trim();
				String r_upass=rupass.getText().toString().trim();
				String r_urepass=rurepass.getText().toString().trim();
				String r_name=rname.getText().toString().trim();
				String r_grade=rgrade.getText().toString().trim();
				String r_unmber=runmber.getText().toString().trim();
				String r_acad=racad.getText().toString().trim();
				String r_major=rmajor.getText().toString().trim();
				String r_clas=rclas.getText().toString().trim();
				String r_tele=rtele.getText().toString().trim();
				if(r_upass.equals(r_urepass)==false) {
					Toast.makeText(RegisterMainActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
					return;}
				SendByHttpClient(r_uname,r_upass,r_name,r_grade,r_unmber,r_acad,r_major,r_clas,r_tele);
				
			}
		});
	}
	
	public void SendByHttpClient(final String r_uname,final String r_upass,final String r_name,final String r_grade,final String r_unmber,final String r_acad,final String r_major,final String rclas,final String rtele ){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"RegisterServlet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username",r_uname));
					params.add(new BasicNameValuePair("password",r_upass));
					params.add(new BasicNameValuePair("name",r_name));
					params.add(new BasicNameValuePair("grade",r_grade));
					params.add(new BasicNameValuePair("number",r_unmber));
					params.add(new BasicNameValuePair("academy",r_acad));
					params.add(new BasicNameValuePair("major",r_major));
					params.add(new BasicNameValuePair("class",rclas));
					params.add(new BasicNameValuePair("telephone",rtele));
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"UTF-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
					int code=httpResponse.getStatusLine().getStatusCode();
					if(code==200)
					{
						HttpEntity entity1=httpResponse.getEntity();
						String response=EntityUtils.toString(entity1,"utf-8");
						Message message=new Message();
						message.what=SHOW_RESPONSE;
						message.obj=response;
						handler.sendMessage(message);
						
					}
					
				}catch(Exception e){
					e.printStackTrace();
					
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_main, menu);
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
