import express from 'express';
import cors from 'cors';

const app = express();
app.use(cors());
app.use(express.json());

// In-memory users/items
const USERS = [{ username: 'admin', password: 'secret' }];
let items = [];
let idCounter = 1;

// Extremely simple token mechanism for demo
const VALID_TOKEN = 'fake-token-123';

// Auth middleware
function auth(req, res, next) {
  const auth = req.headers.authorization || '';
  if (!auth.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Missing or invalid Authorization header' });
  }
  const token = auth.replace('Bearer ', '');
  if (token !== VALID_TOKEN) {
    return res.status(403).json({ error: 'Invalid token' });
  }
  next();
}

// POST /login
app.post('/login', (req, res) => {
  const { username, password } = req.body || {};
  if (!username || !password) {
    return res.status(400).json({ error: 'username and password are required' });
  }
  const ok = USERS.find(u => u.username === username && u.password === password);
  if (!ok) {
    return res.status(401).json({ error: 'Invalid credentials' });
  }
  res.json({ token: VALID_TOKEN });
});

// GET /items (requires auth)
app.get('/items', auth, (req, res) => {
  res.json(items);
});

// POST /items (requires auth)
app.post('/items', auth, (req, res) => {
  const { text } = req.body || {};
  if (!text || typeof text !== 'string' || text.trim().length === 0) {
    return res.status(400).json({ error: 'text is required' });
  }
  const item = { id: idCounter++, text };
  items.push(item);
  res.status(201).json(item);
});

// PUT /items/:id (requires auth)
app.put('/items/:id', auth, (req, res) => {
  const id = Number(req.params.id);
  const { text } = req.body || {};
  const idx = items.findIndex(i => i.id === id);
  if (idx === -1) return res.status(404).json({ error: 'Not found' });
  if (!text || typeof text !== 'string' || text.trim().length === 0) {
    return res.status(400).json({ error: 'text is required' });
  }
  items[idx].text = text;
  res.json(items[idx]);
});

// DELETE /items/:id (requires auth)
app.delete('/items/:id', auth, (req, res) => {
  const id = Number(req.params.id);
  const idx = items.findIndex(i => i.id === id);
  if (idx === -1) return res.status(404).json({ error: 'Not found' });
  items.splice(idx, 1);
  res.status(204).send();
});

const PORT = process.env.PORT || 4000;
app.listen(PORT, () => {
  console.log(`API running on http://localhost:${PORT}`);
});
