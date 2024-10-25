import React, { useState, useRef, useEffect } from 'react';

const MazeGenerator = () => {
  // Хранение состояния для генератора лабиринта и размеров
  const [rows, setRows] = useState(10);
  const [cols, setCols] = useState(10);
  const [maze, setMaze] = useState([]);
  const [mazeIsCreated, setMazeIsCreated] = useState(false);

  const canvasRef = useRef(null);
  
  const [algorithm, setAlgorithm] = useState('');
  const [generators, setGenerators] = useState([]);
  const [solvers, setSolvers] = useState([]);
  const [solver, setSolver] = useState('');

  const idealMazeItems = ["Идеальный", "Неидеальный"];
  const [selectedItem, setSelectedItem] = useState(idealMazeItems[0]);

  // Функция для отправки запроса на генерацию лабиринта
  const generateMaze = async () => {
    try {
      const response = await fetch('http://localhost:8080/labyrinth/create', {
        method: 'POST',
        headers: {
          "Access-Control-Allow-Headers" : "Content-Type",
              "Access-Control-Allow-Origin": "*",
             "Access-Control-Allow-Methods": "OPTIONS,POST,GET,PATCH",
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ height:rows, width:cols, generatorName:algorithm, idealMaze:(selectedItem === 'Идеальный')}),
      });
      const data = await response.json();
      setMaze(data.maze); // Предполагается, что бекенд вернёт лабиринт в виде массива
      setMazeIsCreated(true);
    } catch (error) {
      console.error('Error fetching maze:', error);
    }
  };

  // Загружаем имена всех генераторов и решателей.
  useEffect(() => {
      const getAlllGenerators = async () => {
        try{
          const response = await fetch('http://localhost:8080/labyrinth/getGenerators', {
            method: 'GET',
            headers: {
              "Access-Control-Allow-Headers" : "Content-Type",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "OPTIONS,POST,GET,PATCH",
              'Content-Type': 'application/json',
            }});
          const data = await response.json();
          setGenerators(data);
          setAlgorithm(data[0]);
        }
        catch (error){
          console.log('Error with getting all generators: ', error);
        }
      };
      const getAllSolvers = async () => {
        try{
          const response = await fetch('http://localhost:8080/labyrinth/getSolvers', {
            method: 'GET',
            headers: {
              "Access-Control-Allow-Headers" : "Content-Type",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "OPTIONS,POST,GET,PATCH",
              'Content-Type': 'application/json',
            }});
          const data = await response.json();
          setSolvers(data);
          setSolver(data[0]);
        }
        catch (error){
          console.log('Error with getting all generators: ', error);
        }
      };
      getAlllGenerators();
      getAllSolvers();
  }, []);

  // Функция для визуализации лабиринта на Canvas
  useEffect(() => {
    if (maze.length > 0 && canvasRef.current) {
      const canvas = canvasRef.current;
      const ctx = canvas.getContext('2d');
      const cellSize = Math.min(500 / rows, 500 / cols); // Автоматический размер ячеек для 500x500 canvas
      
      // console.log("canvas " + canvas.height + " " + canvas.width)
      // Очищаем canvas перед отрисовкой
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      // Отрисовка лабиринта
      for (let y = 0; y < rows; y++) {
        for (let x = 0; x < cols; x++) {
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

      { !mazeIsCreated ? (
        <div>
          <div>
            <label>Select Algorithm: </label>
              <select value={algorithm} onChange={(e) => setAlgorithm(e.target.value)}>
              {/* Отображаем загруженные данные */}
              {generators.map((option, index) => (
                <option key={index} value={option}>
                  {option}
                </option>
              ))}
              </select>
            </div>
            <div>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
              {idealMazeItems.map((item, index) => (
              <li
                key={index}
                onClick={() => setSelectedItem(item)}
                style={{
                  padding: '8px 16px',
                  margin: '4px 0',
                  cursor: 'pointer',
                  backgroundColor: item === selectedItem ? '#007bff' : '#f1f1f1',
                  color: item === selectedItem ? 'white' : 'black',
                  borderRadius: '4px'
                }}
              >
              {item}
              </li>
              ))}
          </ul>
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
        </div>
      ) : (
        <div>
          <div>
            <label>Select algorithm to find shortest path: </label>
              <select value={solver} onChange={(e) => setSolver(e.target.value)}>
              {/* Отображаем загруженные данные */}
              {solvers.map((option, index) => (
                <option key={index} value={option}>
                  {option}
                </option>
              ))}
              </select>
            </div>
        </div>
      )}

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
