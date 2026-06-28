package org.solocode.crownfall.Game.Player.Camera;

import org.solocode.Corex.config.Config;

public class CameraSettings {

    @Config("cameraConfig.moveSpeed")
    public static double moveSpeed = 0.6;

    @Config("cameraConfig.zoomSpeed")
    public static double zoomSpeed = 0.5;

    @Config("cameraConfig.maxZoomHeight")
    public static int maxZoomHeight = 20;

    @Config("cameraConfig.minZoomHeight")
    public static int minZoomHeight = 1;

    @Config("cameraConfig.playerPitchCameraRotation")
    public static float playerPitchCameraRotation = 60;

    @Config("cameraConfig.playerYawCameraRotation")
    public static float playerYawCameraRotation = 90;
}