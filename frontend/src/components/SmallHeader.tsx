import React, { FC } from 'react'
import { SearchContainer } from 'containers/SearchContainer'
import { Header } from './header'
import { HeaderProps } from './header/_types'

export const SmallHeader: FC<HeaderProps> = props => {
  return (
    <Header {...props}>
      <div className="header-search">
        <SearchContainer />
      </div>
    </Header>
  )
}
