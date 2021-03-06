package com.SCYahooligens.android.resumescanr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.SCYYahooligens.android.extractor.InfoExtractor;
import com.SCYYahooligens.android.extractor.Resume;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.hackday.resumescanr.database.ResumeReaderDBHelper;
import com.hackday.resumescanr.database.ResumeReaderDatabaseAPI;

/*import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;*/

public class ResumeScanrActivity extends Activity {
	public static final String PACKAGE_NAME = "com.SCYahooligens.android.resumescanr";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/ResumeScanrActivity/";
	
	// You should have the trained data file in assets folder
	// You can get them at:
	// http://code.google.com/p/tesseract-ocr/downloads/list
	public static final String lang = "eng";

	private static final String TAG = "ResumeScanrActivity.java";

	protected Button _button;
	// protected ImageView _image;
	protected EditText _field;
	protected String _path;
	protected boolean _taken;

	protected static final String PHOTO_TAKEN = "photo_taken";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}
		
		// lang.traineddata file with the app (in assets folder)
		// You can get them at:
		// http://code.google.com/p/tesseract-ocr/downloads/list
		// This area needs work and optimization
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {
				Log.v(TAG, "******************** hhhhhhhhhhhhhhh *********************");
				AssetManager assetManager = getAssets();
				
				/*code to read multiple files*/
				//String[] names = new String[]{"eng.cube.bigrams", "eng.cube.fold", "eng.cube.lm", "eng.cube.nn", 
					//			"eng.cube.params", "eng.cube.size", "eng.cube.word-freq", "eng.tesseract_cube.nn", "eng.traineddata1"};
				
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/" + lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
			
				/*changes*/
				//for (String name : names) {
				//		File file = new File(name);
				//		FileInputStream stream = null;  
				//	      try {
				//		          stream = new FileInputStream(file);   
				//		          while( (len = stream.read(buf)) != -1) {
				//		              out.write(buf, 0, len); 
				//		          }
				//		      }
				//	      finally {
				//	    	  	  stream.close();  
				//		      } 
					      
						//while ((lenf = gin.read(buff)) > 0) {
						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}
				//} //end of for
				in.close();
				//gin.close();
				out.close();
				
				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
			}
		}

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// _image = (ImageView) findViewById(R.id.image);
		_field = (EditText) findViewById(R.id.field);
		_button = (Button) findViewById(R.id.button);
		_button.setOnClickListener(new ButtonClickHandler());

		_path = DATA_PATH + "/ocr.tiff";
	}

	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Log.v(TAG, "Starting Camera app");
			startCameraActivity();
		}
	}

	// Simple android photo capture:
	// http://labs.makemachine.net/2010/03/simple-android-photo-capture/

	protected void startCameraActivity() {
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode: " + resultCode);

		if (resultCode == -1) {
			new ParseResume().execute();
		} else {
			Log.v(TAG, "User cancelled");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(ResumeScanrActivity.PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(ResumeScanrActivity.PHOTO_TAKEN)) {
			//onPhotoTaken();
		}
	}

	
	
	public class ParseResume extends AsyncTask<String, Void, String>{

        @Override

        protected void onPreExecute(){
        	Log.i(TAG, "in Async task PreExecute method ");
        }

        protected String doInBackground(String... params) {
        	Log.i(TAG, "in Async task doInBackground method ");
        	String done = "done";
        	onPhotoTaken();
        	return done;
        }
        protected void onPostExecute(String Result){
        	Log.i(TAG, "In Async task PostExecutemethod");
        	System.out.println(Result);
        }
        
    	
    	@SuppressLint("NewApi")
    	protected void onPhotoTaken() {
    		_taken = true;

    		/*BitmapFactory.Options options = new BitmapFactory.Options();
    		
    		options.inSampleSize = 0;
    		options.inPreferQualityOverSpeed=true;

    		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

    		try {
    			ExifInterface exif = new ExifInterface(_path);
    			int exifOrientation = exif.getAttributeInt(
    					ExifInterface.TAG_ORIENTATION,
    					ExifInterface.ORIENTATION_NORMAL);

    			Log.v(TAG, "Orient: " + exifOrientation);

    			int rotate = 0;

    			switch (exifOrientation) {
    			case ExifInterface.ORIENTATION_ROTATE_90:
    				rotate = 90;
    				break;
    			case ExifInterface.ORIENTATION_ROTATE_180:
    				rotate = 180;
    				break;
    			case ExifInterface.ORIENTATION_ROTATE_270:
    				rotate = 270;
    				break;
    			}

    			Log.v(TAG, "Rotation: " + rotate);

    			if (rotate != 0) {

    				// Getting width & height of the given image.
    				int w = bitmap.getWidth();
    				int h = bitmap.getHeight();

    				// Setting pre rotate
    				Matrix mtx = new Matrix();
    				mtx.preRotate(rotate);

    				// Rotating Bitmap
    				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
    			}

    			// Convert to ARGB_8888, required by tess
    			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

    		} catch (IOException e) {
    			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
    		}*/

    		// _image.setImageBitmap( bitmap );
    		
    		Log.v(TAG, "Before baseApi"+DATA_PATH);

    		TessBaseAPI baseApi = new TessBaseAPI();
    		Log.v(TAG, "failing here!");
    		baseApi.setDebug(true);
    		
    		Log.v(TAG, "Before baseApi datapath");
    		baseApi.init(DATA_PATH, null);
    		Log.v(TAG, "after baseApi datapath");
    		
    		//baseApi.setImage(bitmap);
    		File imageFile = new File(_path);
    		baseApi.setImage(imageFile);
    		Log.v(TAG, "After baseApi");
    		
    		String recognizedText = baseApi.getUTF8Text();
    		baseApi.end();

    		// You now have the text in recognizedText var, you can do anything with it.
    		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
    		// so that garbage doesn't make it to the display.

    		Log.v(TAG, "OCRED TEXT: " + recognizedText);

    		/*if ( lang.equalsIgnoreCase("eng") ) {
    			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
    		}*/
    		
    		/*recognizedText = recognizedText.trim();
    		InfoExtractor infoExtractorInstance = new InfoExtractor();
    		Resume studResume = infoExtractorInstance.getInfo(recognizedText);
    		
    		//Do the call to Info Extractor that will give back an instance of Resume 
    		//which is to be loaded into the database
    		ResumeReaderDBHelper dbHelper = new ResumeReaderDBHelper(getBaseContext());
    		
    		int id=0;
    		//Do insert .. id to be generated automatically // Do manual for now.
    		ResumeReaderDatabaseAPI.insertData(dbHelper, id, studResume.studName, 
    				studResume.studEmail, studResume.studGPA, studResume.studBranch, 
    				studResume.studUniv);*/		
    	}
    }
	
}
