/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.dexter.listener.single;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

public class RationaleSnackbarOnDeniedPermissionListener extends SnackbarOnDeniedPermissionListener {

  protected final Runnable runOnGranted;
  protected final Runnable runOnDenied;

  protected RationaleSnackbarOnDeniedPermissionListener(ViewGroup rootView, String text,
      String buttonText, View.OnClickListener onButtonClickListener, Snackbar.Callback snackbarCallback, SnackbarOnDeniedPermissionListener.SnackbarFactory factory,
      Runnable runOnGranted, Runnable runOnDenied) {
    super(rootView, text, buttonText, onButtonClickListener, snackbarCallback, factory);

    this.runOnGranted = runOnGranted;
    this.runOnDenied = runOnDenied;
  }

  @Override public void onPermissionGranted(PermissionGrantedResponse response) {
    super.onPermissionGranted(response);

    if (runOnGranted != null) {
      runOnGranted.run();
    }
  }

  @Override public void onPermissionDenied(PermissionDeniedResponse response) {
    super.onPermissionDenied(response);

    if (runOnDenied != null) {
      runOnDenied.run();
    } else {
      showSnackbar();
    }
  }

  @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
    super.onPermissionRationaleShouldBeShown(permission, token);

    token.continuePermissionRequest();
  }

  public static class Builder {
    protected final ViewGroup rootView;
    protected final String text;
    protected String buttonText;
    protected View.OnClickListener onClickListener;
    protected Snackbar.Callback snackbarCallback;
    protected SnackbarFactory snackbarFactory;
    protected Runnable runOnGranted;
    protected Runnable runOnDenied;

    protected Builder(ViewGroup rootView, String text) {
      this.rootView = rootView;
      this.text = text;
    }

    public static Builder with(ViewGroup rootView, String text) {
      return new Builder(rootView, text);
    }

    public static Builder with(ViewGroup rootView, @StringRes int textResourceId) {
      return Builder.with(rootView, rootView.getContext().getString(textResourceId));
    }

    /**
     * Adds a text button with the provided click listener
     */
    public Builder withButton(String buttonText, View.OnClickListener onClickListener) {
      this.buttonText = buttonText;
      this.onClickListener = onClickListener;
      return this;
    }

    /**
     * Adds a text button with the provided click listener
     */
    public Builder withButton(@StringRes int buttonTextResourceId,
        View.OnClickListener onClickListener) {
      return withButton(rootView.getContext().getString(buttonTextResourceId), onClickListener);
    }

    /**
     * Adds a button that opens the application settings when clicked
     */
    public Builder withOpenSettingsButton(String buttonText) {
      this.buttonText = buttonText;
      this.onClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
          Context context = rootView.getContext();
          Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                  Uri.parse("package:" + context.getPackageName()));
          myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
          myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          context.startActivity(myAppSettings);
        }
      };
      return this;
    }

    /**
     * Adds a button that opens the application settings when clicked
     */
    public Builder withOpenSettingsButton(@StringRes int buttonTextResourceId) {
      return withOpenSettingsButton(rootView.getContext().getString(buttonTextResourceId));
    }

    /**
     * Adds a callback to handle the snackbar {@code onDismissed} and {@code onShown} events.
     */
    public Builder withCallback(Snackbar.Callback callback) {
      this.snackbarCallback = callback;
      return this;
    }

    /**
     * Adds a factor to handle the snackbar.
     */
    public Builder withSnackbarFactory(SnackbarFactory factory) {
      this.snackbarFactory = factory;
      return this;
    }

    public Builder runOnGranted(Runnable runOnGranted) {
      this.runOnGranted = runOnGranted;
      return this;
    }

    public Builder runOnDenied(Runnable runOnGranted) {
      this.runOnDenied = runOnDenied;
      return this;
    }

    /**
     * Builds a new instance of {@link SnackbarOnDeniedPermissionListener}
     */
    public RationaleSnackbarOnDeniedPermissionListener build() {
      return new RationaleSnackbarOnDeniedPermissionListener(rootView, text, buttonText, onClickListener, snackbarCallback, snackbarFactory, runOnGranted, runOnDenied);
    }
  }
}
