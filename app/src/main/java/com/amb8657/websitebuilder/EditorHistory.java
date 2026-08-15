package com.amb8657.websitebuilder;

import java.util.ArrayDeque;

/** Lightweight bounded undo/redo command history. */
public final class EditorHistory {
    public interface Command { void undo(); void redo(); }
    public static final int MAX_STATES = 30;
    private final ArrayDeque<Command> undo = new ArrayDeque<>();
    private final ArrayDeque<Command> redo = new ArrayDeque<>();

    public void execute(Command command) {
        if (command == null) return;
        command.redo();
        undo.push(command);
        while (undo.size() > MAX_STATES) undo.removeLast();
        redo.clear();
    }
    public boolean undo() { if (undo.isEmpty()) return false; Command c=undo.pop(); c.undo(); redo.push(c); while (redo.size() > MAX_STATES) redo.removeLast(); return true; }
    public boolean redo() { if (redo.isEmpty()) return false; Command c=redo.pop(); c.redo(); undo.push(c); while (undo.size() > MAX_STATES) undo.removeLast(); return true; }
    public int undoSize() { return undo.size(); }
    public int redoSize() { return redo.size(); }
}
