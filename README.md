# 🎮 Tic-Tac-Toe – Low-Level Design (LLD) in Java  

This project demonstrates the **Low-Level Design (LLD)** of a **Tic-Tac-Toe** game implemented in **Java**, focusing on **Object-Oriented Programming (OOP)**, **SOLID principles**, and **design patterns**.  

The case study explores how a simple game can be modeled systematically — from **requirements → UML diagrams → class responsibilities → implementation** — using modular and extensible code architecture.  

---

## 🔎 General Concepts  

### 🔹 Object-Oriented Programming (OOP) in Java  

- **Encapsulation** → Private attributes and methods grouped logically inside classes (`TicTacToe`, `GameLogic`), controlling access to internal data.  
- **Abstraction** → Interface `IGameLogic` defines essential behaviors like `makeMove()` and `checkWin()` without exposing implementation.  
- **Inheritance** → `SmartGameLogic` extends `GameLogic`, reusing and refining existing functionality (AI behavior).  
- **Polymorphism** → The interface `IGameLogic` allows dynamic method dispatch — `TicTacToe` can use `GameLogic` or `SmartGameLogic` interchangeably.  

---

## 🧩 Low-Level Design (LLD)  

LLD bridges the gap between conceptual requirements and working implementation through a structured approach:  

1. **Requirement Analysis**  
2. **Use Case Identification**  
3. **NVT Technique** (Narrative → Visual → Textual)  
4. **UML Modeling** (Use Case, Class, Sequence Diagrams)  
5. **Class & Object Design**  
6. **Responsibility Assignment**  

---

## 🧠 Case Study: Tic-Tac-Toe  

Tic-Tac-Toe is chosen for this case study as it provides:  
- Clear, rule-based interactions  
- Defined responsibilities among entities  
- Opportunities for extension (e.g., AI, larger boards)  

---

## 📋 Requirement Analysis  

### ✅ Functional Requirements  
- 3×3 game board for two players (X and O).  
- Validate moves before updating the board.  
- Detect win or draw conditions.  
- Support human vs human and human vs computer play.  
- Display scoreboard and restart option.  

### ⚙️ Non-Functional Requirements  
- Modularity (easy to extend or modify).  
- Code reusability and maintainability.  
- Adherence to OOP and SOLID design.  

---

## 🔑 Use Cases  

| Use Case | Description |
|-----------|-------------|
| **Start Game** | Initialize the board and begin play. |
| **Make Move** | Player selects a cell; system validates and updates it. |
| **Check Win** | Determine if a player has won or if the board is full. |
| **Restart** | Ask players to replay without restarting the program. |

---

## 🧩 NVT Technique  

**Narrative** → “A player selects a cell. If the move is valid, it’s recorded on the board. After each move, the game checks for a win or draw. If neither, the next player proceeds.”  

**Visual** → Modeled via UML diagrams:  
- **Use Case Diagram:** Player ↔ Game ↔ Board.  
- **Class Diagram:** `TicTacToe`, `GameLogic`, `SmartGameLogic`, `IGameLogic`.  

**Textual** → Translated into Java classes with defined responsibilities and relationships.  

---

## 🏗️ UML Class Design  

### 🧱 Classes and Responsibilities

| Class | Responsibility |
|--------|----------------|
| `TicTacToe` | Controls game flow, player interaction, and scoreboard. |
| `GameLogic` | Implements the rules of the game (board initialization, move validation, win/draw checks). |
| `SmartGameLogic` | Extends base logic to provide smarter AI behavior. |
| `IGameLogic` | Abstracts essential game operations for dependency inversion. |

---

## 💡 Design Patterns Used  

- **Builder Pattern** → Used in `GameBuilder` class for structured object creation (`TicTacToe` instances).  
- **Strategy Pattern (Lightweight)** → Different move strategies implemented via `GameLogic` and `SmartGameLogic`.  
- **Interface-Based Design** → Enables flexibility and decoupling using `IGameLogic`.  

---

## 🧱 SOLID Principle Application  

| Principle | Implementation | Description |
|------------|----------------|-------------|
| **S – Single Responsibility** | `GameLogic`, `TicTacToe`, and `SmartGameLogic` each handle one concern. | Separation of concerns (logic, flow, and AI). |
| **O – Open/Closed** | Extendable via new classes (e.g., `SmartGameLogic`). | New features can be added without changing core code. |
| **L – Liskov Substitution** | `SmartGameLogic` replaces `GameLogic` wherever `IGameLogic` is expected. | Subclasses behave as expected. |
| **I – Interface Segregation** | Focused `IGameLogic` interface defines only necessary methods. | Prevents dependency on unused methods. |
| **D – Dependency Inversion** | `TicTacToe` depends on `IGameLogic`, not concrete implementations. | Promotes flexibility and loose coupling. |

---

## 🧠 Architecture Flow  

1. `TicTacToe` initializes a game using a `GameBuilder`.  
2. It injects an implementation of `IGameLogic` (`GameLogic` or `SmartGameLogic`).  
3. The board initializes and awaits player input.  
4. Players alternate turns; the system validates and updates moves.  
5. `checkWin()` determines a winner or draw.  
6. Game restarts or ends as chosen.  

---

## 💻 How to Run  

### 🧰 Prerequisites  
- Java JDK 8+  
- Command Line or IDE (VS Code / IntelliJ / BlueJ)

### ▶️ Steps  

```bash
# 1. Clone this repository
git clone https://github.com/<your-username>/TicTacToe-Java.git
cd TicTacToe-Java

# 2. Compile all files
javac *.java

# 3. Run the main class
java TicTacToe
