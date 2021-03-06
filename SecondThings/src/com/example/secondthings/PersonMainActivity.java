package com.example.secondthings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonMainActivity extends Activity implements View.OnClickListener {

	private Button muBtn_form;
	private Button muBtn_change;
	private ImageView iv_icon;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private LayoutInflater inflater;
	private View layout;
	private TextView takePhoto;
	private TextView choosePhoto;
	private TextView cancel;
	private static String path="/sdcard/DemoHead/";
	private static Bitmap head;
	private String headPicture;
	String username;
	String name;
	String headStr;
	public static final int Change_Photo=1;
	public static final int Get_Photo=2;
	
	public Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case Get_Photo:
				iv_icon=(ImageView)findViewById(R.id.iv_head);
				String response2=(String)msg.obj;
				if(response2.equals("????????")){
					
				}else{
					head=stringToBitmap(response2);
					iv_icon.setImageBitmap(head);
					headStr=response2;
				}
				
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_main);
		init();
		iv_icon=(ImageView)findViewById(R.id.iv_head);
		
		
		iv_icon.setOnClickListener(new OnClickListener() {

	          @Override
	          public void onClick(View v) {
	          	viewInit();
	          }
	      });	
		
		
	}
	
	public void viewInit(){
		
		
		
		builder = new AlertDialog.Builder(this);//??????????
	    inflater = getLayoutInflater();
	    layout = inflater.inflate(R.layout.activity_dialog_select, null);//??????????????
	    builder.setView(layout);//????????????????
	    dialog = builder.create();//????????????????
	    
	    dialog.setCanceledOnTouchOutside(true);//????????Dialog????????????????
	    dialog.show();//??????????
	    
	    takePhoto = (TextView) layout.findViewById(R.id.photograph);
	    choosePhoto = (TextView) layout.findViewById(R.id.photo);
	    cancel = (TextView) layout.findViewById(R.id.cancel);
	    //????????
	    
	    takePhoto.setOnClickListener(this);
	    choosePhoto.setOnClickListener(this);
	    cancel.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.muBtn_change:
            Intent i = new Intent(PersonMainActivity.this,ChangeMainActivity.class);
            i.putExtra("username", username);
            i.putExtra("name", name);
            i.putExtra("head", headStr);
            startActivity(i);
            break;
		case R.id.muBtn_form:
            Intent intent = new Intent(PersonMainActivity.this,FormMainActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("name", name);
            intent.putExtra("head", headStr);
            startActivity(intent);
            break;
		case R.id.photograph:
			try{
				Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        "head.jpg")));
				startActivityForResult(intent2,2);
			}catch(Exception e){
				Toast.makeText(PersonMainActivity.this, "??????????????????????????????", Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
			break;
		 case R.id.photo://????????????????
             Intent intent1 = new Intent(Intent.ACTION_PICK, null);//??????????????URI
             intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//??????????????URI
             startActivityForResult(intent1, 1);
             dialog.dismiss();
             break;
         case R.id.cancel:
             dialog.dismiss();//??????????
             break;
         default:break;
			
		}
	 
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //??????????????????????????
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//????????
                }
 
                break;
            //????????????????????
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));//????????
                }
 
                break;
            //??????????????????
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         	* ??????????????
                         */
                    	edit_headPicture(head);
                        setPicToView(head);//??????SD????
                        iv_icon.setImageBitmap(head);//??ImageView????????
                    }
                }
                break;
            default:
                break;
 
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
 
    //????????
    private void edit_headPicture(Bitmap bitmap){
    	ByteArrayOutputStream baos=new ByteArrayOutputStream();
    	bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
    	headPicture=Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    	int n=headPicture.length();
    	Thread submit=new Thread(new SubmitThread());
    	submit.start();
    }
    
    class SubmitThread implements Runnable
    {
    	
		final String user=username;
    	Message msg=handler.obtainMessage();
    	@Override
    	public void run(){
    		
    		try{
    			
    			Server(headPicture,user);
    			//handler.sendMessage(msg);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
    public void Server(String headPicture,String user){
    	
		try {
			
	    	HttpClient httpClient=new DefaultHttpClient();
	    	HttpPost httpPost=new HttpPost(INFO.url+"/ChangeSerlvet");
	    	List<NameValuePair> params=new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("head",headPicture));
	    	params.add(new BasicNameValuePair("username",user));
	    	params.add(new BasicNameValuePair("code","set"));
	    	final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"UTF-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse=httpClient.execute(httpPost);
			int code=httpResponse.getStatusLine().getStatusCode();
			if(code==200)
			{
				HttpEntity entity1=httpResponse.getEntity();
				String response=EntityUtils.toString(entity1,"utf-8");
				Message message=new Message();
				message.what=Change_Photo;
				message.obj=response;
				handler.sendMessage(message);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //????????URI??????????????
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY ??????????????????
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY ??????????????
        intent.putExtra("outputX", 150);// ????????????
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        //??????????????????????
        startActivityForResult(intent, 3);
        
    }
    
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ????sd??????????
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// ????????File??????????path??????????
        String fileName = path + "head.jpg";//????????
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ????????????????compress????????
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //??????
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
 
        }
    }
	

	public void init(){
		
		muBtn_form=(Button)findViewById(R.id.muBtn_form);
		muBtn_change=(Button)findViewById(R.id.muBtn_change);
		
		Intent intent=getIntent();		
		 username=intent.getStringExtra("username");
		 name=intent.getStringExtra("name");
		String grade=intent.getStringExtra("grade");
		String major=intent.getStringExtra("major");
		String acad=intent.getStringExtra("acad");
		
		TextView p_name=(TextView)findViewById(R.id.p_name);
		TextView p_grade=(TextView)findViewById(R.id.p_grade);
		TextView p_major=(TextView)findViewById(R.id.p_major);
		TextView p_acad=(TextView)findViewById(R.id.p_acad);
		
		p_name.setText(name);
		p_grade.setText(grade);
		p_major.setText(major);
		p_acad.setText(acad);
		
		final String user=username;
		getHeadByHttp(user);
		

	    muBtn_form.setOnClickListener(this);
	    muBtn_change.setOnClickListener(this);
	}
	
	public void getHeadByHttp(final String uer){
		new Thread(new Runnable(){
			
			public void run(){
				String code="get";
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/HeadSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username",uer));
					params.add(new BasicNameValuePair("code",code));
					
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"UTF-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
					int code1=httpResponse.getStatusLine().getStatusCode();
					if(code1==200){
						HttpEntity entity1=httpResponse.getEntity();
						String response=EntityUtils.toString(entity1,"utf-8");
						Message message=new Message();
						message.what=Get_Photo;
						message.obj=response;
						handler.sendMessage(message);
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
			}
		}).start();
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
		getMenuInflater().inflate(R.menu.person_main, menu);
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
