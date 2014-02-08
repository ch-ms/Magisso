package chms.ru.magisso;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final int MEDIA_LIB_REQUEST_CODE = 0;

	private static final String TAG = "MainActivity";

	ImageView ivImage;
	Bitmap currentBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ivImage = (ImageView) findViewById(R.id.ivImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Load image
	 * 
	 * @param v
	 */
	public void loadImage(View v) {
		Log.i(TAG, "load image");
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.setType("image/*");
		startActivityForResult(i, MEDIA_LIB_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MEDIA_LIB_REQUEST_CODE && resultCode == RESULT_OK) {
			decodeImageAndPutInImageView(data.getData());
		}
	}

	/**
	 * Decode image and put it to image view
	 * 
	 * @param uri
	 * @return true - decoding success, otherwise false
	 */
	public boolean decodeImageAndPutInImageView(Uri uri) {
		Log.i(TAG, "decode");
		try {
			InputStream s = getContentResolver().openInputStream(uri);
			// Check image size to not load too large image
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			Bitmap b = BitmapFactory.decodeStream(s, null, opt);

			s.close();

			// Sample image to make sure it is a size of our image view
			int w = opt.outWidth, h = opt.outHeight;
			int dw = ivImage.getWidth(), dh = ivImage.getHeight();
			int sample = 1;
			while (w > dw * sample || h > dh * sample) {
				sample = sample * 2;
			}

			opt.inJustDecodeBounds = false;
			opt.inSampleSize = sample;

			s = getContentResolver().openInputStream(uri);
			b = BitmapFactory.decodeStream(s, null, opt);
			s.close();

			if (currentBitmap != null) {
				currentBitmap.recycle();
			}

			currentBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(),
					Bitmap.Config.ARGB_8888);
			
			Log.i(TAG, "draw bitmap on canvas");
			
			Canvas canvas = new Canvas(currentBitmap);
			canvas.drawBitmap(b, 0, 0, null);

			b.recycle();
			ivImage.setImageBitmap(currentBitmap);

			return true;
		} catch (Exception e) {
			Log.e(TAG, "error while decoding image", e);
			return false;
		}
	}

	/**
	 * Make random visual effect
	 * 
	 * @param v
	 */
	public void makeMagic(View v) {
		Log.i(TAG, "make magic");
	}

	/**
	 * Click on save button
	 * 
	 * @param v
	 */
	public void onBtnSaveClick(View v) {
		Log.i(TAG, "btn save click");
	}

	/**
	 * Share
	 * 
	 * @param v
	 */
	public void share(View v) {
		Log.i(TAG, "share");
	}

	/**
	 * Saves current image
	 */
	public void save() {
		Log.i(TAG, "save");
	}
}
