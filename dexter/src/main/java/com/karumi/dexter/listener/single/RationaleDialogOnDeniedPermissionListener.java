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
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class RationaleDialogOnDeniedPermissionListener extends DialogOnDeniedPermissionListener {

  protected final Runnable runOnGranted;
  protected final Runnable runOnDenied;

  protected RationaleDialogOnDeniedPermissionListener(Context context, String title, String message,
      String positiveButtonText, Drawable icon, DialogFactory factory,
      Runnable runOnGranted, Runnable runOnDenied) {
    super(context, title, message, positiveButtonText, icon, factory);

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
      showDialog();
    }
  }

  @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
    super.onPermissionRationaleShouldBeShown(permission, token);

    token.continuePermissionRequest();
  }

  public static class Builder {
    protected final Context context;
    protected String title;
    protected String message;
    protected String buttonText;
    protected Drawable icon;
    protected DialogFactory dialogFactory;
    protected Runnable runOnGranted;
    protected Runnable runOnDenied;

    protected Builder(Context context) {
      this.context = context;
    }

    public static Builder withContext(Context context) {
      return new Builder(context);
    }

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withTitle(@StringRes int resId) {
      this.title = context.getString(resId);
      return this;
    }

    public Builder withMessage(String message) {
      this.message = message;
      return this;
    }

    public Builder withMessage(@StringRes int resId) {
      this.message = context.getString(resId);
      return this;
    }

    public Builder withButtonText(String buttonText) {
      this.buttonText = buttonText;
      return this;
    }

    public Builder withButtonText(@StringRes int resId) {
      this.buttonText = context.getString(resId);
      return this;
    }

    public Builder withIcon(Drawable icon) {
      this.icon = icon;
      return this;
    }

    public Builder withIcon(@DrawableRes int resId) {
      this.icon = context.getResources().getDrawable(resId);
      return this;
    }

    public Builder withDialogFactory(DialogFactory factory) {
      this.dialogFactory = factory;
      return this;
    }

    public Builder runOnGranted(Runnable runOnGranted) {
      this.runOnGranted = runOnGranted;
      return this;
    }

    public Builder runOnDenied(Runnable runOnDenied) {
      this.runOnDenied = runOnDenied;
      return this;
    }

    public RationaleDialogOnDeniedPermissionListener build() {
      String title = this.title == null ? "" : this.title;
      String message = this.message == null ? "" : this.message;
      String buttonText = this.buttonText == null ? "" : this.buttonText;

      return new RationaleDialogOnDeniedPermissionListener(context, title, message, buttonText, icon, dialogFactory, runOnGranted, runOnDenied);
    }
  }
}
