import React, {useRef, useEffect} from 'react';
import * as THREE from 'three';
import { RoundedBoxGeometry } from 'three/examples/jsm/geometries/RoundedBoxGeometry.js';
import {moveCube} from "../services/gameService.ts";
import {CubeData, GameField} from '../interfaces/GameField';

interface GameBoard3DProps {
    gameField: GameField;
    setLevelCompleted: (completed: boolean) => void;
    setGameCompleted: (completed: boolean) => void;
}

const GameBoard3D: React.FC<GameBoard3DProps> = ({
                                                     gameField,
                                                     setLevelCompleted,
                                                     setGameCompleted,
                                                 }) => {
    const mountRef = useRef<HTMLDivElement>(null);
    const cubeMeshRef = useRef<THREE.Mesh | null>(null);
    const textures = {
        ColoredTile_RESET: 'src/assets/colored_reset.png',
        ColoredTile_RED: 'src/assets/colored_red.png',
        PaintingTile_RED: 'src/assets/painting_red.png',
        ColoredTile_YELLOW: 'src/assets/colored_yellow.png',
        PaintingTile_YELLOW: 'src/assets/painting_yellow.png',
        ColoredTile_CYAN: 'src/assets/colored_reset.png',
        PaintingTile_CYAN: 'src/assets/colored_reset.png',
        ColoredTile_FINISH: 'src/assets/colored_reset.png',
        Skybox: 'src/assets/skybox.png',
    };

    useEffect(() => {
        if (!mountRef.current) return;

        const scene = new THREE.Scene();
        scene.background = new THREE.Color(0xf0f0f0);

        const fieldWidth = gameField.field[0].length;
        const fieldDepth = gameField.field.length;
        const centerX = (fieldWidth - 1) / 2;
        const centerZ = (fieldDepth - 1) / 2;

        const cameraOffset = new THREE.Vector3(centerX+5, 5, centerZ + 5);
        const cameraTargetOffset = new THREE.Vector3(centerX, 0, centerZ);

        const camera = new THREE.PerspectiveCamera(
            75,
            mountRef.current.clientWidth / mountRef.current.clientHeight,
            0.1,
            1000
        );
        camera.position.copy(cameraOffset);
        camera.lookAt(cameraTargetOffset);

        const renderer = new THREE.WebGLRenderer();
        renderer.setSize(mountRef.current.clientWidth, mountRef.current.clientHeight);
        mountRef.current.appendChild(renderer.domElement);

        const skyboxMaterial = new THREE.MeshBasicMaterial({ color: 0xdc7f7f, side: THREE.BackSide });

        const skyboxGeometry = new THREE.BoxGeometry(1000, 1000, 1000);
        const skybox = new THREE.Mesh(skyboxGeometry, skyboxMaterial);
        skybox.userData = {keepMe: true};
        scene.add(skybox);

        const light = new THREE.DirectionalLight(0xffffff, 1);
        scene.add(light);
        scene.add(new THREE.AmbientLight(0xffffff, 0.5));

        const renderGameField = (fieldData: GameField)  => {
            fieldData.field.forEach((row, i) => {
                row.forEach((tile, j) => {
                    let texturePath;
                    switch (tile.type) {
                        case 'ColoredTile':
                            texturePath = `src/assets/colored_${tile.color.toLowerCase()}.png`;
                            break;
                        case 'PaintingTile':
                            texturePath = `src/assets/painting_${tile.color.toLowerCase()}.png`;
                            break;
                        case 'FinishTile':
                            texturePath = textures.ColoredTile_FINISH;
                            break;
                        default:
                            return;
                    }

                    const tileTextureTop = new THREE.TextureLoader().load(texturePath);
                    const tileMaterialTop = new THREE.MeshPhongMaterial({ map: tileTextureTop });

                    const tileTextureSide = new THREE.TextureLoader().load(textures.ColoredTile_RESET);
                    const tileMaterialSide = new THREE.MeshPhongMaterial({ map: tileTextureSide });

                    const materials = [
                        tileMaterialSide,
                        tileMaterialSide,
                        tileMaterialTop,
                        tileMaterialSide,
                        tileMaterialSide,
                        tileMaterialSide,
                    ];
                    const tileGeometry = new RoundedBoxGeometry(1, 0.3, 1, 10, 0.08);
                    const tileMesh = new THREE.Mesh(tileGeometry, materials);
                    tileMesh.position.set(j, 0, i);
                    scene.add(tileMesh);
                });
            });
        };

        const createCubeMesh = (cubeData: CubeData) => {
            const cubeSideColors = {
                TOP: cubeData.sides.TOP ? `src/assets/cube_${cubeData.sides.TOP.toLowerCase()}.png` : '',
                BOTTOM: cubeData.sides.BOTTOM ? `src/assets/cube_${cubeData.sides.BOTTOM.toLowerCase()}.png` : '',
                FRONT: cubeData.sides.RIGHT ? `src/assets/cube_${cubeData.sides.RIGHT.toLowerCase()}.png` : '',
                BACK: cubeData.sides.LEFT ? `src/assets/cube_${cubeData.sides.LEFT.toLowerCase()}.png` : '',
                LEFT: cubeData.sides.FRONT ? `src/assets/cube_${cubeData.sides.FRONT.toLowerCase()}.png` : '',
                RIGHT: cubeData.sides.BACK ? `src/assets/cube_${cubeData.sides.BACK.toLowerCase()}.png` : '',
            };

            const cubeMaterials = [
                new THREE.MeshPhongMaterial({ map: cubeSideColors.FRONT ? new THREE.TextureLoader().load(cubeSideColors.FRONT) : null }),
                new THREE.MeshPhongMaterial({ map: cubeSideColors.BACK ? new THREE.TextureLoader().load(cubeSideColors.BACK) : null }),
                new THREE.MeshPhongMaterial({ map: cubeSideColors.TOP ? new THREE.TextureLoader().load(cubeSideColors.TOP) : null }),
                new THREE.MeshPhongMaterial({ map: cubeSideColors.BOTTOM ? new THREE.TextureLoader().load(cubeSideColors.BOTTOM) : null }),
                new THREE.MeshPhongMaterial({ map: cubeSideColors.RIGHT ? new THREE.TextureLoader().load(cubeSideColors.RIGHT) : null }),
                new THREE.MeshPhongMaterial({ map: cubeSideColors.LEFT ? new THREE.TextureLoader().load(cubeSideColors.LEFT) : null }),
            ];

            const cubeGeometry = new THREE.BoxGeometry(0.5, 0.5, 0.5);
            const cubeMesh = new THREE.Mesh(cubeGeometry, cubeMaterials);
            cubeMesh.position.set(cubeData.x, 0.4, cubeData.y);
            return cubeMesh;
        };


        renderGameField(gameField);
        const initialCubeMesh = createCubeMesh(gameField.cube);
        cubeMeshRef.current = initialCubeMesh;
        scene.add(initialCubeMesh);

        const animate = () => {
            requestAnimationFrame(animate);
            renderer.render(scene, camera);
        };

        animate();

        function clearScene(): void {
            const toRemove: THREE.Mesh[] = [];

            scene.traverse((child: THREE.Object3D) => {
                if (child instanceof THREE.Mesh && !child.userData.keepMe) {
                    toRemove.push(child);
                }
            });

            for (const mesh of toRemove) {
                scene.remove(mesh);
            }
        }

        const handleKeyDown = async (event: KeyboardEvent) => {
            const allowedKeys = ['w', 'a', 's', 'd'];
            const pressedKey = event.key.toLowerCase();

            if (allowedKeys.includes(pressedKey)) {
                try {
                    const response = await moveCube(pressedKey);
                    const responseData = response.data;

                    if (responseData.nextLevel === true) {
                        clearScene();
                        renderGameField(responseData);
                        const newCubeMesh = createCubeMesh(responseData.cube);
                        cubeMeshRef.current = newCubeMesh;
                        scene.add(newCubeMesh);
                        setLevelCompleted(true);
                    } else if (responseData.gameCompleted === true) {
                        setGameCompleted(true);
                    } else {
                        const newCubeData = responseData;

                        if (cubeMeshRef.current) {
                            scene.remove(cubeMeshRef.current);
                            cubeMeshRef.current.geometry.dispose();
                            (cubeMeshRef.current.material as THREE.Material[]).forEach((material) => material.dispose());
                        }

                        const newCubeMesh = createCubeMesh(newCubeData);
                        cubeMeshRef.current = newCubeMesh;
                        scene.add(newCubeMesh);
                    }
                } catch (error) {
                    console.error('Error moving cube:', error);
                }
            }
        };



        window.addEventListener('keydown', handleKeyDown);

        return () => {
            window.removeEventListener('keydown', handleKeyDown);
            if (mountRef.current && renderer.domElement.parentNode) {
                mountRef.current.removeChild(renderer.domElement);
            }
            scene.clear();
            scene.traverse((object) => {
                if (object instanceof THREE.Mesh) {
                    object.geometry.dispose();
                    (object.material as THREE.Material[]).forEach((material) => material.dispose());
                }
            });
        };
    }, [gameField]);

    return <div ref={mountRef} style={{ width: '100vw', height: 'calc(100vh - 135px)' }} />;
};

export default GameBoard3D;
