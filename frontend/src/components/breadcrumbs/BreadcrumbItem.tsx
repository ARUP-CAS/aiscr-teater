import React, { FC } from 'react'
import { Link } from 'react-router-dom'
import { Breadcrumb } from './_types'
import { URL_DESCRIPTION } from 'constants/URL'
import { createURL } from 'utils/createURL'

export const BreadcrumbItem: FC<Breadcrumb> = ({ name, url, active = false }) =>
  active ? (
    <li className="active">{name}</li>
  ) : (
    <li>
      <Link to={createURL(URL_DESCRIPTION, url)} title={name}>
        {name}
      </Link>
    </li>
  )
