// interfaces/GameField.ts
export interface GameFieldTile {
    type: string;
    color: string;
}

export interface CubeSides {
    TOP: string;
    BOTTOM: string;
    FRONT: string;
    BACK: string;
    LEFT: string;
    RIGHT: string;
}

export interface CubeData {
    sides: CubeSides;
    x: number;
    y: number;
}

export interface GameField {
    field: GameFieldTile[][];
    cube: CubeData;
}