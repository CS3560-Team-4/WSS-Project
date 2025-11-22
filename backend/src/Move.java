public enum Move {
    MoveNorth(0, -1),
    MoveSouth(0, 1),
    MoveEast(1, 0),
    MoveWest(-1, 0),
    MoveNorthEast(1, -1),
    MoveNorthWest(-1, -1),
    MoveSouthEast(1, 1),
    MoveSouthWest(-1, 1);

    public final int dx;
    public final int dy;

    Move(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
