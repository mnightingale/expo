package expo.modules.av.player.datasource;

import android.content.Context;

import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.Map;

import androidx.annotation.NonNull;
import expo.modules.core.ModuleRegistry;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SharedCookiesDataSourceFactory implements DataSource.Factory {
  private final DataSource.Factory mDataSourceFactory;

  public SharedCookiesDataSourceFactory(Context reactApplicationContext, ModuleRegistry moduleRegistry, String userAgent, Map<String, Object> requestHeaders, TransferListener transferListener) {
    CookieHandler cookieHandler = moduleRegistry.getModule(CookieHandler.class);
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    // TODO: pass to OkHttpDataSource.Factory instead
    if (requestHeaders != null) {
      builder.addInterceptor(chain -> {
        Request.Builder newRequest = chain.request().newBuilder();

        for (Map.Entry<String, Object> headerEntry : requestHeaders.entrySet()) {
          if (headerEntry.getValue() instanceof String) {
            newRequest.addHeader(headerEntry.getKey(), (String) headerEntry.getValue());
          }
        }

        return chain.proceed(newRequest.build());
      });
    }
    if (cookieHandler != null) {
      builder.cookieJar(new JavaNetCookieJar(cookieHandler));
    }
    OkHttpClient client = builder.build();
    mDataSourceFactory = new DefaultDataSource.Factory(reactApplicationContext, new OkHttpDataSource.Factory(client).setUserAgent(userAgent)).setTransferListener(transferListener);
  }

  @Override
  public DataSource createDataSource() {
    return mDataSourceFactory.createDataSource();
  }
}
