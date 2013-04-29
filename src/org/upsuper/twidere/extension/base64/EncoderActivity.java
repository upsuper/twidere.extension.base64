package org.upsuper.twidere.extension.base64;

import org.mariotaku.twidere.Twidere;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EncoderActivity extends Activity {
	
	private EditText mEditPlain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encoder);
		mEditPlain = (EditText) findViewById(R.id.edit_plain);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.encoder, menu);
		return true;
	}
	
	public void appendText(View view) {
		byte[] plainBytes = mEditPlain.getText().toString().getBytes();
		String result = Base64.encodeToString(plainBytes, 0);
		Twidere.appendComposeActivityText(this, result);
	}

}
