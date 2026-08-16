# Build Progress — Master 10-at-a-time plan

## Current batch under verification: 5

## QA policy
Every batch gets full automated/source-contract regression for all completed batches plus a focused emulator run for the new batch and a critical-path smoke regression for the existing app. Every fifth batch (5, 10, 15, 20, ...) additionally runs the longer emulator lifecycle/persistence regression. A full-project emulator regression remains mandatory before the final APK.

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

## Batch 5 — current implementation/verification
- [ ] Final Builder V3 UI parity
- [ ] Fully contextual property panel
- [ ] Text free X/Y movement and robust selection behavior
- [ ] Text independent resize with font auto-scaling
- [ ] Image property panel and selected-image preview
- [ ] Explicit image crop mode separate from resize
- [ ] Button styling/action controls and preview behavior
- [ ] Button deletion dependency warning/cleanup
- [ ] Section/container hierarchy behavior
- [ ] Shape visual-container behavior

## Batches 6–20
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

Testing will use Android's recommended emulator/instrumented UI-testing approach and, where appropriate, modern UI Automator with explicit app-state waits and screenshots rather than brittle shell XML parsing. Android recommends emulator/device UI behavior tests and screenshot tests for regression coverage. https://developer.android.com/training/testing/other-components/ui-automator
