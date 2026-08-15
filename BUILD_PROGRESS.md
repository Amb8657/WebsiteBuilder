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
- [ ] Final UI parity with the fully approved design
- [ ] Final QA on a physical device
- [ ] Final installable APK from the latest source commit

## Current verification note
The latest CI red crosses included runner-allocation failures where GitHub executed zero job steps; those failures did not exercise the application. The V4 QA workflow has now been changed to use Activity Manager's `am start -W` rather than fragile sleep/poll loops, and the activity-component check uses the correct package name.
