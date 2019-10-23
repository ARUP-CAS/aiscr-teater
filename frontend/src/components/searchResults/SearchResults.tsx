import React, { FC } from 'react'
import { SearchResultsProps } from './_types'
import Column from 'components/column'

function Loading(): JSX.Element {
  return (
    <>
      <div className="column-wrapper">
        <Column loading />
      </div>
      <div className="column-wrapper">
        <Column loading />
      </div>
    </>
  )
}

export const SearchResults: FC<SearchResultsProps> = ({ columns, loading }) => {
  if (loading) {
    return (
      <div className="search-results">
        <Loading />
      </div>
    )
  }

  if (!columns.length) {
    return (
      <div className="search-results empty-results">
        Žádné vyhovující výsledky
      </div>
    )
  }

  return (
    <div className="search-results">
      {columns.map(column => (
        <div className="column-wrapper" key={column.key}>
          <h5>{column.title}</h5>
          <Column data={column.data} />
        </div>
      ))}
    </div>
  )
}
