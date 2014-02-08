package chms.ru.magisso;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Load image
	 * @param v
	 */
	public void loadImage(View v){
		Log.i(TAG, "load image");
	}
	
	/**
	 * Make random visual effect
	 * @param v
	 */
	public void makeMagic(View v){
		Log.i(TAG, "make magic");
	}
	
	/**
	 * Click on save button
	 * @param v
	 */
	public void onBtnSaveClick(View v){
		Log.i(TAG, "btn save click");
	}
	
	/**
	 * Share
	 * @param v
	 */
	public void share(View v){
		Log.i(TAG, "share");
	}
	
	/**
	 * Saves current image
	 */
	public void save(){
		Log.i(TAG, "save");
	}
}
