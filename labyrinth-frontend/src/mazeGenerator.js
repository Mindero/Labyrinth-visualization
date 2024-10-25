import React, { useState, useRef, useEffect } from 'react';

const MazeGenerator = () => {
  // Хранение состояния для генератора лабиринта и размеров
  const [algorithm, setAlgorithm] = useState('DFS');
  const [rows, setRows] = useState(10);
  const [cols, setCols] = useState(10);
  const [maze, setMaze] = useState([]);
  const canvasRef = useRef(null);

  // Функция для отправки запроса на генерацию лабиринта
  const generateMaze = async () => {
    console.log("tring to generate")
    try {
      const response = await fetch('http://localhost:8080/labyrinth/create', {
        method: 'POST',
        headers: {
          "Access-Control-Allow-Headers" : "Content-Type",
              "Access-Control-Allow-Origin": "*",
            'Content-Type': 'application/json',
             "Access-Control-Allow-Methods": "OPTIONS,POST,GET,PATCH",
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ height:rows, width:cols, generatorName:algorithm}),
      });
      const data = await response.json();
      setMaze(data.maze); // Предполагается, что бекенд вернёт лабиринт в виде массива
      console.log("set maze was succesfull")
    } catch (error) {
      console.error('Error fetching maze:', error);
    }
  };

  // useEffect(()=>{
  //   generateMaze();
  // }
  // , []);

  // Функция для визуализации лабиринта на Canvas
  useEffect(() => {
    console.log("useEffect starts");
    console.log(maze.length);
    if (maze.length > 0 && canvasRef.current) {
      console.log(rows, cols);
      const canvas = canvasRef.current;
      const ctx = canvas.getContext('2d');
      const cellSize = Math.min(500 / rows, 500 / cols); // Автоматический размер ячеек для 500x500 canvas
      
      console.log("canvas " + canvas.height + " " + canvas.width)
      // Очищаем canvas перед отрисовкой
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      // Отрисовка лабиринта
      for (let y = 0; y < rows; y++) {
        for (let x = 0; x < cols; x++) {
          console.log(y, x, maze[y][x])
          if (maze[y][x] === true) { // Предполагается, что 1 - это стена
            ctx.fillStyle = 'black';
          } else {
            ctx.fillStyle = 'white'; // Пустое пространство
          }
          ctx.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        }
      }
    }
  }, [maze]);

  return (
    <div style={{ textAlign: 'center' }}>
      <h1>Maze Generator and Pathfinding Visualizer</h1>

      <div>
        <label>
          Select Algorithm:
          <select value={algorithm} onChange={(e) => setAlgorithm(e.target.value)}>
            <option value="DFS">Depth First Search</option>
            <option value="Prim">Prim's Algorithm</option>
            <option value="Kruskal">Kruskal's Algorithm</option>
          </select>
        </label>
      </div>

      <div>
        <label>
          Rows:
          <input
            type="number"
            value={rows}
            onChange={(e) => setRows(Number(e.target.value))}
          />
        </label>
      </div>

      <div>
        <label>
          Columns:
          <input
            type="number"
            value={cols}
            onChange={(e) => setCols(Number(e.target.value))}
          />
        </label>
      </div>

      <button onClick={generateMaze}>Generate Maze</button>

      <div>
        <canvas
          ref={canvasRef}
          width="500"
          height="500"
          style={{ border: '1px solid black', marginTop: '20px' }}
        />
      </div>
    </div>
  );
};

export default MazeGenerator;
