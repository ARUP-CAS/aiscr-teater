import React, { FC } from 'react'
import times from 'lodash/times'
import { Link } from 'react-router-dom'
import { Category } from './_types'
import { createURL } from '../../utils/createURL'
import { URL_DESCRIPTION } from '../../constants/URL'

function Loading(): JSX.Element {
  return (
    <>
      {times(
        15,
        (index): JSX.Element => {
          return <li key={index} className="loading-background" />
        }
      )}
    </>
  )
}

export const Categories: FC<{ data?: Category[]; loading?: boolean }> = ({
  data = [],
  loading = false
}): JSX.Element => (
  <ul className="categories">
    {loading ? (
      <Loading />
    ) : (
      data.map(
        (category): JSX.Element => (
          <li key={`category-${category.url}`}>
            <Link
              to={createURL(URL_DESCRIPTION, category.url)}
              title={category.name}
            >
              {category.name}
            </Link>
          </li>
        )
      )
    )}
  </ul>
)
