package com.DJ.emoji.Sample;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DJ.emoji.EmojiManager;
import com.DJ.emoji.EmojiPopup;
import com.DJ.emoji.facebook.FacebookEmojiProvider;
import com.DJ.emoji.google.GoogleEmojiProvider;
import com.DJ.emoji.googlecompat.GoogleCompatEmojiProvider;
import com.DJ.emoji.twitter.TwitterEmojiProvider;
import com.DJ.emoji.ios.IosEmojiProvider;
import com.DJ.emoji.material.MaterialEmojiLayoutFactory;
import com.DJ.emoji.sample.R;

// We don't care about duplicated code in the sample.
@SuppressWarnings("CPD-START") public class MainActivityAutoCompeteTextView extends AppCompatActivity {
  static final String TAG = "MainActivity";

  ChatAdapter chatAdapter;
  EmojiPopup emojiPopup;

  AutoCompleteTextView editText;
  ViewGroup rootView;
  ImageView emojiButton;
  EmojiCompat emojiCompat;

  @Override protected void onCreate(final Bundle savedInstanceState) {
    getLayoutInflater().setFactory2(new MaterialEmojiLayoutFactory((LayoutInflater.Factory2) getDelegate()));
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main_autocompletetextview);

    chatAdapter = new ChatAdapter();

    editText = findViewById(R.id.main_activity_chat_bottom_message_edittext);
    rootView = findViewById(R.id.main_activity_root_view);
    emojiButton = findViewById(R.id.main_activity_emoji);
    final ImageView sendButton = findViewById(R.id.main_activity_send);

    emojiButton.setColorFilter(ContextCompat.getColor(this, R.color.emoji_icons), PorterDuff.Mode.SRC_IN);
    sendButton.setColorFilter(ContextCompat.getColor(this, R.color.emoji_icons), PorterDuff.Mode.SRC_IN);

    emojiButton.setOnClickListener(ignore -> emojiPopup.toggle());
    sendButton.setOnClickListener(ignore -> {
      final String text = editText.getText().toString().trim();

      if (text.length() > 0) {
        chatAdapter.add(text);

        editText.setText("");
      }
    });

    final RecyclerView recyclerView = findViewById(R.id.main_activity_recycler_view);
    recyclerView.setAdapter(chatAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    setUpEmojiPopup();
  }

  @Override public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menuMainShowDialog:
        emojiPopup.dismiss();
        MainDialog.show(this);
        return true;
      case R.id.menuMainVariantIos:
        EmojiManager.destroy();
        EmojiManager.install(new IosEmojiProvider());
        recreate();
        return true;
      case R.id.menuMainGoogle:
        EmojiManager.destroy();
        EmojiManager.install(new GoogleEmojiProvider());
        recreate();
        return true;
      case R.id.menuMainTwitter:
        EmojiManager.destroy();
        EmojiManager.install(new TwitterEmojiProvider());
        recreate();
        return true;
      case R.id.menuMainFacebook:
        EmojiManager.destroy();
        EmojiManager.install(new FacebookEmojiProvider());
        recreate();
        return true;
      case R.id.menuMainGoogleCompat:
        if (emojiCompat == null) {
          emojiCompat = EmojiCompat.init(new FontRequestEmojiCompatConfig(this,
              new FontRequest("com.google.android.gms.fonts", "com.google.android.gms", "Noto Color Emoji Compat", R.array.com_google_android_gms_fonts_certs)
          ).setReplaceAll(true));
        }
        EmojiManager.destroy();
        EmojiManager.install(new GoogleCompatEmojiProvider(emojiCompat));
        recreate();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void onBackPressed() {
    if (emojiPopup != null && emojiPopup.isShowing()) {
      emojiPopup.dismiss();
    } else {
      super.onBackPressed();
    }
  }

  private void setUpEmojiPopup() {
    emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
        .setOnEmojiBackspaceClickListener(ignore -> Log.d(TAG, "Clicked on Backspace"))
        .setOnEmojiClickListener((ignore, ignore2) -> Log.d(TAG, "Clicked on emoji"))
        .setOnEmojiPopupShownListener(() -> emojiButton.setImageResource(R.drawable.ic_keyboard))
        .setOnSoftKeyboardOpenListener(ignore -> Log.d(TAG, "Opened soft keyboard"))
        .setOnEmojiPopupDismissListener(() -> emojiButton.setImageResource(R.drawable.emoji_ios_category_smileysandpeople))
        .setOnSoftKeyboardCloseListener(() -> Log.d(TAG, "Closed soft keyboard"))
        .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
        .setPageTransformer(new PageTransformer())
        .build(editText);
  }
}
