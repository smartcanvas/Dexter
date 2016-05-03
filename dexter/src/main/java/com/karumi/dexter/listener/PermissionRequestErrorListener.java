/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.dexter.listener;

/**
 * Interface to listen to errors while requesting new permissions.
 */
public interface PermissionRequestErrorListener {

  /**
   * Method called whenever the client request permissions before another request
   * has finished.
   */
  void onPermissionsAlreadyBeingRequested();
}
