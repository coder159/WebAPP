import React, { useState, useEffect } from 'react'
import axios from 'axios'

const API = 'http://localhost:4000'

export default function App() {
  const [token, setToken] = useState(localStorage.getItem('token') || '')
  const [view, setView] = useState(token ? 'todos' : 'login')
  const [error, setError] = useState('')

  useEffect(() => {
    if (token) setView('todos')
  }, [token])

  const onLoggedIn = (tok) => {
    localStorage.setItem('token', tok)
    setToken(tok)
    setView('todos')
  }

  const logout = () => {
    localStorage.removeItem('token')
    setToken('')
    setView('login')
  }

  return (
    <div style={{ maxWidth: 520, margin: '40px auto', fontFamily: 'system-ui' }}>
      <h1>React Todo Demo</h1>
      {view === 'login' ? (
        <Login onLoggedIn={onLoggedIn} setError={setError} />
      ) : (
        <Todos token={token} onLogout={logout} />
      )}
      {error && <p id="error" style={{ color: 'red' }}>{error}</p>}
    </div>
  )
}

function Login({ onLoggedIn, setError }) {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')

  const submit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      const res = await axios.post(`${API}/login`, { username, password })
      onLoggedIn(res.data.token)
    } catch (err) {
      setError(err?.response?.data?.error || 'Login failed')
    }
  }

  return (
    <form onSubmit={submit}>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
        <label>
          Username
          <input id="username" value={username} onChange={e => setUsername(e.target.value)} />
        </label>
        <label>
          Password
          <input id="password" type="password" value={password} onChange={e => setPassword(e.target.value)} />
        </label>
        <button id="loginBtn" type="submit">Login</button>
      </div>
    </form>
  )
}

function Todos({ token, onLogout }) {
  const [items, setItems] = useState([])
  const [text, setText] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [editText, setEditText] = useState('')

  const headers = { Authorization: `Bearer ${token}` }

  const load = async () => {
    const res = await axios.get(`${API}/items`, { headers })
    setItems(res.data)
  }

  useEffect(() => { load() }, [])

  const add = async () => {
    if (!text.trim()) return
    const res = await axios.post(`${API}/items`, { text }, { headers })
    setText('')
    setItems(prev => [...prev, res.data])
  }

  const startEdit = (item) => {
    setEditingId(item.id)
    setEditText(item.text)
  }

  const save = async () => {
    const res = await axios.put(`${API}/items/${editingId}`, { text: editText }, { headers })
    setItems(prev => prev.map(i => i.id === editingId ? res.data : i))
    setEditingId(null)
  }

  const del = async (id) => {
    await axios.delete(`${API}/items/${id}`, { headers })
    setItems(prev => prev.filter(i => i.id !== id))
  }

  return (
    <div>
      <div style={{ display: 'flex', gap: 8 }}>
        <input id="newItem" placeholder="Add todo" value={text} onChange={e => setText(e.target.value)} />
        <button id="addBtn" onClick={add}>Add</button>
        <button id="logoutBtn" onClick={onLogout}>Logout</button>
      </div>
      <ul id="todoList">
        {items.map(item => (
          <li key={item.id} data-id={item.id}>
            {editingId === item.id ? (
              <>
                <input id="editText" value={editText} onChange={e => setEditText(e.target.value)} />
                <button id="saveBtn" onClick={save}>Save</button>
              </>
            ) : (
              <>
                <span className="todo-text">{item.text}</span>
                <button className="editBtn" onClick={() => startEdit(item)}>Edit</button>
                <button className="deleteBtn" onClick={() => del(item.id)}>Delete</button>
              </>
            )}
          </li>
        ))}
      </ul>
    </div>
  )
}
