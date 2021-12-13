package com.example.secondthings;

import java.io.ByteArrayOutputStream;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import Normal.INFO;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import Tools.Change;

public class ChangeMainActivity extends Activity {
	
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private LayoutInflater inflater;
	private View layout;
	
	private ImageView change_addIV;
	private ListView change_List;
	
	private Button change_forumBtn;
	private Button change_personBtn;

	
	private Button change_sendBtn;
	private Button change_reciveBtn;
	
	
	private List<Change> list;
	
	private SendAdapter adapter;
	private ReceiveAdapter receiveAdapter;
	
	private String username,head,name;
	
	
	
	
	public static final int SEND=1;
	public static final int RECEIVE=2;
	
	public static final int READYES=3;
	public static final int READNO=4;
	
	public static final int person=5;
	
	public static final int UNREAD=7;
	public static final int PLEASE=6;
	
	public static final int REFUSE=8;

	public static final int BLACK=9;
	public int dlgCode;
	public int code;
	public Handler handler=new Handler(){

		public void handleMessage(final Message msg){
			switch(msg.what){
			
			case person:
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
    				Intent i=new Intent(ChangeMainActivity.this,PersonMainActivity.class);
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
				break;
				
			case SEND:
				adapter = new SendAdapter();
				change_List.setAdapter(adapter);
				
				break;
			case RECEIVE:
				receiveAdapter = new ReceiveAdapter();
				change_List.setAdapter(receiveAdapter);
				break;
				
			case BLACK:
				AlertDialog.Builder builder=new Builder(ChangeMainActivity.this);
				builder.setMessage("对方为黑名单用户");
				builder.setTitle("警告");
				
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
					}																			
				});
				builder.create().show();;				
			
				break;
			
			case UNREAD:
				if(dlgCode==UNREAD){
					
					String res=msg.obj.toString();
					
					
					
					try {
						JSONObject jsonObject=new JSONObject(res);
						
						final String idS=jsonObject.getString("id");
						final String mess = jsonObject.getString("message");
						
						AlertDialog.Builder builder1=new Builder(ChangeMainActivity.this);
						builder1.setMessage(mess);
						builder1.setTitle("交易信息");
						
						builder1.setPositiveButton("同意", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();							
								updateRead("同意",idS,mess);							
							}						
						});
						
						
						builder1.setNegativeButton("拒绝", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();							
								updateRead("拒绝",idS,mess);																				
							}																
						});
						
						builder1.create().show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				if(dlgCode==PLEASE){}
				
				break;
			
			}
			
		}
	};

	private class ReceiveAdapter extends BaseAdapter {
		public int getCount() {                   // 获取条目总数
			return list.size();
		}

		public Object getItem(int position) { // 根据位置获取对象
			return list.get(position);
		}

		public long getItemId(int position) { // 根据位置获取id
			return position;
		}

        // 获取一个条目视图
		public View getView(int position, View convertView, ViewGroup parent) { 
             // 重用convertView
			View item = convertView != null ? convertView : View.inflate(
					getApplicationContext(), R.layout.change_item_receive, null);
           // 获取该视图中的TextView		
			
          // 根据当前位置获取Account对象
          final Change a = list.get(position); 
          // 把Account对象中的数据放到TextView中
          //String remind2=a.getHead();
          
          TextView sendnameTV=(TextView) item.findViewById(R.id.receive_itemNameTV);
          TextView messageTV=(TextView) item.findViewById(R.id.receive_itemMessageTV);
          TextView readTV=(TextView) item.findViewById(R.id.receive_itemReadCodeTV);
          
          String receiveName=a.getName();
          String mess=a.getMessage();
          int readCode=a.getReadcode();
          sendnameTV.setText(receiveName);
          messageTV.setText(mess);
          if(readCode==READYES){
        	  readTV.setText("已同意");
          }else if(a.getReadcode()==READNO){
        	  readTV.setText("已拒绝");
          }
          
			return item;
		}
	}
	
	
	private class SendAdapter extends BaseAdapter {
		public int getCount() {                   // 获取条目总数
			return list.size();
		}

		public Object getItem(int position) { // 根据位置获取对象
			return list.get(position);
		}

		public long getItemId(int position) { // 根据位置获取id
			return position;
		}

        // 获取一个条目视图
		public View getView(int position, View convertView, ViewGroup parent) { 
             // 重用convertView
			View item = convertView != null ? convertView : View.inflate(
					getApplicationContext(), R.layout.change_item, null);
           // 获取该视图中的TextView		
			
          // 根据当前位置获取Account对象
          final Change a = list.get(position); 
          // 把Account对象中的数据放到TextView中
          //String remind2=a.getHead();
          
          TextView receivenameTV=(TextView) item.findViewById(R.id.change_itemNameTV);
          TextView messageTV=(TextView) item.findViewById(R.id.change_itemMessageTV);
          TextView readTV=(TextView) item.findViewById(R.id.change_itemReadTV);
          
          String receiveName=a.getName();
          String mess=a.getMessage();
          int readCode=a.getReadcode();
          receivenameTV.setText(receiveName);
          messageTV.setText(mess);
          if(readCode==READYES){
        	  readTV.setText("已同意");
          }else if(a.getReadcode()==READNO){
        	  readTV.setText("已拒绝");
          }
          
			return item;
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_main);
		
		code=SEND;
		dlgCode=0;
		
		Intent i=getIntent();
		username=i.getStringExtra("username");
		head=i.getStringExtra("head");
		name=i.getStringExtra("name");
		change_addIV=(ImageView)findViewById(R.id.change_addIV);
        change_List=(ListView)findViewById(R.id.change_List);
        
        change_forumBtn=(Button)findViewById(R.id.change_forumBtn);
        change_personBtn=(Button)findViewById(R.id.change_personBtn);
        
        change_sendBtn=(Button)findViewById(R.id.change_sendBtn);
        change_reciveBtn=(Button)findViewById(R.id.change_reciveBtn);
        
        change_List.setOnItemClickListener(new MyOnItemClickListener());
        
        list=new ArrayList<Change>();
       queryAllSend();
       
       
       change_personBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				person(username);
			}
     });
       
       change_forumBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i=new Intent(ChangeMainActivity.this,FormMainActivity.class);
				i.putExtra("username", username);
				i.putExtra("name", name);
				i.putExtra("head", head);
				startActivity(i);
			}
      });
       
       
       change_addIV.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				AlertDialog.Builder builder=new Builder(ChangeMainActivity.this);//创建对话框
			    inflater = getLayoutInflater();
			    layout = inflater.inflate(R.layout.activity_dialog_transaction, null);//获取自定义布局
			    builder.setView(layout);//设置对话框的布局
			    builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EditText change_dialogUsernameET=(EditText) layout.findViewById(R.id.change_dialogUsernameET);
						EditText change_dialogMessET=(EditText) layout.findViewById(R.id.chang_dialogMessage);
						
						String dlgUsername=change_dialogUsernameET.getText().toString().trim();
						String dlgMess=change_dialogMessET.getText().toString().trim();
						
						insertMessage(username,dlgUsername,dlgMess);
					}
				});
			    dialog = builder.create();
			    dialog.show();//生成最终的对话框
			    
			}
      });
       
       change_sendBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				code=SEND;
				list.clear();
				queryAllSend();
			}
       });
       
       change_reciveBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				code=RECEIVE;
				list.clear();
				queryAllReceive();
			}
      });
       
	}
	
	public void person(final String username){
		new Thread(new Runnable(){
			public void run(){			
				try{
					Message msg=new Message();
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/PersonSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();					
					params.add(new BasicNameValuePair("username",username));
					
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
							msg.what=person;
							handler.sendMessage(msg);
						}				
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	

	protected void insertMessage(final String sendname,final String receivename,final String message) {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"ChangeSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("code","insert"));
					params.add(new BasicNameValuePair("sendname",sendname));
					params.add(new BasicNameValuePair("receivename",receivename));
					params.add(new BasicNameValuePair("message",message));
					
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"UTF-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
					int code=httpResponse.getStatusLine().getStatusCode();
					if(code==200)
					{
						HttpEntity entity1=httpResponse.getEntity();
						String response=EntityUtils.toString(entity1,"utf-8");
						Message message=new Message();
						
						message.obj=response;
						if(response.toString().equals("black")) message.what=BLACK;
						handler.sendMessage(message);
						list.clear();
						queryAllSend();
						
					}
					
				}catch(Exception e){
					e.printStackTrace();
					
				}
			}
		}).start();
	}


	protected void queryAllReceive() {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){
			public void run(){			
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/ChangeSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();					
					params.add(new BasicNameValuePair("code","receive"));
					params.add(new BasicNameValuePair("receivename",username));
					
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
							String name=jsonObject.getString("sendname");
							String mess=jsonObject.getString("message");
							int readcode=jsonObject.getInt("readcode");
							
							Change acc=new Change(id,name,mess,readcode);							
		
							list.add(acc);
							
						}
						msg.what=RECEIVE;
						handler.sendMessage(msg);						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}


	private void queryAllSend() {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){
			public void run(){			
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/ChangeSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();					
					params.add(new BasicNameValuePair("code","send"));
					params.add(new BasicNameValuePair("sendname",username));
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
							String name=jsonObject.getString("receivename");
							String mess=jsonObject.getString("message");
							int readcode=jsonObject.getInt("readcode");
							
							Change acc=new Change(id,name,mess,readcode);							
		
							list.add(acc);
							
						}
						msg.what=SEND;
						handler.sendMessage(msg);						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void updateRead(final String string, final String idS,final String mess) {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){
			public void run(){			
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/ChangeSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();					
					params.add(new BasicNameValuePair("id",idS));
					if(string.equals("selectRead")){
						params.add(new BasicNameValuePair("code","selectRead"));
					}
					else{
						params.add(new BasicNameValuePair("code","updateRead"));
						if(string.equals("同意")){
							params.add(new BasicNameValuePair("readcode","3"));
						}
						if(string.equals("拒绝")){
							params.add(new BasicNameValuePair("readcode","4"));
						}
					}
					
					params.add(new BasicNameValuePair("sendname",username));
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
					int code=httpResponse.getStatusLine().getStatusCode();
					
					if(code==200){
						Message msg=new Message();
						JSONObject jsonObject=new JSONObject();
						jsonObject.put("id", idS);
						jsonObject.put("message", mess);
						msg.obj=jsonObject;
						msg.what=UNREAD;
						HttpEntity entityI=httpResponse.getEntity();
						String Result=EntityUtils.toString(entityI);
						if(Result.equals("待查看")){
							dlgCode=UNREAD;
						}else if(Result.equals("可申诉")){
							dlgCode=PLEASE;
						}else if(Result.equals("已拒绝")){
							dlgCode=REFUSE;
						}
						handler.sendMessage(msg);
						list.clear();
						queryAllReceive();						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private class MyOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
          // 获取点击位置上的数据
			if(code==SEND){
				final Change a = (Change) parent.getItemAtPosition(position);	
				
				AlertDialog.Builder builder=new Builder(ChangeMainActivity.this);
				builder.setMessage(a.getMessage());
				builder.setTitle("交易信息");
				
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
					}																			
				});
				builder.create().show();;				
			}
			else if(code==RECEIVE){
				final Change a = (Change) parent.getItemAtPosition(position);	
				
				int idDlg=a.getId();
				
				final String idS=Integer.toString(idDlg);
				String mess=a.getMessage();
				updateRead("selectRead",idS,mess);
				
				
				
			}
		
		}
	}
	
	
	public static Bitmap stringToBitmap(String string) {
	    Bitmap bitmap = null;
	    try {
	        byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
	        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return bitmap;
	}

	
	public static String bitmapToString(Bitmap bitmap) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
	    byte[] imgBytes = baos.toByteArray();// 转为byte数组
	    return Base64.encodeToString(imgBytes, Base64.DEFAULT);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_main, menu);
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
