package com.specher.modplayer;
/**
 * 
 * 加入显示SD卡目录列表
 * 
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.PagerAdapter;  

@SuppressLint("NewApi")
public class MainActivity extends Activity {
private	Button btn_play,btn_stop,btn_open;
private	CheckBox ck_loop;
private	ListView list,list1,list2;
private	Player player ;
private	Module module;
private ViewPager viewPager,viewPager1;  //对应的viewPager  
private List<View> viewList,viewList1;//view数组  

private View view1, view2, view3,view4,view5,view6;  
//	IBXM ibxm;
private	EditText edittext,edittext1,edittext2;
//	SeekBar seekbar;
private	ScrollView scrollView;
private View root;
private	boolean interpolation = false;
String filepath;
	  private String[] mListTitle = {"skid_row_portal2luncher.xm","dalezy_billie_jean.xm", "dalezy_porncop.xm", "dualtrax_intr2.xm", "dualtrax_orion.xm"
	    		,"emax_delicate.mod", "fourmat_twinbee.xm" ,"lavaburn_skimmed_milk.xm", "moh_quick_chippie_2495.xm"
	    		, "paledeth_premature_ejaculation.xm", "quazar_funky_stars.xm", "razor1911.xm",  "zalza_isglass.xm"};
	    private int[] mListStr = {R.raw.skid_row_portal2luncher,R.raw.dalezy_billie_jean,R.raw.dalezy_porncop, R.raw.dualtrax_intr2, R.raw.dualtrax_orion, R.raw.emax_delicate, R.raw.fourmat_twinbee,
	    		R.raw.lavaburn_skimmed_milk, R.raw.moh_quick_chippie_2495, R.raw.paledeth_premature_ejaculation, R.raw.quazar_funky_stars, R.raw.razor1911, R.raw.zalza_isglass};
	  ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();
	private String PATH;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_main);
	
	SharedPreferences sp= 	PreferenceManager.getDefaultSharedPreferences(this);
	
	
		setColor(MainActivity.this,Color.parseColor(sp.getString("barcolor", "#4CAF50")));//Color.rgb( 56, 142, 60)
		changeActionbarSkinMode(getActionBar(),Color.parseColor(sp.getString("barcolor", "#4CAF50")));
		filepath=sp.getString("filepath", "ModPlayer");
		root = findViewById(R.id.rootlayout);
		root.setBackgroundColor(Color.parseColor(sp.getString("bgcolor", "#4CAF50")));// android:background="#388E3C"
		
		btn_play = (Button) findViewById(R.id.button_play);
		btn_open = (Button) findViewById(R.id.button_open);
		btn_stop = (Button) findViewById(R.id.button_stop);
		
		int btnbgcolor = Color.parseColor(sp.getString("btnbgcolor", "#8BC34A"));
		btn_play.setBackgroundColor(btnbgcolor);
		btn_open.setBackgroundColor(btnbgcolor);
		btn_stop.setBackgroundColor(btnbgcolor);
	//	seekbar= (SeekBar) findViewById(R.id.seekBar1);
		
		ck_loop = (CheckBox) findViewById(R.id.checkBox_loop);
		ck_loop.setChecked(true);
		
		   viewPager = (ViewPager) findViewById(R.id.viewpager);  
		   
		   LayoutInflater inflater=getLayoutInflater();  
	        view1 = inflater.inflate(R.layout.modlist, null);  
	        view2 = inflater.inflate(R.layout.modfilelist,null);  
	        view6 = inflater.inflate(R.layout.modfolderlist, null);
	        view1.setBackgroundColor(Color.parseColor(sp.getString("listbgcolor", "#388E3C")));
	        view2.setBackgroundColor(Color.parseColor(sp.getString("listbgcolor", "#388E3C")));
	        view6.setBackgroundColor(Color.parseColor(sp.getString("listbgcolor", "#388E3C")));
		       
	        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
	        viewList.add(view6);  
	        viewList.add(view1);  
	        viewList.add(view2);  
	        
	      
	        PagerAdapter pagerAdapter = new PagerAdapter() {  
	              
	        	
	        
	        	
	        	@Override
	        	public CharSequence getPageTitle(int position) {
	        		return super.getPageTitle(position);
	        	}
	        	
	        	
	            @Override  
	            public boolean isViewFromObject(View arg0, Object arg1) {  
	                // TODO Auto-generated method stub  
	                return arg0 == arg1;  
	            }  
	              
	            @Override  
	            public int getCount() {  
	                // TODO Auto-generated method stub  
	                return viewList.size();  
	            }  
	              
	            @Override  
	            public void destroyItem(ViewGroup container, int position,  
	                    Object object) {  
	                // TODO Auto-generated method stub  
	                container.removeView(viewList.get(position));  
	            }  
	              
	            @Override  
	            public Object instantiateItem(ViewGroup container, int position) {  
	                // TODO Auto-generated method stub  
	                container.addView(viewList.get(position));  
	                  
	                  
	                return viewList.get(position);  
	            }
	            
	            @Override
	            public void setPrimaryItem(View container, int position,
	            		Object object) {
	            	//当页面滑动会调用这个方法，position是当前页面索引
	            	super.setPrimaryItem(container, position, object);
	            }
	          
	        };  
	          
	       // pagerAdapter.setPrimaryItem(container, position, object);
	        viewPager.setAdapter(pagerAdapter);  
	        viewPager.setCurrentItem(1);

	        viewPager1 =  (ViewPager) findViewById(R.id.viewpager1);  
	        

			   
			  
		        view3 = inflater.inflate(R.layout.musicinfo, null);  
		        view4 = inflater.inflate(R.layout.musicpat,null);  
		        view5 = inflater.inflate(R.layout.musicins,null); 
		       
//		        view1.setBackgroundColor(Color.parseColor("#388E3C"));
//		        view2.setBackgroundColor(Color.parseColor("#388E3C"));
		        viewList1 = new ArrayList<View>();// 将要分页显示的View装入数组中  
		        viewList1.add(view3);  
		        viewList1.add(view4);  
		        viewList1.add(view5);  
		    	edittext = (EditText) view3.findViewById(R.id.editText1);
		    	edittext1 = (EditText) view4.findViewById(R.id.editText_musicpat);
		    	edittext1.setText("这里将显示乐谱信息\n(一个Row的格式是这样的：XXX-XX-XX-XXX，比如C-3 02 30 037，其中C-3为音符名，02是乐器的编号，30是音量（音量的范围为0到40之间，也有可能是其它的控制信息，具体请查看Milky Tracke的帮助文档），037是效果信息（Effect），其中第一位0代表是琶音效果，后两位37是效果的参数)");
		    	edittext2 = (EditText) view5.findViewById(R.id.editText_ins);
		    	edittext2.setText("这里将显示乐器信息\n(有些歌曲作者和备注会写在乐器名称上)");
		        PagerAdapter pagerAdapter1 = new PagerAdapter() {  
		              
		        	
		        	
		        	@Override
		        	public CharSequence getPageTitle(int position) {
		        		return super.getPageTitle(position);
		        	}
		        	
		        	
		            @Override  
		            public boolean isViewFromObject(View arg0, Object arg1) {  
		                // TODO Auto-generated method stub  
		                return arg0 == arg1;  
		            }  
		              
		            @Override  
		            public int getCount() {  
		                // TODO Auto-generated method stub  
		                return viewList1.size();  
		            }  
		              
		            @Override  
		            public void destroyItem(ViewGroup container, int position,  
		                    Object object) {  
		                // TODO Auto-generated method stub  
		                container.removeView(viewList1.get(position));  
		            }  
		              
		            @Override  
		            public Object instantiateItem(ViewGroup container, int position) {  
		                // TODO Auto-generated method stub  
		                container.addView(viewList1.get(position));  
		                  
		                  
		                return viewList1.get(position);  
		            }  
		        };  
		        viewPager1.setAdapter(pagerAdapter1);  
		         
	        
	        
		list = (ListView) view1.findViewById(R.id.listView1);
		list1 = (ListView) view2.findViewById(R.id.listView2);
		list2 = (ListView) view6.findViewById(R.id.listView_folder);
		
		loadfilelist();
		loadDirlist(null);
		int lengh = mListTitle.length;
		for(int i =0; i < lengh; i++) {
		    Map<String,Object> item = new HashMap<String,Object>();
		    item.put("title", mListTitle[i]);
		    item.put("text", mListStr[i]);
		    mData.add(item);
		}
		 
		    
		SimpleAdapter adapter = new SimpleAdapter(this,mData,android.R.layout.simple_list_item_2,
				new String[]{"title","text"},new int[]{android.R.id.text1,android.R.id.text2});
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> adapterView, View view, int position,
			    long id) {
		    TextView tv = 	(TextView) view.findViewById(android.R.id.text2);

		    try {
            	if(player!=null){
            		player.stop();
            	}
    			module = new Module(getResources().openRawResource(Integer.parseInt(tv.getText().toString()) ) );
    		//	ibxm= new IBXM(module, 48000);
    			player = new Player( MainActivity.this,module, interpolation, ck_loop.isChecked() );
    			edittext.setText ( player.getModuleInfo()+"\n持续时长(Duration):"+player.getSongtime() );
    			edittext1.setText(module.getPatterns());
    			edittext2.setText(module.getInstruments());
    			Thread thread = new Thread( player );
    			thread.start();

    		} catch (Exception e) {
    			// TODO 自动生成的 catch 块
    			 Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
	               
    		} 
		    }
		});
		edittext.setText("ModPlayer 1.5 by Specher.\n右滑列表显示SD卡下的"+filepath+"文件夹内的文件\n左滑列表显示SD卡目录\n右滑此处查看乐谱、乐器信息");
		
		scrollView =(ScrollView) findViewById(R.id.scrollView1);
//		scrollView.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);
//		scrollView.setScrollbarFadingEnabled(false);
//		scrollView.setScrollContainer(true);
		btn_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				if(player!=null){
					
				player.play();
				}
			}
		});
		
	btn_stop.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			if(player!=null){
				player.stop();
			}
		}
	});
		
		btn_open.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				 try {
	                    Intent intt = new Intent(Intent.ACTION_GET_CONTENT);
	                    intt.setType("audio/x-mod");//设置类型，*/*任意类型，任意后缀的可以这样写。
	                    intt.addCategory(Intent.CATEGORY_OPENABLE);
	                    startActivityForResult(intt, 1);
	                }catch (Exception e){
	                    Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
	                }
			}
		});
		
		ck_loop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				
					if(player!=null){
						player.setLoop(ck_loop.isChecked());
					}
			}
		});
		
//		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//			
//			@Override
//			public void onStopTrackingTouch(SeekBar arg0) {
//				// TODO 自动生成的方法存根
//			//	ibxm.seek(arg0.getProgress());
//			}
//			
//			@Override
//			public void onStartTrackingTouch(SeekBar arg0) {
//				// TODO 自动生成的方法存根
//				
//			}
//			
//			@Override
//			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
//				// TODO 自动生成的方法存根
//				
//			}
//		});
		
		Intent intent = getIntent();
		String action = intent.getAction();
		if(intent.ACTION_VIEW.equals(action))
		{
			 Uri uri = intent.getData();
	         String path = FileUtil.getPath(this, uri);
			openModfile(path);
			
		
		}

		
		
	}
	
	
	
	
	public void loadDirlist(File dir){
		File specItemDir=null;
		if(dir==null){
		specItemDir = Environment.getExternalStorageDirectory();  
		//取出文件列表：                  
		}else{
		 specItemDir = dir;
			
		}
		if(specItemDir==null){  
		    Toast.makeText(this, "读取目录失败！", Toast.LENGTH_SHORT).show();  
		}else{  
			File[] files = specItemDir.listFiles();  
			final List<File> fileslist = Arrays.asList(files);  
			  Collections.sort(fileslist, new Comparator< File>() {  
			   @Override  
			   public int compare(File o1, File o2) {  
			    if (o1.isDirectory() && o2.isFile())  
			          return -1;  
			    if (o1.isFile() && o2.isDirectory())  
			          return 1;  
			    return o1.getName().compareTo(o2.getName());  
			   }  
			  });  
		
			 
			  
			
			
			
			List<HashMap<String, Object>> specs = new ArrayList<HashMap<String,Object>>();  
			HashMap<String, Object> hashMap1 = new HashMap<String, Object>();   
			hashMap1.put("name", "返回上一层");   
			hashMap1.put("length",specItemDir);  
			specs.add(hashMap1); 
	
				for(File spec : fileslist){     
				    HashMap<String, Object> hashMap = new HashMap<String, Object>();
				   if(!spec.isDirectory()){
				    hashMap.put("name", spec.getName());
				    
				    hashMap.put("length", spec.length()<1024?spec.length()+"bytes":spec.length()/1024 + "KB");  
				    specs.add(hashMap);    
				   }else{
					   hashMap.put("name", spec.getName());
					    
					    hashMap.put("length", "文件夹");  
					    specs.add(hashMap);  
				   }
				}     
			
				SimpleAdapter adapter1 =  
				    new SimpleAdapter(  
				        this,  
				        specs,  
				        R.layout.item_files,  
				        new String[]{"name","length"},  
				        new int[]{R.id.textView_title, R.id.textView_length}  
				    );    
				                  
				list2.setAdapter(adapter1);
			
			
		list2.setOnItemClickListener(new OnItemClickListener() {  
		    @Override  
		    public void onItemClick(AdapterView<?> arg0, View arg1,int position, long aid) {  
		       try{
		    	if(position==0){
		    		TextView tv = 	(TextView) arg1.findViewById(R.id.textView_length);
		    		File file = new File(tv.getText().toString());
		    		
		    		loadDirlist(file.getParentFile());
		    		return;
		    	}

		    	if(fileslist.get(position-1).isDirectory()){
		    		loadDirlist(fileslist.get(position-1));
		    	}else{
		    String filePath = fileslist.get(position-1).getAbsolutePath();  
		        openModfile(filePath);	
		    	}
	    		} catch (Exception e) {
	    			// TODO 自动生成的 catch 块
	    			 Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
		               
	    		} 
		    }  
		});  
		}
		 
		   
	  
	}
	
	
	
	public void loadfilelist(){
		PATH = Environment.getExternalStorageDirectory() + "/";  
        
		File specItemDir = new File(PATH +  filepath);  
		//取出文件列表：  
				
		if(!specItemDir.exists()){  
		    specItemDir.mkdir();  
		}  
		                          
		if(!specItemDir.exists()){  
		    Toast.makeText(this, "创建歌曲目录文件夹失败！", Toast.LENGTH_SHORT).show();  
		}else{  
			final File[] files = specItemDir.listFiles();  
			List<HashMap<String, Object>> specs = new ArrayList<HashMap<String,Object>>();  
			
			HashMap<String, Object> hashMap1 = new HashMap<String, Object>();   
			  hashMap1.put("name", "刷新");   
			    hashMap1.put("length","点击刷新列表");  
			    specs.add(hashMap1); 
			    
				if(files.length == 0){
					HashMap<String, Object> hashMap = new HashMap<String, Object>();    
					   
				    hashMap.put("name", "这里什么都没有！");   
				    hashMap.put("length","请把xm/mod/s3m文件放到SD卡下的"+filepath+"文件夹内。");  
				    specs.add(hashMap); 
				}
				
				
				
				for(File spec : files){     
				     
				    HashMap<String, Object> hashMap = new HashMap<String, Object>();
				    hashMap.put("name", spec.getName());   
				    hashMap.put("length", spec.length()<1024?spec.length()+"bytes":spec.length()/1024 + "KB");  
				    specs.add(hashMap);    
				}     
				                  
				SimpleAdapter adapter1 =  
				    new SimpleAdapter(  
				        this,  
				        specs,  
				        R.layout.item_files,  
				        new String[]{"name","length"},  
				        new int[]{R.id.textView_title, R.id.textView_length}  
				    );    
				                  
				list1.setAdapter(adapter1);
			
			
		list1.setOnItemClickListener(new OnItemClickListener() {  
		    @Override  
		    public void onItemClick(AdapterView<?> arg0, View arg1,int position, long aid) {  
		       try{
		    	if(position==0){
		    		filepath=PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("filepath", "ModPlayer");
		    		
		    		loadfilelist();
		    		return;
		    	}
		    	   
		    	String filePath = files[position-1].getAbsolutePath();  
		       
		        openModfile(filePath);	
	    		} catch (Exception e) {
	    			// TODO 自动生成的 catch 块
	    			 Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
		               
	    		} 
		    }  
		});  
		}
		 
		   
	  
	}
	
	  /**
     * 改变actionbar颜色
     * @param isNight
     */
    public static void changeActionbarSkinMode(ActionBar mActionbar,int color){

        setBackgroundAlpha(mActionbar,color);
    }
    public static void setBackgroundAlpha(ActionBar view, int baseColor) {
        int rgb = baseColor;
        Drawable drawable = new ColorDrawable(rgb);
        if(view!=null)
        view.setBackgroundDrawable(drawable);
        view.setTitle(Html.fromHtml("<font color=\"#00FF00\">ModPlayer</font>"));
    }
	
    
    public void openModfile(String path){
    	if(path==null){
            Toast.makeText(this, "读取文件路径失败！:" + path, Toast.LENGTH_SHORT).show();
            return;
        }
       
        try {
        	if(player!=null){
        		player.stop();
        	}
			module = new Module( new java.io.FileInputStream( path ) );
		//	ibxm= new IBXM(module, 48000);
			player = new Player(MainActivity.this, module, interpolation, ck_loop.isChecked() );
			edittext.setText (player.getModuleInfo() +"\n持续时长(Duration):"+player.getSongtime() );
			edittext1.setText(module.getPatterns());
			edittext2.setText(module.getInstruments());
			Thread thread = new Thread( player );
			thread.start();

		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			 Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
               
		} 
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
		if (id == R.id.action_about) {
			  AlertDialog.Builder builder = new Builder(MainActivity.this);
			  final TextView message = new TextView(this);
			  message.setPadding(16, 16, 16, 16);
			  message.setText(Html.fromHtml("支持(仅仅)ProTracker(MOD),Scream Tracker 3(S3M), 和 FastTracker 2(XM)音乐格式的播放器。<br/>" +
			  		"E-mail:<a href=\"https://specher.github.io/\">858198302@qq.com</a><br/>" +
			  		"获取最新动态请关注微信公众号:Sp软件服务"+
			  		"或者加入ModPlayer交流QQ群:<a href=\"http://shang.qq.com/wpa/qunwpa?idkey=19318e1605461154d59e4c32449734e60d699a9c92daba5e0e7bc0f216021c75\">165550424</a><br/>"+
			  		"Specher制作，核心使用<a href=\"https://github.com/martincameron/micromod\">micromod</a>，非常感谢Martin Cameron。<br/>本软件托管在github，欢迎pull request<a href=\"https://github.com/Specher/ModPlayer\">项目地址</a>"));
			  message.setMovementMethod(LinkMovementMethod.getInstance());

			  builder.setView(message);
			  builder.setTitle("关于");
			  builder.setPositiveButton("确定", null);
			 builder.create().show();
			return true;
		}else if(id ==R.id.action_exit){
			finish();
		}else if (id == R.id.action_setting){
			Intent intent = new Intent (MainActivity.this,SettingsActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	  @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode ==1 && resultCode==RESULT_OK){
	            Uri uri = data.getData();
	            String path = FileUtil.getPath(this, uri);//这有问题，系统默认的选择器读取是空
	            openModfile(path);
	        }else{
	        	Toast.makeText(this, data.getAction(), Toast.LENGTH_LONG).show();
	        }

	    }
	
	  @Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		  if(player!=null){
			  player.stop();
		  }
		super.onDestroy();
	}
	  
	
	  public static void setColor(Activity activity, int color) {

	      if (Build.VERSION.SDK_INT >= 19) {

	          // 设置状态栏透明

	          activity.getWindow().addFlags(0x4000000);

	          // 生成一个状态栏大小的矩形

	          View statusView = createStatusView(activity, color);

	          // 添加 statusView 到布局中

	          ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();

	          decorView.addView(statusView);

	          // 设置根布局的参数

	          ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);

	          rootView.setFitsSystemWindows(true);

	          rootView.setClipToPadding(true);

	      }

	  }



	  private static View createStatusView(Activity activity, int color) {

	      // 获得状态栏高度

	      int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");

	      int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);



	      // 绘制一个和状态栏一样高的矩形

	      View statusView = new View(activity);

	      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,

	              statusBarHeight);

	      statusView.setLayoutParams(params);

	      statusView.setBackgroundColor(color);

	      return statusView;

	  }

	  @Override
		protected void onResume() {
			// TODO 自动生成的方法存根
		
			super.onResume();
		
		}
}
