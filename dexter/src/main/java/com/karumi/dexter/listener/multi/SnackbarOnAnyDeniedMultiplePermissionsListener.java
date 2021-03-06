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

package com.karumi.dexter.listener.multi;

import com.karumi.dexter.MultiplePermissionsReport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

/**
 * Utility listener that shows a {@link Snackbar} with a custom text whenever a permission has been
 * denied
 */
public class SnackbarOnAnyDeniedMultiplePermissionsListener extends EmptyMultiplePermissionsListener {

  protected final ViewGroup rootView;
  protected final String text;
  protected final String buttonText;
  protected final View.OnClickListener onButtonClickListener;
  protected final Snackbar.Callback snackbarCallback;
  protected final SnackbarFactory snackbarFactory;

  /**
   * @param rootView Parent view to show the snackbar
   * @param text Message displayed in the snackbar
   * @param buttonText Message displayed in the snackbar button
   * @param onButtonClickListener Action performed when the user clicks the snackbar button
   */
  protected SnackbarOnAnyDeniedMultiplePermissionsListener(ViewGroup rootView, String text,
      String buttonText, View.OnClickListener onButtonClickListener, Snackbar.Callback snackbarCallback, SnackbarFactory factory) {
    this.rootView = rootView;
    this.text = text;
    this.buttonText = buttonText;
    this.onButtonClickListener = onButtonClickListener;
    this.snackbarCallback = snackbarCallback;
    this.snackbarFactory = factory;
  }

  @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
    super.onPermissionsChecked(report);

    if (!report.areAllPermissionsGranted()) {
      showSnackbar();
    }
  }

  public void showSnackbar() {
    Snackbar snackbar = Snackbar.make(rootView, text, Snackbar.LENGTH_LONG);
    if (buttonText != null && onButtonClickListener != null) {
      snackbar.setAction(buttonText, onButtonClickListener);
    }
    if (snackbarCallback != null) {
      snackbar.setCallback(snackbarCallback);
    }

    if (snackbarFactory != null) {
      snackbarFactory.showSnackbar(snackbar);
    } else {
      snackbar.show();
    }
  }

  /**
   * Builder class to configure the displayed snackbar
   * Non set fields will not be shown
   */
  public static class Builder {
    protected final ViewGroup rootView;
    protected final String text;
    protected String buttonText;
    protected View.OnClickListener onClickListener;
    protected Snackbar.Callback snackbarCallback;
    protected SnackbarFactory snackbarFactory;

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

    /**
     * Builds a new instance of {@link SnackbarOnAnyDeniedMultiplePermissionsListener}
     */
    public SnackbarOnAnyDeniedMultiplePermissionsListener build() {
      return new SnackbarOnAnyDeniedMultiplePermissionsListener(rootView, text, buttonText, onClickListener, snackbarCallback, snackbarFactory);
    }
  }

  public interface SnackbarFactory {

    void showSnackbar(Snackbar snackbar);
  }
}
