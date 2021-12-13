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
import Tools.Account;
import android.app.Activity;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FormMainActivity extends Activity {

	// 需要适配的数据集合
			private List<Account> list;
			// 数据库增删改查操作类
			
			// 输入姓名的EditText
			private TextView nameET;
			// 输入金额的EditText
			private EditText msgET;
			private EditText titleET;
			private ImageView headIV;
			private ImageView photoIV;
			// 适配器
			private MyAdapter adapter;
			// ListView
			private ListView accountLV;
			private ImageView addIV;
			
			private String username,name,head;
			
			
			private int option;
			
			public static final int person=1;
			public static final int All=2;
			
			
			public Handler handler=new Handler(){
				public void handleMessage(Message msg){
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
		    				Intent i=new Intent(FormMainActivity.this,PersonMainActivity.class);
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
					case All:
						try{
							adapter = new MyAdapter();
							accountLV.setAdapter(adapter);// 给ListView添加适配器(自动把数据生成条目)
						}catch(Exception e){
							e.printStackTrace();
						}
						break;
					}
				}
			};
			
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_form_main);
			
			  //初始化控件
			option=0;
			initView();
			list=new ArrayList<Account>();
			queryAll();
					
			Intent intent=getIntent();
			 username=intent.getStringExtra("username");
			 name=intent.getStringExtra("name");
			 head=intent.getStringExtra("head");
			addIV=(ImageView)findViewById(R.id.addIV);
			
			
			Button form_personBtn=(Button)findViewById(R.id.form_personBtn);
			form_personBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					person(username);
					
				}
	      });
			
			Button form_changeBtn=(Button)findViewById(R.id.form_changeBtn);
			form_changeBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i=new Intent(FormMainActivity.this,ChangeMainActivity.class);
					i.putExtra("username", username);
					i.putExtra("head", head);
					i.putExtra("name", name);
					startActivity(i);
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

		public void queryAll(){
			new Thread(new Runnable(){
				public void run(){			
					try{
						HttpClient httpclient=new DefaultHttpClient();
						HttpPost httpPost=new HttpPost(INFO.url+"/SqlSerlvet");
						List<NameValuePair> params=new ArrayList<NameValuePair>();					
						params.add(new BasicNameValuePair("code","all"));
						
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
								String name=jsonObject.getString("name");
								String head=jsonObject.getString("head");
								String title=jsonObject.getString("title");
								String mess=jsonObject.getString("message");
								String photo=jsonObject.getString("photo");
								Account acc=new Account(id,name,head,title,mess,photo);
								
								int code222=0;
								list.add(acc);
								System.out.print("list:");
								System.out.print(list.toString());
							}
							msg.what=All;
							handler.sendMessage(msg);						
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		}
		public void insert(Account account) {
			
			final String name=account.getName();
			final String head=account.getHead();
			final String title=account.getTitle();
			final String message=account.getMessgae();
			final String photo=account.getPhoto();
			new Thread(new Runnable(){
				public void run(){
					try{
						HttpClient httpclient=new DefaultHttpClient();
						HttpPost httpPost=new HttpPost(INFO.url+"/SqlSerlvet");
						List<NameValuePair> params=new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("name",name));
						params.add(new BasicNameValuePair("head",head));
						params.add(new BasicNameValuePair("title",title));
						params.add(new BasicNameValuePair("message",message));
						params.add(new BasicNameValuePair("photo",photo));
						params.add(new BasicNameValuePair("code","insert"));
						
						final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
						httpPost.setEntity(entity);
						HttpResponse httpResponse=httpclient.execute(httpPost);
						int code=httpResponse.getStatusLine().getStatusCode();
						
						if(code==200){
							Message msg=new Message();
							HttpEntity entityI=httpResponse.getEntity();
							String Result=EntityUtils.toString(entityI);
							if(Result.equals("successful")){							
								msg.what=3;							
							}else msg.what=0;
							handler.sendMessage(msg);						
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					
					
				}
			}).start();
		}
		// 初始化控件
		
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

		/**
		 * 图片转换成base64字符串
		 *
		 * @param bitmap
		 * @return
		 */
		public static String bitmapToString(Bitmap bitmap) {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		    byte[] imgBytes = baos.toByteArray();// 转为byte数组
		    return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		}

		private void initView() {
			accountLV = (ListView) findViewById(R.id.accountLV);

			// 添加监听器, 监听条目点击事件
			accountLV.setOnItemClickListener(new MyOnItemClickListener());
		}
	     //activity_mian.xml 对应ImageView的点击事件触发的方法
		public void add(View v) {
			
			Intent intent=new Intent(FormMainActivity.this,SendMessageMainActivity.class);
			intent.putExtra("username", username);
            intent.putExtra("name", name);
            intent.putExtra("head", head);
			startActivity(intent);
			
			/*String name = "jerry";
			String message = msgET.getText().toString().trim();
			String title=titleET.getText().toString().trim();
			
	          //三目运算 balance.equals(“”) 则等于0 
	          //如果balance 不是空字符串 则进行类型转换
			Account a = new Account(name, balance);
			dao.insert(a);                      // 插入数据库
			list.add(a);                        // 插入集合
			adapter.notifyDataSetChanged(); // 刷新界面
	         // 选中最后一个
			accountLV.setSelection(accountLV.getCount() - 1); 
			nameET.setText("");
			balanceET.setText("");*/
		}	
		// 自定义一个适配器(把数据装到ListView的工具)
		private class MyAdapter extends BaseAdapter {
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
						getApplicationContext(), R.layout.item, null);
	           // 获取该视图中的TextView		
	        ImageView headIV=(ImageView) item.findViewById(R.id.itHeadIV);	
			TextView nameTV = (TextView) item.findViewById(R.id.itnameTV);
			TextView titleTV = (TextView) item.findViewById(R.id.itTitleTv);
			TextView msgTV=(TextView) item.findViewById(R.id.itMessageTV);
			ImageView photoIV=(ImageView) item.findViewById(R.id.itPhotoIV);
	          // 根据当前位置获取Account对象
	          final Account a = list.get(position); 
	          // 把Account对象中的数据放到TextView中
	          //String remind2=a.getHead();
	          
	          String head=a.getHead();
	          String name=a.getName();
	          String title=a.getTitle();
	          String msg=a.getMessgae();
	          String photo=a.getPhoto();
	          
	          Bitmap headBitmap=stringToBitmap(head);
	          Bitmap photoBitmap=stringToBitmap(photo);
	          
			 headIV.setImageBitmap(headBitmap);
			 photoIV.setImageBitmap(photoBitmap);
			 nameTV.setText(name);
			 titleTV.setText(title);
			 msgTV.setText(msg);

				return item;
			}
		}
	         //ListView的Item点击事件
		private class MyOnItemClickListener implements OnItemClickListener {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
	          // 获取点击位置上的数据
			Account a = (Account) parent.getItemAtPosition(position);	
			Intent intent=new Intent(FormMainActivity.this,MessageMainActivity.class);
			intent.putExtra("username", username);
			intent.putExtra("id", a.getId());
			intent.putExtra("title", a.getTitle());
			intent.putExtra("name", a.getName());
			intent.putExtra("head", a.getHead());
			intent.putExtra("message",a.getMessgae());
			intent.putExtra("photo", a.getPhoto());
			intent.putExtra("userHead", head);
			intent.putExtra("usrname", name);
			startActivity(intent);
			
			}
		}
}
