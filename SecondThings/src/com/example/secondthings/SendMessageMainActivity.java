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

import com.example.secondthings.PersonMainActivity.SubmitThread;

import Normal.INFO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SendMessageMainActivity extends Activity implements View.OnClickListener{

	private static Bitmap addPhoto;
	private String sendPicture;
	private String username,name,head;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private LayoutInflater inflater;
	private View layout;
	private TextView takePhoto;
	private TextView choosePhoto;
	private TextView cancel;
	
	
	private EditText titileET;
	private EditText MessageET;
	private ImageView photoIV;
	private Button sendBtn;
	
	private static String path="/sdcard/DemoHead/";
	
	
	public Handler handler=new Handler(){
		public void handleMessage(Message msg){
			if(msg.obj.equals("insert successful")){
				Toast.makeText(SendMessageMainActivity.this, "插入成功", Toast.LENGTH_LONG).show();
				Intent i=new Intent(SendMessageMainActivity.this,FormMainActivity.class);
				i.putExtra("username", username);
				i.putExtra("name", name);
				i.putExtra("head", head);
				startActivity(i);
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message_main);
		
		init();
		
		photoIV.setOnClickListener(new OnClickListener() {

	          @Override
	          public void onClick(View v) {
	        	  viewInit();
	          }
	      });		
		
		sendBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title=titileET.getText().toString();
				String message=MessageET.getText().toString();
				
				 
				
				SendByHttpClient(username,name,head,title,message,sendPicture);
				
			}
		});
	}

	public void SendByHttpClient(final String username,final String name,final String head,final String title,final String message,final String photo ){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost(INFO.url+"/SendSerlvet");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username",username));
					params.add(new BasicNameValuePair("name",name));
					params.add(new BasicNameValuePair("head",head));
					params.add(new BasicNameValuePair("title",title));
					params.add(new BasicNameValuePair("message",message));
					params.add(new BasicNameValuePair("photo",photo));

					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"UTF-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse=httpclient.execute(httpPost);
					int code=httpResponse.getStatusLine().getStatusCode();
					if(code==200)
					{
						HttpEntity entity1=httpResponse.getEntity();
						String response=EntityUtils.toString(entity1,"utf-8");
						Message message=new Message();
						message.what=1;
						message.obj=response;
						handler.sendMessage(message);
						
					}
					
				}catch(Exception e){
					e.printStackTrace();
					
				}
			}
		}).start();
	}
	
public void viewInit(){
		
		
		builder = new AlertDialog.Builder(this);//创建对话框
	    inflater = getLayoutInflater();
	    layout = inflater.inflate(R.layout.activity_dialog_select, null);//获取自定义布局
	    builder.setView(layout);//设置对话框的布局
	    dialog = builder.create();//生成最终的对话框
	    
	    dialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭
	    dialog.show();//显示对话框
	    
	    takePhoto = (TextView) layout.findViewById(R.id.photograph);
	    choosePhoto = (TextView) layout.findViewById(R.id.photo);
	    cancel = (TextView) layout.findViewById(R.id.cancel);
	    //设置监听
	    
	    takePhoto.setOnClickListener(this);
	    choosePhoto.setOnClickListener(this);
	    cancel.setOnClickListener(this);

	    
		
	}
	
	@Override
	public void onClick(View view){
		switch(view.getId()){
		
		case R.id.photograph:
			try{
				Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        "head.jpg")));
				startActivityForResult(intent2,2);
			}catch(Exception e){
				Toast.makeText(SendMessageMainActivity.this, "相机无法启动，请先开启相机权限", Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
			break;
		 case R.id.photo://从相册里面取照片
             Intent intent1 = new Intent(Intent.ACTION_PICK, null);//返回被选中项的URI
             intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//得到所有图片的URI
             startActivityForResult(intent1, 1);
             dialog.dismiss();
             break;
         case R.id.cancel:
             dialog.dismiss();//关闭对话框
             break;
         default:break;
			
		}
	 
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //从相册里面取相片的返回结果
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
 
                break;
            //相机拍照后的返回结果
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));//裁剪图片
                }
 
                break;
            //调用系统裁剪图片后
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    addPhoto = extras.getParcelable("data");
                    if (addPhoto != null) {
                        /**
                         	* 上传服务器代码
                         */
                    	edit_headPicture(addPhoto);
                        setPicToView(addPhoto);//保存在SD卡中
                        photoIV.setImageBitmap(addPhoto);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;
 
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
 
    //转换图片
    private void edit_headPicture(Bitmap bitmap){
    	ByteArrayOutputStream baos=new ByteArrayOutputStream();
    	bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
    	sendPicture=Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    	int n=sendPicture.length();   	   	
    }
    
    

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //找到指定URI对应的资源图片
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是裁剪框宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        //进入系统裁剪图片的界面
        startActivityForResult(intent, 3);
        
    }
    
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd卡是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建以此File对象为名（path）的文件夹
        String fileName = path + "head.jpg";//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件（compress：压缩）
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
 
        }
    }
	
    
	public void init(){
		titileET=(EditText)findViewById(R.id.form_titleET);
		MessageET=(EditText)findViewById(R.id.form_MessageET);
		photoIV=(ImageView)findViewById(R.id.form_addIV);
		sendBtn=(Button)findViewById(R.id.form_SendMessageBtn);
		Intent intent = getIntent();
		username=intent.getStringExtra("username");
		 name=intent.getStringExtra("name");
		 head=intent.getStringExtra("head");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_message_main, menu);
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
