public enum Move {
    MoveNorth(0, -1),
    MoveSouth(0, 1),
    MoveEast(1, 0),
    MoveWest(-1, 0),
    MoveNorthEast(1, -1),
    MoveNorthWest(-1, -1),
    MoveSouthEast(1, 1),
    MoveSouthWest(-1, 1),

    // For keen vision
    MoveNorth2(0, -2),          // 2 spaces ahead north
    MoveSouth2(0, 2),           // 2 spaces ahead south
    MoveEast2(2, 0),            // 2 spaces ahead east
    MoveWest2(-2, 0),           // 2 spaces ahead west

    MoveNorth2East1(1, -2),     // tile east of north 2 
    MoveNorth2West1(-1, -2),    // tile west north 2
    MoveSouth2East1(1, 2),      // tile east of south 2 
    MoveSouth2West1(-1, 2),     // tile west of south 2
    MoveEast2North1(2, -1),     // tile north of east 2
    MoveEast2South1(2, 1),      // tile south of east 2
    MoveWest2North1(-2, -1),    // tile north of west 2
    MoveWest2South1(-2, 1),     // tile south of west 2


    // For narrow vision
    MoveNorth3(0, -3),
    MoveNorth4(0, -4),
    MoveNorth5(0, -5),
    
    MoveSouth3(0, 3),
    MoveSouth4(0, 4),
    MoveSouth5(0, 5),

    MoveEast3(3, 0),
    MoveEast4(4, 0),
    MoveEast5(5, 0),

    MoveWest3(-3, 0),
    MoveWest4(-4, 0),
    MoveWest5(-5, 0),

    // ---- For queen vision ---- //
    // -- Straights -- //
    MoveNorth6(0, -6),
    MoveNorth7(0, -7),
    MoveNorth8(0, -8),
    MoveNorth9(0, -9),
    MoveNorth10(0, -10),

    MoveSouth6(0, 6),
    MoveSouth7(0, 7),
    MoveSouth8(0, 8),
    MoveSouth9(0, 9),
    MoveSouth10(0, 10),

    MoveEast6(6, 0),
    MoveEast7(7, 0),
    MoveEast8(8, 0),
    MoveEast9(9, 0),
    MoveEast10(10, 0),

    MoveWest6(-6, 0),
    MoveWest7(-7, 0),
    MoveWest8(-8, 0),
    MoveWest9(-9, 0),
    MoveWest10(-10, 0),
    
    // -- Diagonals -- //
    // - Northeasts - //
    MoveNorthEast2(2,-2),
    MoveNorthEast3(3,-3),
    MoveNorthEast4(4,-4),
    MoveNorthEast5(5,-5),
    MoveNorthEast6(6,-6),
    MoveNorthEast7(7,-7),
    MoveNorthEast8(8,-8),
    MoveNorthEast9(9,-9),
    MoveNorthEast10(10,-10),

    // - Northwests - //
    MoveNorthWest2(-2,-2),
    MoveNorthWest3(-3,-3),
    MoveNorthWest4(-4,-4),
    MoveNorthWest5(-5,-5),
    MoveNorthWest6(-6,-6),
    MoveNorthWest7(-7,-7),
    MoveNorthWest8(-8,-8),
    MoveNorthWest9(-9,-9),
    MoveNorthWest10(-10,-10),

    // - Southeasts - //
    MoveSouthEast2(2,2),
    MoveSouthEast3(3,3),
    MoveSouthEast4(4,4),
    MoveSouthEast5(5,5),
    MoveSouthEast6(6,6),
    MoveSouthEast7(7,7),
    MoveSouthEast8(8,8),
    MoveSouthEast9(9,9),
    MoveSouthEast10(10,10),

    // - Southwests - //
    MoveSouthWest2(-2,2),
    MoveSouthWest3(-3,3),
    MoveSouthWest4(-4,4),
    MoveSouthWest5(-5,5),
    MoveSouthWest6(-6,6),
    MoveSouthWest7(-7,7),
    MoveSouthWest8(-8,8),
    MoveSouthWest9(-9,9),
    MoveSouthWest10(-10,10);

    public final int dx;
    public final int dy;

    Move(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
