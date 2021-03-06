package com.DJ.emoji;

import androidx.annotation.NonNull;

import com.vanniktech.emoji.R;
import com.DJ.emoji.emoji.Emoji;
import com.DJ.emoji.emoji.EmojiCategory;

final class TestEmojiProvider implements EmojiProvider {
  static EmojiProvider from(final Emoji... emojis) {
    return new TestEmojiProvider(emojis);
  }

  static EmojiProvider emptyCategories() {
    return new EmojiProvider() {
      @Override @NonNull public EmojiCategory[] getCategories() {
        return new EmojiCategory[0];
      }
    };
  }

  static EmojiProvider emptyEmojis() {
    return new EmojiProvider() {
      @Override @NonNull public EmojiCategory[] getCategories() {
        return new EmojiCategory[] {
          new EmojiCategory() {
            @Override @NonNull public Emoji[] getEmojis() {
              return new Emoji[0];
            }

            @Override public int getIcon() {
              return 0;
            }

            @Override public int getCategoryName() {
                return 0;
            }
          }
        };
      }
    };
  }

  final Emoji[] emojis;

  private TestEmojiProvider(final Emoji[] emojis) { // NOPMD
    this.emojis = emojis;
  }

  @Override @NonNull public EmojiCategory[] getCategories() {
    return new EmojiCategory[] {
      new EmojiCategory() {
        @Override @NonNull public Emoji[] getEmojis() {
          return emojis; // NOPMD
        }

        @Override public int getIcon() {
          return R.drawable.emoji_recent;
        }

          @Override public int getCategoryName() {
            return R.string.emoji_category_recent;
        }
      }
    };
  }
}
