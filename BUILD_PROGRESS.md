# Build Progress

Current focus: unified Website Builder V3, approved Gmail branding, and a deterministic installable debug APK.

- [x] Android project and GitHub Actions build pipeline
- [x] Unified Builder V3 launcher
- [x] Pages and local persistence
- [x] Image picker and real image rendering
- [x] Preview and page navigation foundation
- [x] HTML export foundation
- [x] Free X/Y movement architecture
- [x] Selection move handle and resize handles
- [x] Section/Shape/Tool element model
- [x] Context-aware element model fields for styling/actions
- [x] Approved gmail branding asset stored for CI build
- [x] Approved gmail launcher icon wired into manifest
- [x] Splash screen branding patched to use gmail logo
- [x] Photoshop-style HSV colour picker in canonical V4
- [x] Text resize auto-scaling with minimum dimensions
- [x] Image resize independent from explicit crop mode
- [x] Contextual properties per element type
- [x] Button styling and preview actions
- [x] Delete dependency confirmation and child cleanup
- [x] Section/container hierarchy foundation
- [x] Shape-as-container foundation
- [x] Deterministic V4 emulator launch verification
- [x] Legacy source-patcher workflows removed
- [x] V4 canvas movement clamped to canvas bounds
- [x] V4 resize clamped to canvas bounds with 24dp minimum dimensions
- [x] V4 manipulation prevents parent canvas scrolling during gestures
- [x] V4 is the sole launcher activity in AndroidManifest
- [x] Final APK build workflow moved to the proven macOS Intel runner
- [ ] Final UI parity with the fully approved design
- [ ] Final QA on a physical device
- [ ] Final installable APK from the latest source commit

## Current verification note
The latest red crosses before the final refactor included GitHub runner-allocation failures where zero job steps executed. The latest source pass now hardens the actual V4 editor gesture path and makes the final APK build use the same macOS Intel runner family that successfully booted the community Android emulator. A new workflow run must still execute successfully before the latest-source APK can honestly be called final.
