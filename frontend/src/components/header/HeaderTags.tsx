import React, { FC } from 'react'
import { Helmet } from 'react-helmet'
import { HeaderProps } from './_types'

export const HeaderTags: FC<Pick<HeaderProps, 'title'>> = ({ title = '' }) => (
  <Helmet>
    <title>{`${title}${
      title ? ' | ' : ''
    }Tezaurus archeologické terminologie`}</title>
  </Helmet>
)
