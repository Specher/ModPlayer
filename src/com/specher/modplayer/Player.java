package com.specher.modplayer;


import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.preference.PreferenceManager;
import android.util.Log;


public class Player implements Runnable {
	public static final int SAMPLE_RATE = 48000;
	private Module module;
	private IBXM micromod;
	private boolean playing, loop,isplaying;
	private int duration;
	private Context context;
	 AudioTrack audioTrack;

	public Player( Context context,Module module, boolean interpolation, boolean loop ) {
		
		isplaying=true;
		this.module = module;
		micromod = new IBXM( module, SAMPLE_RATE );

		micromod.setInterpolation( 0 );
		duration = micromod.calculateSongDuration();
		this.loop = loop;
		this.context = context;
	}

	
	public int getDuration() {
		return duration;
	}
	
	public void setLoop(boolean loop){
		this.loop=loop;
		
	}
	
	public void seek(int seek){
		micromod.seek(seek);
	}

	public String getModuleInfo() {
		StringBuffer output = new StringBuffer();
	//	output.append( "Micromod " + Micromod.VERSION + '\n' );
	//	output.append( "Song name: " + module.getSongName() + '\n' );
		//for( int idx = 1; idx <= 31; idx++ ) {
	//		String name = module.getInstrument( idx ).getName();
		//	if( name.trim().length() > 0 ) {
		//		output.append( String.format( "%1$3d ", idx ) + name + '\n' );
		//	}
	//	}
		 module.toStringBuffer(output);
		return output.toString();
	}

	public void stop() {
		playing = false;
		isplaying=false;
	}
	
	public void play(){
		
		if(!(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)){
			isplaying=true;
			
			Thread thread = new Thread( this );
			thread.start();
		}
			
		
	}
	
	public String getSongtime(){
		int secs = getDuration() / SAMPLE_RATE;
		int mins = secs / 60;
		secs = secs % 60;
		return mins + ( secs < 10 ? ":0" : ":" ) + secs;
		
	}
	
	public void run() {
		try {
			int[] mixBuf = new int[ micromod.getMixBufferLength() ];
			byte[] outBuf = new byte[ mixBuf.length * 2 ];
			 Log.i("modplayer","length:"+outBuf.length+" mix:"+mixBuf.length * 2 );
			 
			 audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,SAMPLE_RATE , 
						AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, outBuf.length , AudioTrack.MODE_STREAM);

			try {
				Log.i("modplayer", "start play");
				audioTrack.play();
				int samplePos = 0;
				playing = true;
					byte[] buf = new byte[ micromod.getMixBufferLength() * 2 ];
					
					WavInputStream in = new WavInputStream( micromod, duration,  0 );
				//	byte[] buf = new byte[in.read()];
					
					int remain = in.getBytesRemaining();
					audioTrack.play();
					int i = 0;
					
					
//					 while(playing && (i = in.read(buf, 0, buf.length)) > -1){
//						 audioTrack.write(buf, 0, i);
//				        }
						
					while( playing) {
						int count = remain > buf.length ? buf.length : remain;
						count = in.read( buf, 0, count );
						
					int ret=	audioTrack.write( buf, 0, count );
					
						remain -= count ;
						if(ret<0){//²¥·ÅÍê±Ï
							
							Thread.sleep(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("delay","0")));
						
							break;
						}
						
				} 
					audioTrack.stop();
					Log.i("modplayer", "stop play");
//			
//				
					
				
				
			} finally {
				audioTrack.flush();
				audioTrack.release();
				if(loop && isplaying){
					this.run();
				}
			}
		} catch( Exception e ) {
		}
	}
	
}


