// ── State ──────────────────────────────────────────────────
let gameId        = null;
let gameFinished  = false;
let aiThinking    = false;
let scores        = { X: 0, O: 0, draws: 0 };

// ── DOM refs ───────────────────────────────────────────────
const boardEl      = document.getElementById('board');
const statusEl     = document.getElementById('status');
const thinkingEl   = document.getElementById('thinkingIndicator');
const gameOverlay  = document.getElementById('gameOverlay');
const overlayMsg   = document.getElementById('overlayMsg');
const overlayEmoji = document.getElementById('overlayEmoji');

// ── Button listeners ───────────────────────────────────────
document.getElementById('newBtn').addEventListener('click', startNewGame);
document.getElementById('overlayBtn').addEventListener('click', () => {
  gameOverlay.style.display = 'none';
  startNewGame();
});

// Hide player2 field when AI mode is on
document.getElementById('ai').addEventListener('change', (e) => {
  document.getElementById('player2').style.display = e.target.checked ? 'none' : '';
});

// ── Start new game ─────────────────────────────────────────
async function startNewGame() {
  const player1 = document.getElementById('player1').value.trim() || 'Player 1';
  const player2 = document.getElementById('player2').value.trim() || '';
  const ai      = document.getElementById('ai').checked ? '1' : '0';

  gameFinished = false;
  aiThinking   = false;
  thinkingEl.style.display  = 'none';
  gameOverlay.style.display = 'none';

  const res = await fetch(`/api/new?player1=${encodeURIComponent(player1)}&player2=${encodeURIComponent(player2)}&ai=${ai}`);
  const j   = await res.json();
  gameId    = j.id;
  renderState(j.state);
}

// ── Polling (1 s) ──────────────────────────────────────────
async function fetchState() {
  if (!gameId || gameFinished || aiThinking) return;
  const res = await fetch(`/api/state?id=${gameId}`);
  renderState(await res.json());
}
setInterval(fetchState, 1000);

// ── Cell click ─────────────────────────────────────────────
async function onCellClick(e) {
  if (!gameId || gameFinished || aiThinking) return;
  const cell = e.currentTarget;
  if (cell.classList.contains('taken')) return;

  const row = cell.dataset.row;
  const col = cell.dataset.col;
  const isAI = document.getElementById('ai').checked;

  // Show "thinking" while waiting for the server round-trip
  if (isAI) {
    aiThinking = true;
    statusEl.textContent = '';
    thinkingEl.style.display = 'flex';
  }

  const res = await fetch(`/api/move?id=${gameId}&row=${row}&col=${col}`);
  const j   = await res.json();
  aiThinking = false;
  renderState(j);
}

// ── Detect winning cells client-side ───────────────────────
function getWinningCells(board, winner) {
  const lines = [
    [[0,0],[0,1],[0,2]], [[1,0],[1,1],[1,2]], [[2,0],[2,1],[2,2]],
    [[0,0],[1,0],[2,0]], [[0,1],[1,1],[2,1]], [[0,2],[1,2],[2,2]],
    [[0,0],[1,1],[2,2]], [[0,2],[1,1],[2,0]],
  ];
  for (const line of lines) {
    if (line.every(([r, c]) => board[r][c] === winner)) return line;
  }
  return null;
}

// ── Render state ───────────────────────────────────────────
function renderState(state) {
  const board  = state.board;
  const p1Name = state.player1 || 'Player 1';
  const p2Name = state.player2 || (state.vsComputer ? 'Computer' : 'Player 2');

  // Update names
  document.getElementById('p1name').textContent = p1Name;
  document.getElementById('p2name').textContent = p2Name;

  // Active player highlight on score cards
  const scoreX = document.getElementById('scoreX');
  const scoreO = document.getElementById('scoreO');
  scoreX.classList.remove('active-x');
  scoreO.classList.remove('active-o');
  if (!state.finished) {
    if (state.currentPlayer === 'X') scoreX.classList.add('active-x');
    else                             scoreO.classList.add('active-o');
  }

  // Find winning cells when game is won
  let winLine = null;
  if (state.finished && state.winner !== '-') {
    winLine = getWinningCells(board, state.winner);
  }

  // Build board cells
  boardEl.innerHTML = '';
  for (let r = 0; r < 3; r++) {
    for (let c = 0; c < 3; c++) {
      const val  = board[r][c];
      const cell = document.createElement('div');
      cell.className    = 'cell';
      cell.dataset.row  = r;
      cell.dataset.col  = c;

      if (val !== '-') {
        const mark     = document.createElement('span');
        mark.className = 'mark';
        mark.textContent = val;
        cell.appendChild(mark);
        cell.classList.add('taken', val === 'X' ? 'x-mark' : 'o-mark');
      }

      // Highlight winning line
      if (winLine && winLine.some(([wr, wc]) => wr === r && wc === c)) {
        cell.classList.add('winning');
      }

      cell.addEventListener('click', onCellClick);
      boardEl.appendChild(cell);
    }
  }

  // Status text & score update (only once per game)
  thinkingEl.style.display = 'none';

  if (state.finished) {
    if (!gameFinished) {
      gameFinished = true;

      if (state.winner === '-') {
        scores.draws++;
        document.getElementById('drawCount').textContent = scores.draws;
        statusEl.textContent = "It's a draw! 🤝";
        setTimeout(() => showOverlay('🤝', "It's a Draw!"), 650);
      } else {
        const winnerName = state.winner === 'X' ? p1Name : p2Name;
        scores[state.winner]++;
        const valEl = state.winner === 'X' ? 'scoreXVal' : 'scoreOVal';
        document.getElementById(valEl).textContent = scores[state.winner];
        statusEl.textContent = `${winnerName} wins! 🎉`;
        setTimeout(() => showOverlay('🏆', `${winnerName} Wins!`), 650);
      }
    }
  } else {
    const currentName = state.currentPlayer === 'X' ? p1Name : p2Name;
    const icon = state.currentPlayer === 'X' ? '🔴' : '🔵';
    statusEl.textContent = `${icon} ${currentName}'s turn`;
  }
}

// ── Show game-over overlay ─────────────────────────────────
function showOverlay(emoji, msg) {
  overlayEmoji.textContent = emoji;
  overlayMsg.textContent   = msg;
  gameOverlay.style.display = 'flex';
}

// ── Theme toggle ───────────────────────────────────────────
const themeBtn = document.getElementById('themeBtn');

function applyTheme(theme) {
  document.documentElement.setAttribute('data-theme', theme);
  themeBtn.textContent = theme === 'light' ? '🌙' : '☀️';
  localStorage.setItem('ttt-theme', theme);
}

// Load saved preference; default = dark
applyTheme(localStorage.getItem('ttt-theme') || 'dark');

themeBtn.addEventListener('click', () => {
  const current = document.documentElement.getAttribute('data-theme');
  applyTheme(current === 'light' ? 'dark' : 'light');
});
