# 🎮 Tic-Tac-Toe – Java Web Application

This project is a **web-based Tic-Tac-Toe game implemented using Java, HTML, CSS, and JavaScript**. It demonstrates **Object-Oriented Programming (OOP)**, interface-based design, inheritance, game logic, AI gameplay, frontend-backend communication, Docker containerization, and cloud deployment.

---

## 🔎 General Concepts

### 🔹 Object-Oriented Programming (OOP) in Java

- **Encapsulation** → Game state and game operations are organized inside classes such as `GameState` and `GameLogic`.
- **Abstraction** → Interface `IGameLogic` defines the essential game operations.
- **Inheritance** → `SmartGameLogic` extends `GameLogic` to implement computer gameplay.
- **Polymorphism** → `IGameLogic` can work with both `GameLogic` and `SmartGameLogic`.

---

## 🧠 Case Study: Tic-Tac-Toe

Tic-Tac-Toe is a simple two-player game played on a **3×3 board**.

The application supports:

- 🎮 Player vs Player mode.
- 🤖 Player vs Computer mode.
- 👤 Custom player names.
- ✅ Move validation.
- 🏆 Win detection.
- 🤝 Draw detection.
- 📊 Scoreboard.
- 🔄 New game functionality.
- 🌙 Light/Dark theme.
- 📱 Responsive web interface.

---

## 📋 Requirements

### ✅ Functional Requirements

- Initialize a 3×3 game board.
- Allow players to select cells.
- Validate player moves.
- Switch turns between `X` and `O`.
- Detect winning combinations.
- Detect draw conditions.
- Support Player vs Player gameplay.
- Support Player vs Computer gameplay.
- Maintain game state.
- Display player scores.
- Allow users to start a new game.

### ⚙️ Non-Functional Requirements

- Simple and modular architecture.
- Reusable Java components.
- Maintainable code structure.
- Responsive user interface.
- Easy deployment using Docker.
- Cloud deployment support.

---

## 🧩 Project Components

| Class / File | Responsibility |
|---|---|
| `TicTacToe.java` | Main game entry point and game functionality. |
| `WebServer.java` | Handles HTTP requests and serves the web application. |
| `GameState.java` | Maintains the state of each game. |
| `GameLogic.java` | Implements board and game rules. |
| `SmartGameLogic.java` | Provides computer/AI moves. |
| `IGameLogic.java` | Defines common game operations. |
| `index.html` | Provides the web application structure. |
| `style.css` | Provides styling and responsive UI. |
| `app.js` | Handles frontend interaction and API communication. |

---

## 🧠 OOP Concepts Used

### 🔹 Encapsulation

Game-related data and operations are organized into dedicated classes such as `GameState` and `GameLogic`.

### 🔹 Abstraction

The `IGameLogic` interface defines common game operations without exposing their implementation.

### 🔹 Inheritance

`SmartGameLogic` extends `GameLogic` and reuses its existing game functionality.

### 🔹 Polymorphism

The `IGameLogic` interface allows the application to work with different implementations such as `GameLogic` and `SmartGameLogic`.

### 🔹 Interface

`IGameLogic` defines the common operations that must be implemented by the game logic classes.

### 🔹 Composition

`GameState` contains an `IGameLogic` object to manage the actual game rules and board operations.

### 🔹 Method Overloading

`GameState` provides multiple constructors with different parameters for creating game instances.

### 🔹 Concurrency

`ConcurrentHashMap` is used to safely maintain multiple game sessions in the web server.

### 🔹 Exception Handling

The application uses `try-catch` blocks to handle invalid requests and runtime exceptions safely.
