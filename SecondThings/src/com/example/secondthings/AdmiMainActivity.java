package com.example.secondthings;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import Normal.INFO;
import Tools.Black;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class AdmiMainActivity extends Activity {
	
	private List<Black> list;
	
	private ListView blackLV;
	
	private ImageView addIV;
	
	private MyAdapter adapter;
	
	private EditText nameAddET;
	public Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			
			case 1:
				try{
					adapter = new MyAdapter();
					blackLV.setAdapter(adapter);// ��ListView���������(�Զ�������������Ŀ)
				}catch(Exception e){
					e.printStackTrace();
				}
				break;
				
			case 2:
				list.clear();
				queryAll();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admi_main);
		blackLV=(ListView) findViewById(R.id.AdmiList);
		list=new ArrayList<Black>();
		addIV=(ImageView) findViewById(R.id.blackAdd_IV);
		nameAddET=(EditText) findViewById(R.id.BlackName_ET);
		queryAll();
		
		addIV.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final String username=nameAddET.getText().toString();
				insertBlack(username);
			}
      });
	}
	
	public void insertBlack(final String username){
		new Thread(new Runnable(){
			public void run(){
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/SqlSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("code","blackinsert"));
					params.add(new BasicNameValuePair("username",username));
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
					int code=httpResponse.getStatusLine().getStatusCode();
					
					if(code==200){
						Message msg=new Message();
						HttpEntity entityI=httpResponse.getEntity();
						String Result=EntityUtils.toString(entityI);
						msg.what=2;
						handler.sendMessage(msg);
						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void queryAll(){
		new Thread(new Runnable(){
			public void run(){
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/SqlSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("code","black"));
					
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
					int code=httpResponse.getStatusLine().getStatusCode();
					
					if(code==200){
						Message msg=new Message();
						HttpEntity entityI=httpResponse.getEntity();
						String Result=EntityUtils.toString(entityI);
						JSONArray jsonArray=new JSONArray(Result);
						for(int i=0;i<jsonArray.length();i++){
							JSONObject jsonObject=jsonArray.getJSONObject(i);
							int id=jsonObject.getInt("id");
							String username=jsonObject.getString("username");
							Black acc=new Black(id,username);
							
							
							list.add(acc);
							
						}
						msg.what=1;
						handler.sendMessage(msg);
						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void deletblack(final String username){
		new Thread(new Runnable(){
			public void run(){
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/SqlSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("code","blackdelete"));
					params.add(new BasicNameValuePair("username",username));
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
					int code=httpResponse.getStatusLine().getStatusCode();
					
					if(code==200){
						Message msg=new Message();
						HttpEntity entityI=httpResponse.getEntity();
						String Result=EntityUtils.toString(entityI);
						
						msg.what=2;
						handler.sendMessage(msg);
						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	private class MyAdapter extends BaseAdapter {
		public int getCount() {                   // ��ȡ��Ŀ����
			return list.size();
		}

		public Object getItem(int position) { // ����λ�û�ȡ����
			return list.get(position);
		}

		public long getItemId(int position) { // ����λ�û�ȡid
			return position;
		}

        // ��ȡһ����Ŀ��ͼ
		public View getView(int position, View convertView, ViewGroup parent) { 
             // ����convertView
			View item = convertView != null ? convertView : View.inflate(
					getApplicationContext(), R.layout.black_item, null);
           // ��ȡ����ͼ�е�TextView		
        ImageView delIV=(ImageView) item.findViewById(R.id.blackDel_Btn);	
		TextView nameTV = (TextView) item.findViewById(R.id.blackName_TV);
		
          // ���ݵ�ǰλ�û�ȡAccount����
          final Black a = list.get(position); 
          // ��Account�����е����ݷŵ�TextView��
          //String remind2=a.getHead();
          
          
          final String username=a.getUsername();
          int id=a.getId();
          
          
          
          
		 
		 nameTV.setText(username);
		 

		 
		 delIV.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
                    //ɾ������֮ǰ���ȵ���һ���Ի���
					android.content.DialogInterface.OnClickListener listener = 
							new android.content.DialogInterface.
                        OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							deletblack(username);
						}
					};
					Builder builder = new Builder(AdmiMainActivity.this); // �����Ի���
					builder.setTitle("ȷ��Ҫɾ����?");                    // ���ñ���
                     // ����ȷ����ť���ı��Լ�������
					builder.setPositiveButton("ȷ��", listener);	
				     builder.setNegativeButton("ȡ��", null);         // ����ȡ����ť
					builder.show(); // ��ʾ�Ի���
				}
			});
		 
		 
			return item;
		}
	}
         
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admi_main, menu);
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
