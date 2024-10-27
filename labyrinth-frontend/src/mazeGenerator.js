import React, { useState, useRef, useEffect } from 'react';

const WALL = 1;
const PASSAGE = 2;
const IN_SHORTEST_PATH = 3;
const START_SHORTEST_PATH = 4;
const END_SHORTEST_PATH = 5;

function convertFromJsonToMaze (booleanMaze) {
  return booleanMaze.map((row) => row.map((val) => {
    if (val === true) return WALL;
    else return PASSAGE;
  }));
}
function convertMazeToJson (maze){
  return maze.map((row) => row.map((val) => {
    if (val === WALL) return true;
    else return false;
  }));
}

const MazeGenerator = () => {
  // Хранение состояния для генератора лабиринта и размеров
  const [rows, setRows] = useState(10);
  const [cols, setCols] = useState(10);
  const [maze, setMaze] = useState([]);
  const [mazeIsCreated, setMazeIsCreated] = useState(false);

  // Начальная и конечная точка в поиске кратчайшего пути
  const [start, setStart] = useState(null);
  const [end, setEnd] = useState(null);
  
  // ShortestPath
  const [tempShortestPath, setTempShortestPath] = useState([]);
  const [shortestPath, setShortestPath] = useState([]);
  const [stepShortestPath, setStepShortestPath] = useState(0);
  // Флаг, показывающий можно ли кратчайший путь
  const [shortestPathIsRunning, setShortestPathIsRunning] = useState(false);
  
  // AlgoSimualte
  const [visited, setVisited] = useState([]);
  const [tempVisited, setTempVisited] = useState([]); 
  const [stepVisited, setStepVisited] = useState(0);
  // Флаг, показывающий можно ли визуализировать алгоритм кратчайшего пути
  const [algoSimulateIsRunning, setAlgoSimulateIsRunning] = useState(false);
  
  const canvasRef = useRef(null);
  
  // Получение данных алгоритма генерации и алгоритма нахождения кратчайшего пути
  const [algorithm, setAlgorithm] = useState('');
  const [generators, setGenerators] = useState([]);
  const [solver, setSolver] = useState('');
  const [solvers, setSolvers] = useState([]);

  const idealMazeItems = ["Идеальный", "Неидеальный"];
  const [selectedItem, setSelectedItem] = useState(idealMazeItems[0]);

  const cellSize = Math.min(500 / rows, 500 / cols); // Автоматический размер ячеек для 500x500 canvas

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
      setMaze(convertFromJsonToMaze(data.maze)); // Предполагается, что бекенд вернёт лабиринт в виде массива
      setMazeIsCreated(true);
    } catch (error) {
      console.error('Error fetching maze:', error);
    }
  };

  const simulateSolverAlgo = async () => {
    setAlgoSimulateIsRunning(false);
    try{
      const response = await fetch('http://localhost:8080/labyrinth/simulateAlgo', {
        method: 'POST',
        headers: {
          "Access-Control-Allow-Headers" : "Content-Type",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "OPTIONS,POST,GET,PATCH",
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({solverName : solver, mazeDto: {maze: convertMazeToJson(maze)}, 
          start: {row : start.row, column : start.col}, end : {row : end.row, column : end.col}}),
      });
      const data = await response.json();
      setTempVisited(data);
      setStepVisited(0);
      setAlgoSimulateIsRunning(true);
    } catch (error) {
      console.error('Error fetching shortestPath:', error);
    }
  }

  // Основная логика визуализации работы алгоритма
  useEffect(() => {
    if (algoSimulateIsRunning && stepVisited < tempVisited.length) {
      const currentPoint = tempVisited[stepVisited];
      
      // Добавляем текущую точку в посещённые
      setVisited((prev) => [...prev, currentPoint]);
      
      // Запускаем следующий шаг с задержкой
      const timer = setTimeout(() => {
        setStepVisited((prevStep) => prevStep + 1);
      }, 20);

      // Очищаем таймер при размонтировании или остановке
      return () => clearTimeout(timer);
    } else if (stepVisited >= tempVisited.length && algoSimulateIsRunning) {
      // Если точки закончились, строим итоговый путь
      setAlgoSimulateIsRunning(false);
      findShortestPath();
    }
  }, [stepVisited, algoSimulateIsRunning, tempVisited]);
  
  const findShortestPath = async () => {
    try{
      const response = await fetch('http://localhost:8080/labyrinth/findShortestPath', {
        method: 'POST',
        headers: {
          "Access-Control-Allow-Headers" : "Content-Type",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "OPTIONS,POST,GET,PATCH",
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({solverName : solver, mazeDto: {maze:convertMazeToJson(maze)}, 
          start: {row : start.row, column : start.col}, end : {row : end.row, column : end.col}}),
      });
      const data = await response.json();
      setTempShortestPath(data);
      setStepShortestPath(0);
      setShortestPathIsRunning(true);
    } catch (error) {
      console.error('Error fetching shortestPath:', error);
    }
  }

  const visualizeShortestPath = (data) => {
    // Постепенная отрисовка кратчайшего пути
    let step = 0;
    const visualizeSteps = () => {
      if (algoSimulateIsRunning) return;
      if (step < data.length) {
        const currentPoint = data[step];
        setShortestPath((prev) => [...prev, currentPoint]);
        setTimeout(visualizeSteps, 20); // Задержка 50 мс для каждого шага
        step++;
      }
    };

    visualizeSteps();
  };
  
  // Основная логика визуализации работы алгоритма
  useEffect(() => {
    if (shortestPathIsRunning && stepShortestPath < tempShortestPath.length) {
      const currentPoint = tempShortestPath[stepShortestPath];
      
      // Добавляем текущую точку в посещённые
      setShortestPath((prev) => [...prev, currentPoint]);
      
      // Запускаем следующий шаг с задержкой
      const timer = setTimeout(() => {
        setStepShortestPath((prevStep) => prevStep + 1);
      }, 20);

      // Очищаем таймер при размонтировании или остановке
      return () => clearTimeout(timer);
    } else if (stepShortestPath >= tempShortestPath.length && shortestPathIsRunning) {
      // Если точки закончились, то заканчиваем визуализацию
      setShortestPathIsRunning(false);
    }
  }, [stepShortestPath, shortestPathIsRunning, tempShortestPath]);

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

  // Функция для отрисовки лабиринта на canvas
  const drawMaze = () => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    maze.forEach((row, rowIndex) => {
      row.forEach((cell, colIndex) => {
        ctx.fillStyle = (cell === WALL) ? 'black' : 'white';
        ctx.fillRect(colIndex * cellSize, rowIndex * cellSize, cellSize, cellSize);
      });
    });

    // Отрисовка просмотренных алгоритмом клеток
    if (visited.length > 0){
      // console.log("visited", visited);
      ctx.fillStyle = 'lightblue';
      visited.forEach(cell => {
        ctx.fillRect(cell.column * cellSize, cell.row * cellSize, cellSize, cellSize);
      });
    }

    // Отрисовка найденного пути
    if (shortestPath.length > 0) {
      ctx.fillStyle = 'green';
      // console.log("shortestPath", shortestPath);
      shortestPath.forEach(cell => {
        ctx.fillRect(cell.column * cellSize, cell.row * cellSize, cellSize, cellSize);
      });
    }

    // Отрисовка начальной и конечной точек
    if (start) {
      ctx.fillStyle = 'blue';
      ctx.fillRect(start.col * cellSize, start.row * cellSize, cellSize, cellSize);
    }
    if (end) {
      ctx.fillStyle = 'red';
      ctx.fillRect(end.col * cellSize, end.row * cellSize, cellSize, cellSize);
    }

  };

  // Вызывает функцию отрисовки лабиринта, если какой-то из параметров меняется
  useEffect(() => {
    drawMaze();
  }, [maze, start, end, visited, shortestPath]);

  // Обработка клика по canvas
  const handleCanvasClick = (event) => {
    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const col = Math.floor((event.clientX - rect.left) / cellSize);
    const row = Math.floor((event.clientY - rect.top) / cellSize);
    if (!start) {
      setStart({ row, col });
    } else if (!end) {
      setEnd({ row, col });
    }
    else{
      if (end.row === row && end.col === col){
        setEnd(null);
        resetVisualization();
      }
      else if(start.row === row && start.col === col){
        setStart(null);
        resetVisualization();
      }
    }
  };

  const resetVisualization = () => {
    setAlgoSimulateIsRunning(false);
    setShortestPathIsRunning(false);
    setShortestPath([]);
    setVisited([]);
  }  

  // Обнуление лабиринта
  const resetMaze = () => {
    setMaze([]);
    setRows(10);
    setCols(10);
    setShortestPath([]);
    setVisited([]);
    setStart(null);
    setEnd(null);
    resetVisualization();
    // Очищаем canvas
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    setMazeIsCreated(false);
  };

  return (
    <div className='app-container'>
      {/* <h1>Maze Generator and Pathfinding Visualizer</h1> */}
      <div className='settings'>
      { !mazeIsCreated ? (
        <div className='settings-text'>
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
        <div className='settings-text'>
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
          <button onClick={simulateSolverAlgo} disabled = {!start || !end}>Find shortest path</button>
          <button onClick={resetMaze}>Generate new maze</button>
        </div>
      )}
      </div>

      <div className='maze'>
        <canvas
          ref={canvasRef}
          width="500"
          height="500"
          onClick={handleCanvasClick}
          style={{ border: '1px solid black', marginTop: '20px' }}
        />
      </div>
    </div>
  );
};

export default MazeGenerator;
