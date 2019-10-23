import React, { FC } from 'react'
import { Link } from 'react-router-dom'
import { Breadcrumb } from './_types'
import { BreadcrumbItem } from './BreadcrumbItem'

export const Breadcrumbs: FC<{ data: Breadcrumb[] }> = ({ data = [] }) => (
  <div className="breadcrumbs">
    <ul className="breadcrumbs-list">
      {data.map(breadcrumb => (
        <BreadcrumbItem key={breadcrumb.url} {...breadcrumb} />
      ))}
    </ul>
    <Link to="/" className="remove-search" title="Vymazat vyhledávaní">
      Vymazat vyhledávaní
    </Link>
  </div>
)
