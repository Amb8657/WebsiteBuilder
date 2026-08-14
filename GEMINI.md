# WebsiteBuilder Gemini instructions

You are the coding agent for the private Android project `Amb8657/WebsiteBuilder`.

## Goal
Continue the Website Builder V3 implementation until it is a genuinely usable visual website editor, not merely a mock UI.

## Current priority
Fix and finish the issues recorded from physical-device testing:
- Match the approved Builder V3 UI/design rather than the temporary/simple UI.
- Show the approved `gmail` branding image on startup/splash and use it as the launcher icon.
- Text: free X/Y movement; selection handle above the object; independent resize; resizing must scale text appropriately; prevent canvas/page scrolling while manipulating a selected element; preview must preserve exact editor coordinates.
- Image: free movement and independent resize; resizing must not unexpectedly crop; cropping should be an explicit separate operation; contextual image properties only; remove irrelevant text-colour/action controls; preview must render the actual selected image.
- Button: provide button background colour, text colour, shape/radius, sizing and typography controls; actions must work in preview; deleting a button must warn about/remediate attached action/page dependencies.
- Section: make it a useful container/group/layout region with clear hierarchy and predictable child placement.
- Shape: make it a visual container/background element that can optionally contain text, images and buttons when the user chooses; provide useful fill, border, radius and opacity controls.
- Tool: only expose tools that have a real editor function; do not ship dead controls.
- Remove the standalone Spacer element. Spacing should be achievable through normal object movement/layout distance controls.
- Use contextual properties: only show controls relevant to the selected element type.
- Preview must use the same persisted document model and coordinates as the editor.
- Keep page navigation/actions functional.
- Preserve user data and avoid regressions.

## Engineering rules
- Inspect the existing code before changing architecture. Reuse the existing V3 model/controllers where practical.
- Prefer small, testable Java classes over putting all logic in one Activity.
- Do not fake interactions with buttons that do nothing.
- Do not use hard-coded device-specific coordinates.
- Keep editor coordinates in a stable document coordinate system and transform them to the device canvas.
- Separate editor gestures from ScrollView gestures so dragging/resizing a selected element never scrolls the page.
- Keep crop and resize as distinct operations for images.
- Every property control must write to the document model and immediately update both editor and preview.
- Run the Android build after meaningful changes. Fix compilation errors before considering a task complete.
- Do not commit API keys, tokens, passwords, or other secrets.
- The Gemini API key is available only as the GitHub Actions secret `GEMINI_API_KEY`.

## Workflow
When invoked from an issue/comment, inspect the current repository state and implement the requested change directly. For broad requests, work through the highest-priority unfinished items above, build, and report what changed and what remains.
