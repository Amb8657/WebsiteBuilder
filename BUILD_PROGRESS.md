# Build Progress — Master 10-at-a-time plan

## Current batch under verification: 13

## QA policy
Every batch gets full automated/source-contract regression for all completed batches plus a focused emulator run for the new batch and a critical-path smoke regression for the existing app. Every fifth batch (5, 10, 15, 20, ...) additionally runs the longer emulator lifecycle/persistence regression. A full-project emulator regression remains mandatory before the final APK. GitHub Actions uses concurrency cancellation for the emulator gate so stale queued emulator runs do not compete with the newest commit.

## Completion rule
Implement features in batches of 10. Each batch must be coded and verified before moving to the next batch. After all currently defined batches are complete, run full emulator QA, fix all genuine failures, and only then produce the final APK. Never mark a feature complete without implementation/evidence.

## Batch 1 — core editor quality
- [x] Android project/build pipeline
- [x] Unified V4 launcher
- [x] Pages + local persistence
- [x] Real image picker/rendering
- [x] Preview/page navigation foundation
- [x] HTML export foundation
- [x] Free X/Y movement
- [x] Selection/resize handles
- [x] Section/Shape/Tool model
- [x] Context-aware element properties

## Batch 2 — approved core behavior
- [x] Gmail branding asset
- [x] Gmail launcher icon
- [x] Gmail splash branding
- [x] HSV colour picker
- [x] Text resize auto-scaling
- [x] Image resize independent from crop
- [x] Contextual properties by element type
- [x] Button styling/actions
- [x] Delete dependency confirmation/cleanup
- [x] Section/container hierarchy foundation

## Batch 3 — manipulation/stability
- [x] Shape-as-container foundation
- [x] Deterministic emulator launch verification
- [x] Legacy source patchers removed
- [x] Canvas movement clamping
- [x] Resize clamping + 24dp minimum
- [x] Gesture manipulation prevents parent scrolling
- [x] V4 sole launcher activity
- [x] Proven macOS Intel build runner
- [ ] Final UI parity
- [ ] Final latest-source APK verification

## Batch 4 — advanced editor controls
- [x] Duplicate element
- [x] Copy/paste element
- [x] Undo
- [x] Redo
- [x] Group elements
- [x] Ungroup elements
- [x] Lock element
- [x] Hide/show element
- [x] Layers panel/reordering
- [x] Element rename

## Batch 5 — completed and verified
- [x] Final Builder V3 UI parity layer
- [x] Fully contextual property panel
- [x] Text free X/Y movement and robust selection behavior
- [x] Text independent resize with font auto-scaling
- [x] Image property panel and selected-image preview
- [x] Explicit image crop mode separate from resize
- [x] Button styling/action controls and preview behavior
- [x] Button deletion dependency warning/cleanup
- [x] Section/container hierarchy behavior
- [x] Shape visual-container behavior

## Batch 6 — completed and emulator verified
- [x] Mobile responsive preview mode
- [x] Tablet responsive preview mode
- [x] Desktop responsive preview mode
- [x] All-device preview summary
- [x] Dedicated Preview & Testing panel
- [x] Explicit autosave toggle
- [x] About Project information panel
- [x] Active/inactive project status
- [x] Performance warning scan
- [x] Fullscreen/native preview entry

## Batch 7 — completed and emulator verified
- [x] Project rename
- [x] Project description
- [x] Created timestamp
- [x] Last-modified timestamp
- [x] Project data size
- [x] Progress/status indicator
- [x] Local project privacy flag
- [x] Form/submission data panel foundation
- [x] Local project backup
- [x] Local project restore

## Batch 8 — completed and emulator verified
- [x] Align left
- [x] Center horizontally
- [x] Align right
- [x] Align top
- [x] Center vertically
- [x] Align bottom
- [x] Snap to 8dp grid
- [x] Bring element to front
- [x] Send element to back
- [x] Canvas zoom in/out/reset

## Batch 9 — completed and emulator verified
- [x] Multi-selection entry point
- [x] Distribute elements horizontally
- [x] Distribute elements vertically
- [x] Match selected widths
- [x] Match selected heights
- [x] Align selected elements to first X
- [x] Align selected elements to first Y
- [x] Nudge selected elements left
- [x] Nudge selected elements right
- [x] Nudge selected elements down

## Batch 10 — visual styling controls under verification
- [x] Opacity 100%
- [x] Opacity 75%
- [x] Opacity 50%
- [x] Corner radius 0dp
- [x] Corner radius 12dp
- [x] Corner radius 24dp
- [x] Font size increase
- [x] Font size decrease
- [x] Solid/outline fill toggle
- [x] Visual style reset

## Batch 11 — website structure
- [x] Add page
- [x] Rename page
- [x] Duplicate page
- [x] Delete page
- [x] Reorder pages
- [x] Page background controls
- [x] Home page control
- [x] Navigation preview
- [x] Mobile navigation preview
- [x] Site structure menu

## Batch 12 — responsive design
- [x] Desktop preview
- [x] Tablet preview
- [x] Mobile preview
- [x] Breakpoint presets
- [x] Device-specific visibility
- [x] Responsive positioning
- [x] Responsive sizing
- [x] Responsive typography
- [x] Responsive sections
- [x] Responsive settings reset

## Batch 13 — advanced visual editor upgrade
- [x] Layers/object hierarchy panel
- [x] Multi-select operations entry point
- [x] Group selected elements into a real Section container
- [x] Ungroup selected Section/container
- [x] Duplicate selected element
- [x] Copy/paste selected element
- [x] Lock/unlock with drag enforcement
- [x] Hide/show selected element
- [x] Canvas rulers, zoom, pan and snap controls
- [x] Keyboard shortcuts and contextual element rename

## Batches 14–20
Each batch will contain exactly 10 additional features selected from the approved Master Specification, implemented and verified sequentially. If the master specification grows beyond 200 features, continue with additional 10-feature batches.

## Final QA gate
- [ ] Build latest main
- [ ] Install APK on emulator
- [ ] Launch/stability check
- [ ] Exercise core user flows
- [ ] Pages/navigation
- [ ] Text manipulation
- [ ] Image import/resize/crop
- [ ] Button actions
- [ ] Sections/shapes
- [ ] Save/reopen persistence
- [ ] Preview
- [ ] HTML export
- [ ] UI screenshot/evidence checks
- [ ] No WebsiteBuilder crash
- [ ] No false failure from unrelated Android system services
- [ ] Final APK artifact from latest verified commit

Testing will use Android's recommended emulator/instrumented UI-testing approach and, where appropriate, modern UI Automator with explicit app-state waits and screenshots rather than brittle shell XML parsing. Android recommends emulator/device UI behavior tests and screenshot tests for regression coverage.
