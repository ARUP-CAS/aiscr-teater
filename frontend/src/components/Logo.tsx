import React from 'react'
import LogoImage from 'assets/logo.png'
import { Link } from 'react-router-dom'

export function Logo(): JSX.Element {
  return (
    <Link className="logo" to="/" title="Archeologický informačný systém">
      <img src={LogoImage} className="logo-image" alt="Logo" title="Logo" />
    </Link>
  )
}
