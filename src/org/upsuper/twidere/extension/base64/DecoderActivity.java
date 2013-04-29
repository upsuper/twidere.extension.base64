package org.upsuper.twidere.extension.base64;

import java.util.regex.Pattern;

import org.mariotaku.twidere.Twidere;
import org.mariotaku.twidere.model.ParcelableStatus;
import org.mariotaku.twidere.model.ParcelableUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

public class DecoderActivity extends Activity {

	private static final Pattern PATTERN_BASE64 = Pattern.compile(
			"(?:[a-z0-9+/]{4})*(?:[a-z0-9+/]{2}==|[a-z0-9+/]{3}=)?", 
			Pattern.CASE_INSENSITIVE);

	private TextView mTextContent;
	private TextView mTextPlain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decoder);

		mTextContent = (TextView) findViewById(R.id.text_content);
		mTextPlain = (TextView) findViewById(R.id.text_plain);

		Intent intent = getIntent();
		CharSequence text = "";
		ParcelableStatus status = Twidere.getStatusFromIntent(intent);
		ParcelableUser user = Twidere.getUserFromIntent(intent);
		if (status != null)
			text = status.text;
		else if (user != null)
			text = user.description;

		mTextContent.setText(text);
		Linkify.addLinks(mTextContent, PATTERN_BASE64, "");

		SpannableString spannable = SpannableString.valueOf(mTextContent.getText());
		URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
		for (URLSpan span : spans) {
			int start = spannable.getSpanStart(span);
			int end = spannable.getSpanEnd(span);
			if (start < 0 || end > spannable.length() || start > end)
				continue;
			spannable.removeSpan(span);

			Base64Span b64Span = new Base64Span(span.getURL(), mTextPlain);
			spannable.setSpan(b64Span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		mTextContent.setText(spannable);
	}

	static class Base64Span extends URLSpan {

		private String mContent;
		private TextView mTarget;

		public Base64Span(String url, TextView target) {
			super(url);
			mTarget = target;
			mContent = new String(Base64.decode(url, 0));
		}

		@Override
		public void onClick(View widget) {
			mTarget.setText(mContent);
		}
	}

}
