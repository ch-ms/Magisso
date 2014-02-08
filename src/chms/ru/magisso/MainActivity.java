package chms.ru.magisso;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final String CURRENT_IMAGE_URI_KEY = "currentImageUri";

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
			// Check image size
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

			Canvas canvas = new Canvas(currentBitmap);
			canvas.drawBitmap(b, 0, 0, null);

			b.recycle();
			ivImage.setImageBitmap(currentBitmap);
			
			setCurrentImageUri(uri);
			return true;
		} catch (Exception e) {
			Log.e(TAG, "error while decoding image", e);
			return false;
		}
	}
	
	/**
	 * Set current image uri
	 */
	private void setCurrentImageUri(Uri uri){
		getPreferences(MODE_PRIVATE).edit().putString(CURRENT_IMAGE_URI_KEY, uri.toString()).commit();
	}
	
	/**
	 * Get current image uri
	 * @return Current image uri, if there is no image then return empty string ("") 
	 */
	private String getCurrentImageUri(){
		return getPreferences(MODE_PRIVATE).getString(CURRENT_IMAGE_URI_KEY, "");
	}

	/**
	 * How much line we draw
	 */
	private final int LINES_COUNT = 200;

	/**
	 * Available colors for lines
	 */
	private final int COLORS[] = { 0xffed1c24, 0xfff26522, 0xfffbaf5d,
			0xfffff200, 0xff8dc63f, 0xff39b54a, 0xff00a651, 0xff00a99d,
			0xff00aeef, 0xff0072bc, 0xff2e3192, 0xff662d91, 0xff92278f,
			0xffec008c, 0xffed145b };

	/**
	 * Make random visual effect with lines
	 * 
	 * @param v
	 */
	public void makeMagic(View v) {
		Log.i(TAG, "make magic");
		Canvas canvas = new Canvas(currentBitmap);
		Paint p = new Paint();

		int maxWidth = ivImage.getWidth();
		int maxHeight = ivImage.getHeight();

		int linesLeft = LINES_COUNT;

		while (linesLeft > 0) {
			// randomize color
			int color = COLORS[(int) (Math.random() * COLORS.length)];
			p.setColor(color);
			// randomize alpha
			p.setAlpha((int) (Math.random() * 155 + 150));

			// randomize coordinates
			int x1, y1, x2, y2;
			x1 = (int) (Math.random() * maxWidth);
			x2 = (int) (Math.random() * maxWidth);
			y1 = (int) (Math.random() * maxHeight);
			y2 = (int) (Math.random() * maxHeight);
			canvas.drawLine(x1, x2, y1, y2, p);
			linesLeft--;
		}

		ivImage.setImageBitmap(currentBitmap);
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
	 * Reset image in image view
	 * @param v
	 */
	public void resetImage(View v) {
		Log.i(TAG, "reset");
		String uri = getCurrentImageUri();
		if(uri!=""){ decodeImageAndPutInImageView(Uri.parse(uri)); }
	}

	/**
	 * Saves current image
	 */
	public void save() {
		Log.i(TAG, "save");
	}
}
