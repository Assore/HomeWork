package com.example.secondthings;

import java.net.URLDecoder;
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

public class MainActivity extends Activity {

Message msg=new Message();
	
	public Handler handler=new Handler() {
        public void handleMessage(Message msg)
        {
        	
        	if(msg.arg1==1){
        		String pname="123";
    			String pgrade="123";
    			String pacad="123";
    			String pmajor="123";
    			String user="123";
    			try {
    				JSONObject jo=new JSONObject(msg.obj.toString());
    				pname=jo.getString("name");
    				pgrade=jo.getString("grade");
    				pacad=jo.getString("acad");
    				pmajor=jo.getString("major");
    				user=jo.getString("user");
    				String response="µ«¬Ω≥…π¶";
    				Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
    				Intent i=new Intent(MainActivity.this,PersonMainActivity.class);
    				i.putExtra("username", user);
    				i.putExtra("name", pname);
    				i.putExtra("acad", pacad);
    				i.putExtra("grade", pgrade);
    				i.putExtra("major", pmajor);
    				startActivity(i);
    				
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}else{
        		String response="’À∫≈ªÚ√‹¬Î¥ÌŒÛ";
        		Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                
        	}
        	
			
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button Ltn=(Button)findViewById(R.id.login);
		Button Rtn=(Button)findViewById(R.id.register);
		
		final EditText usernameL=(EditText)findViewById(R.id.ET_username);
		final EditText passwordL=(EditText)findViewById(R.id.ET_password);
		
		Ltn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String username=usernameL.getText().toString();
				final String password=passwordL.getText().toString();
				
				if(username.equals("jerry")&&password.equals("123456")){
					Intent i=new Intent(MainActivity.this,AdmiMainActivity.class);
    				startActivity(i);
				}
				else SendByHttpClient(username,password);
			}
		});
		
		Rtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this,RegisterMainActivity.class);
				startActivity(i);
				
				
			}
		});
	}
	
	public void SendByHttpClient(final String name,final String password){
		new Thread(new Runnable(){
			public void run(){
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"LoginServlet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username",name));
					params.add(new BasicNameValuePair("password",password));
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
		
					int code=httpResponse.getStatusLine().getStatusCode();
					if(code==200){
						
						HttpEntity entityP=httpResponse.getEntity();
					
						String mStrResult=EntityUtils.toString(entityP);
						if(mStrResult.equals("error")){
							msg.arg1=0;
							handler.sendMessage(msg);
						}else{
							URLDecoder.decode(mStrResult,"utf-8");
							JSONObject result=new JSONObject(mStrResult);
							
							
							msg.obj=result;
							msg.arg1=1;
							handler.sendMessage(msg);
						}
						
					}
					
				}catch(Exception e){
					e.printStackTrace();
					System.out.print("fff");
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
