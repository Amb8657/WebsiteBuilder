package com.amb8657.websitebuilder;

/** Small integration layer used by the Android canvas touch listener. */
public final class ElementGestureEngine {
    private final MoveController mover = new MoveController();
    private final ResizeController resizer = new ResizeController();

    public MoveController.Position move(MoveController.Position start, float dx, float dy) {
        return mover.move(start, dx, dy);
    }

    public ResizeController.Geometry resize(ResizeController.Geometry start, ResizeHandle handle, float dx, float dy) {
        return resizer.resize(start, handle, dx, dy);
    }
}
