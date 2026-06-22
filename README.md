# AvatarX — Real-Time Avatar & Body Tracking Android App

A virtual try-on Android app: real-time camera capture, full-body pose tracking, 
body measurement estimation, and live garment overlay.

## Features
- Live camera preview with front/back switching (CameraX)
- Real-time body pose tracking using MediaPipe Pose Landmarker (33 landmarks)
- Body measurement extraction: shoulder width, hip width, shoulder-to-hip ratio
- Live garment overlay that scales and follows the tracked body
- Garment switching on the fly, skeleton-overlay toggle

## Tech Stack
- Kotlin, Jetpack Compose
- CameraX (camera-core, camera-camera2, camera-lifecycle, camera-view)
- MediaPipe Tasks Vision — Pose Landmarker (lite model)
- MVVM architecture
- Jetpack Navigation Compose

## Architecture
- `ui/` — Compose screens (Permission, Home, CameraTracking, GarmentSelect, TryOn)
- `tracking/` — MediaPipe wrapper + body measurement calculations
- `overlay/` — Canvas-based skeleton and garment rendering, with coordinate mapping
  to correct for the camera preview's center-crop scaling
- `model/` — Garment data + repository

## Setup Instructions
1. Clone the repo and open in Android Studio (latest stable).
2. Let Gradle sync — dependencies are declared in `app/build.gradle.kts`.
3. Confirm `pose_landmarker_lite.task` is present at `app/src/main/assets/`.
   If missing, download it from:
   `https://storage.googleapis.com/mediapipe-models/pose_landmarker/pose_landmarker_lite/float16/1/pose_landmarker_lite.task`
4. Run on a physical device (camera features don't work on the emulator).
5. Grant camera permission when prompted.

## Known Limitations
- Single-pose tracking only (no multi-person support)
- Garment overlay uses 2D scaling (no perspective warp / fabric simulation)
- Garment scanning/segmentation (bonus feature) not implemented in this version

## Author
Krishan Verma
