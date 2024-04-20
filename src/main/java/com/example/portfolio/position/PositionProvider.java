package com.example.portfolio.position;

import java.util.List;

public interface PositionProvider {
    List<Position> listPositions();

    boolean hasPosition(String symbol);
}
