package com.amb8657.websitebuilder;

import java.util.ArrayDeque;

/** Lightweight undo/redo command history foundation. */
public final class EditorHistory {
    public interface Command { void undo(); void redo(); }
    private final ArrayDeque<Command> undo = new ArrayDeque<>();
    private final ArrayDeque<Command> redo = new ArrayDeque<>();

    public void execute(Command command) {
        if (command == null) return;
        command.redo();
        undo.push(command);
        redo.clear();
    }
    public boolean undo() { if (undo.isEmpty()) return false; Command c=undo.pop(); c.undo(); redo.push(c); return true; }
    public boolean redo() { if (redo.isEmpty()) return false; Command c=redo.pop(); c.redo(); undo.push(c); return true; }
}
