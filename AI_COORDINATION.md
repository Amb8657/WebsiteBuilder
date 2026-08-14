# AI Collaboration Contract — WebsiteBuilder

This file is the shared handoff point between ChatGPT, Gemini, and the human project owner.

## Single source of truth
GitHub `main` is the canonical project state. Never assume a local/chat-only state is newer than GitHub. Before doing meaningful work, inspect the current branch, recent commits, `BUILD_PROGRESS.md`, `GEMINI.md`, and this file.

## Roles
- Human owner: product decisions, visual approval, device testing, and final approval.
- ChatGPT: repository inspection, architecture/reasoning, implementation/review, planning, regression analysis, and coordination.
- Gemini: repository-native coding agent running through GitHub Actions; inspect, implement, build, test, and report results in GitHub.

## Handoff rules
1. Every agent must inspect current GitHub state before changing code.
2. Do not overwrite or revert another agent's newer work merely because an older chat/task expected it.
3. Prefer small, focused commits and keep `BUILD_PROGRESS.md` current.
4. After meaningful implementation, run the Android build and record the result.
5. If a task is incomplete, record exactly what remains instead of claiming completion.
6. Never commit secrets, API keys, tokens, or credentials.
7. When asked for a project status, report repository, branch, latest commit, build status if known, completed work, unfinished work, active blockers, and the next recommended task.
8. When asked to "check everything", inspect the repository tree, build configuration, workflows, project instructions, progress checklist, recent commits, open PRs/issues, and relevant source files before reporting.

## Agent communication
Use GitHub commits, pull requests, issues/comments, `BUILD_PROGRESS.md`, and this file as the durable communication layer. Chat conversations are not the source of truth.

## Current project direction
Website Builder V3 should become a genuinely usable visual website editor, not a mock UI. Preserve the requirements and priorities in `GEMINI.md` and the checklist in `BUILD_PROGRESS.md`.

## Status template
When reporting status, use this order:
1. Repository / branch
2. Latest commit
3. Build / CI status
4. What is implemented
5. What is unfinished
6. Current blockers / risks
7. Recommended next task
8. Files/commits changed by the agent during this task
