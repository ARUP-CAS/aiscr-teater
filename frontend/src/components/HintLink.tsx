import React from 'react'
import { Link } from 'react-router-dom'

export function HintLink(): JSX.Element {
  return (
    <div className="hint">
      <Link to="/hint">Nápověda</Link>
    </div>
  )
}
