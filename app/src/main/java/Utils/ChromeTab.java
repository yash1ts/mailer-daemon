package Utils;

import android.content.Context;
import android.net.Uri;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.mailerdaemon.app.R;

public class ChromeTab {
  private Context context;
  public ChromeTab(Context context){
    this.context=context;
  }
  public void openTab(String url){
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
    builder.setToolbarColor(ContextCompat.getColor(context,R.color.colorAccent));
    CustomTabsIntent customTabsIntent = builder.build();
    customTabsIntent.launchUrl(context, Uri.parse(url));
  }
}
