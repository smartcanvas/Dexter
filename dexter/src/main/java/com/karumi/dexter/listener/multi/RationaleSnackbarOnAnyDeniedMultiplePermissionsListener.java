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
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RationaleSnackbarOnAnyDeniedMultiplePermissionsListener extends SnackbarOnAnyDeniedMultiplePermissionsListener {

  protected final Runnable runOnGranted;

  protected RationaleSnackbarOnAnyDeniedMultiplePermissionsListener(ViewGroup rootView, String text,
      String buttonText, View.OnClickListener onButtonClickListener, Snackbar.Callback snackbarCallback, SnackbarOnAnyDeniedMultiplePermissionsListener.SnackbarFactory factory,
      Runnable runOnGranted) {
    super(rootView, text, buttonText, onButtonClickListener, snackbarCallback, factory);

    this.runOnGranted = runOnGranted;
  }

  @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
    super.onPermissionsChecked(report);

    if (report.areAllPermissionsGranted()) {
      if (runOnGranted != null) {
        runOnGranted.run();
      }
    } else {
      showSnackbar();
    }
  }

  @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
    super.onPermissionRationaleShouldBeShown(permissions, token);

    token.continuePermissionRequest();
  }

  public static class Builder extends SnackbarOnAnyDeniedMultiplePermissionsListener.Builder {
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

    public Builder withRunnableOnGranted(Runnable runnableOnGranted) {
      this.runOnGranted = runnableOnGranted;
      return this;
    }

    public RationaleSnackbarOnAnyDeniedMultiplePermissionsListener build() {
      return new RationaleSnackbarOnAnyDeniedMultiplePermissionsListener(rootView, text, buttonText, onClickListener, snackbarCallback, snackbarFactory, runOnGranted);
    }
  }
}
