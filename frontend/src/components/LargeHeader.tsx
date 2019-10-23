import React, { FC } from 'react'

import { SearchContainer } from 'containers/SearchContainer'
import { Header } from './header'
import { HeaderProps } from './header/_types'

export const LargeHeader: FC<
  {
    numberOfSearchResults?: number
  } & HeaderProps
> = ({ numberOfSearchResults, ...props }) => (
  <Header className="large-header" {...props}>
    <div className="large-search">
      <h1>Zadejte hledaný výraz</h1>
      <div className="search-content">
        <SearchContainer numberOfResults={numberOfSearchResults} />
      </div>
    </div>
  </Header>
)
