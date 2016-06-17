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

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

public class RationaleSnackbarOnDeniedPermissionListener extends SnackbarOnDeniedPermissionListener {

  protected final Runnable runOnGranted;

  protected RationaleSnackbarOnDeniedPermissionListener(ViewGroup rootView, String text,
      String buttonText, View.OnClickListener onButtonClickListener, Snackbar.Callback snackbarCallback, SnackbarOnDeniedPermissionListener.SnackbarFactory factory,
      Runnable runOnGranted) {
    super(rootView, text, buttonText, onButtonClickListener, snackbarCallback, factory);

    this.runOnGranted = runOnGranted;
  }

  @Override public void onPermissionGranted(PermissionGrantedResponse response) {
    super.onPermissionGranted(response);

    if (runOnGranted != null) {
      runOnGranted.run();
    }
  }

  @Override public void onPermissionDenied(PermissionDeniedResponse response) {
    super.onPermissionDenied(response);

    showSnackbar();
  }

  @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
    super.onPermissionRationaleShouldBeShown(permission, token);

    token.continuePermissionRequest();
  }

  public static class Builder extends SnackbarOnDeniedPermissionListener.Builder {
    protected Runnable runOnGranted;

    protected Builder(ViewGroup rootView, String text) {
      super(rootView, text);
    }

    public static Builder with(ViewGroup rootView, String text) {
      return new Builder(rootView, text);
    }

    public static Builder with(ViewGroup rootView, @StringRes int textResourceId) {
      return Builder.with(rootView, rootView.getContext().getString(textResourceId));
    }

    public Builder withRunnableOnGranted(Runnable runOnGranted) {
      this.runOnGranted = runOnGranted;
      return this;
    }

    public RationaleSnackbarOnDeniedPermissionListener build() {
      return new RationaleSnackbarOnDeniedPermissionListener(rootView, text, buttonText, onClickListener, snackbarCallback, snackbarFactory, runOnGranted);
    }
  }
}
